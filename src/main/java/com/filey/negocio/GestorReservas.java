package com.filey.negocio;

import com.filey.datos.*;
import com.filey.notificaciones.EventoNotificacion;
import com.filey.notificaciones.GestorNotificaciones;
import com.filey.notificaciones.TipoEvento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorReservas {

    private final Mapa mapa;
    private final List<Reserva> reservas = new ArrayList<>();
    private final ConfiguracionPlazos configuracion;
    private final GestorNotificaciones notificaciones;

    public GestorReservas(Mapa mapa, ConfiguracionPlazos configuracion, GestorNotificaciones notificaciones) {
        this.mapa = mapa;
        this.configuracion = configuracion;
        this.notificaciones = notificaciones;
    }

    public Reserva reservar(int idStand, Editorial editorial, LocalDate hoy) {
        Stand stand = obtenerStand(idStand);
        if (stand.getEstado() != EstadoStand.DISPONIBLE) {
            throw new IllegalStateException("El stand " + idStand + " no esta DISPONIBLE (estado actual: " + stand.getEstado() + ")");
        }

        LocalDate limiteReserva = hoy.plusDays(configuracion.getDiasPlazoReserva());
        LocalDate limiteProntoPago = limiteReserva.plusDays(configuracion.getDiasPlazoProntoPago());
        LocalDate limitePagoTotal = limiteReserva.plusDays(configuracion.getDiasPlazoPagoTotal());

        Reserva reserva = new Reserva(stand, editorial, hoy, limiteReserva, limiteProntoPago, limitePagoTotal);
        stand.setReserva(reserva);
        cambiarEstado(stand, EstadoStand.RESERVADO, editorial, hoy,
                "Reserva creada, plazo de pago hasta " + limiteReserva);
        reservas.add(reserva);
        return reserva;
    }

    public void bloquear(int idStand, LocalDate hoy) {
        Stand stand = obtenerStand(idStand);
        if (stand.getEstado() != EstadoStand.DISPONIBLE) {
            throw new IllegalStateException("Solo se puede bloquear un stand DISPONIBLE");
        }
        cambiarEstado(stand, EstadoStand.BLOQUEADO, null, hoy, "Stand bloqueado por el administrador");
    }

    /** Transferencia: envia un stand bloqueado directamente a una editorial (pasa a RESERVADO). */
    public Reserva transferir(int idStand, Editorial editorial, LocalDate hoy) {
        Stand stand = obtenerStand(idStand);
        if (stand.getEstado() != EstadoStand.BLOQUEADO) {
            throw new IllegalStateException("Solo se puede transferir un stand BLOQUEADO");
        }
        LocalDate limiteReserva = hoy.plusDays(configuracion.getDiasPlazoReserva());
        LocalDate limiteProntoPago = limiteReserva.plusDays(configuracion.getDiasPlazoProntoPago());
        LocalDate limitePagoTotal = limiteReserva.plusDays(configuracion.getDiasPlazoPagoTotal());

        Reserva reserva = new Reserva(stand, editorial, hoy, limiteReserva, limiteProntoPago, limitePagoTotal);
        stand.setReserva(reserva);
        cambiarEstado(stand, EstadoStand.RESERVADO, editorial, hoy,
                "Stand transferido por el administrador, plazo de pago hasta " + limiteReserva);
        reservas.add(reserva);
        return reserva;
    }

    /** Pago de anticipo (15%): pasa el stand de RESERVADO a OCUPADO. */
    public void pagarAnticipo(int idStand, double monto, TipoPago tipo, LocalDate hoy) {
        Stand stand = obtenerStand(idStand);
        if (stand.getEstado() != EstadoStand.RESERVADO) {
            throw new IllegalStateException("Solo se puede pagar anticipo de un stand RESERVADO");
        }
        Reserva reserva = stand.getReserva();
        double costoTotal = stand.calcularCosto();
        double minimoAnticipo = costoTotal * 0.15;
        if (monto < minimoAnticipo) {
            throw new IllegalArgumentException(String.format(
                    "El anticipo minimo es %.2f (15%% de %.2f)", minimoAnticipo, costoTotal));
        }
        reserva.registrarPago(new Pago(monto, hoy, tipo));
        cambiarEstado(stand, EstadoStand.OCUPADO, reserva.getEditorial(), hoy,
                "Anticipo del 15% recibido, pago total antes de " + reserva.getFechaLimitePagoTotal());
    }

    /** Pago total: aplica 10% de descuento si se paga antes de la fecha de pronto pago. */
    public void pagarTotal(int idStand, TipoPago tipo, LocalDate hoy) {
        Stand stand = obtenerStand(idStand);
        if (stand.getEstado() != EstadoStand.OCUPADO) {
            throw new IllegalStateException("Solo se puede pagar el total de un stand OCUPADO");
        }
        Reserva reserva = stand.getReserva();
        double saldoPendiente = stand.calcularCosto() - reserva.totalPagado();
        double montoAPagar = reserva.aplicarDescuentoProntoPago(saldoPendiente, hoy);
        reserva.registrarPago(new Pago(montoAPagar, hoy, tipo));
        reserva.setConfirmada(true);

        String mensaje = montoAPagar < saldoPendiente
                ? "Pago total confirmado con 10% de descuento por pronto pago"
                : "Pago total confirmado";
        emitirEvento(TipoEvento.CAMBIO_ESTADO, stand.getId(), reserva.getEditorial(), hoy, mensaje);
    }

    /** RF01/RF02/RF03: recorre todas las reservas activas y notifica vencimientos proximos o efectivos. */
    public void chequearVencimientos(LocalDate hoy) {
        for (Reserva reserva : reservas) {
            Stand stand = reserva.getStand();
            Editorial editorial = reserva.getEditorial();

            if (stand.getEstado() == EstadoStand.RESERVADO) {
                if (reserva.reservaVencida(hoy)) {
                    cambiarEstado(stand, EstadoStand.DISPONIBLE, editorial, hoy,
                            "La reserva vencio sin pago de anticipo, el stand vuelve a estar DISPONIBLE");
                    stand.setReserva(null);
                } else if (!reserva.getFechaLimiteReserva().isAfter(hoy.plusDays(configuracion.getDiasAvisoPrevio()))) {
                    emitirEvento(TipoEvento.VENCIMIENTO_RESERVA, stand.getId(), editorial, hoy,
                            "La reserva vence el " + reserva.getFechaLimiteReserva());
                }
            }

            if (stand.getEstado() == EstadoStand.OCUPADO && !reserva.isConfirmada()) {
                if (!reserva.getFechaLimiteProntoPago().isAfter(hoy.plusDays(configuracion.getDiasAvisoPrevio()))
                        && !reserva.prontoPagoVencido(hoy)) {
                    emitirEvento(TipoEvento.VENCIMIENTO_PRONTO_PAGO, stand.getId(), editorial, hoy,
                            "El plazo de pronto pago (10% descuento) vence el " + reserva.getFechaLimiteProntoPago());
                }
                if (!reserva.getFechaLimitePagoTotal().isAfter(hoy.plusDays(configuracion.getDiasAvisoPrevio()))) {
                    emitirEvento(TipoEvento.VENCIMIENTO_PAGO_TOTAL, stand.getId(), editorial, hoy,
                            "La fecha limite de pago total vence el " + reserva.getFechaLimitePagoTotal());
                }
            }
        }
    }

    public Mapa getMapa() {
        return mapa;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    private Stand obtenerStand(int idStand) {
        return mapa.buscarPorId(idStand)
                .orElseThrow(() -> new IllegalArgumentException("No existe el stand " + idStand));
    }

    private void cambiarEstado(Stand stand, EstadoStand nuevoEstado, Editorial editorial, LocalDate hoy, String mensaje) {
        stand.setEstado(nuevoEstado);
        if (editorial != null) {
            emitirEvento(TipoEvento.CAMBIO_ESTADO, stand.getId(), editorial, hoy, mensaje);
        }
    }

    private void emitirEvento(TipoEvento tipo, int idStand, Editorial editorial, LocalDate hoy, String mensaje) {
        notificaciones.emitir(new EventoNotificacion(tipo, idStand, editorial.getNombre(), mensaje, hoy));
    }
}
