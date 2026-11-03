package com.filey.notificaciones;

/**
 * Punto unico de entrada del paquete de notificaciones: registra los
 * observadores (uno por tipo de evento, RF04) y expone emitir() para
 * que el negocio dispare eventos sin conocer los observadores concretos.
 */
public class GestorNotificaciones {

    private final SujetoNotificaciones sujeto = new SujetoNotificaciones();
    private final ObservadorTrazabilidad trazabilidad = new ObservadorTrazabilidad();

    public GestorNotificaciones() {
        sujeto.suscribir(new ObservadorVencimientoReserva());
        sujeto.suscribir(new ObservadorProntoPago());
        sujeto.suscribir(new ObservadorPagoTotal());
        sujeto.suscribir(new ObservadorCambioEstado());
        sujeto.suscribir(trazabilidad);
    }

    public void emitir(EventoNotificacion evento) {
        sujeto.notificar(evento);
    }

    public ObservadorTrazabilidad getTrazabilidad() {
        return trazabilidad;
    }
}
