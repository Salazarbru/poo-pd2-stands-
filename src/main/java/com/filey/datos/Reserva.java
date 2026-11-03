package com.filey.datos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {

    private final Stand stand;
    private final Editorial editorial;
    private final LocalDate fechaInicio;
    private final LocalDate fechaLimiteReserva;
    private final LocalDate fechaLimiteProntoPago;
    private final LocalDate fechaLimitePagoTotal;
    private final List<Pago> pagos = new ArrayList<>();
    private boolean confirmada = false;

    public Reserva(Stand stand, Editorial editorial, LocalDate fechaInicio,
                    LocalDate fechaLimiteReserva, LocalDate fechaLimiteProntoPago, LocalDate fechaLimitePagoTotal) {
        this.stand = stand;
        this.editorial = editorial;
        this.fechaInicio = fechaInicio;
        this.fechaLimiteReserva = fechaLimiteReserva;
        this.fechaLimiteProntoPago = fechaLimiteProntoPago;
        this.fechaLimitePagoTotal = fechaLimitePagoTotal;
    }

    public void registrarPago(Pago pago) {
        pagos.add(pago);
    }

    public double totalPagado() {
        return pagos.stream().mapToDouble(Pago::getMonto).sum();
    }

    public double aplicarDescuentoProntoPago(double monto, LocalDate fechaPago) {
        if (fechaPago.isBefore(fechaLimiteProntoPago) || fechaPago.isEqual(fechaLimiteProntoPago)) {
            return monto * 0.90;
        }
        return monto;
    }

    public boolean reservaVencida(LocalDate hoy) {
        return hoy.isAfter(fechaLimiteReserva);
    }

    public boolean prontoPagoVencido(LocalDate hoy) {
        return hoy.isAfter(fechaLimiteProntoPago);
    }

    public boolean pagoTotalVencido(LocalDate hoy) {
        return hoy.isAfter(fechaLimitePagoTotal);
    }

    public Stand getStand() {
        return stand;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaLimiteReserva() {
        return fechaLimiteReserva;
    }

    public LocalDate getFechaLimiteProntoPago() {
        return fechaLimiteProntoPago;
    }

    public LocalDate getFechaLimitePagoTotal() {
        return fechaLimitePagoTotal;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }
}
