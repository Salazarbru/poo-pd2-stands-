package com.filey.datos;

import java.time.LocalDate;

public class Pago {

    private final double monto;
    private final LocalDate fecha;
    private final TipoPago tipo;

    public Pago(double monto, LocalDate fecha, TipoPago tipo) {
        this.monto = monto;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public TipoPago getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Pago{monto=" + monto + ", fecha=" + fecha + ", tipo=" + tipo + "}";
    }
}
