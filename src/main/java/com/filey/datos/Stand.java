package com.filey.datos;

public class Stand {

    private final int id;
    private final double metrosCuadrados;
    private final double precioM2;
    private EstadoStand estado;
    private Reserva reserva;

    public Stand(int id, double metrosCuadrados, double precioM2) {
        this.id = id;
        this.metrosCuadrados = metrosCuadrados;
        this.precioM2 = precioM2;
        this.estado = EstadoStand.DISPONIBLE;
    }

    public double calcularCosto() {
        return metrosCuadrados * precioM2;
    }

    public int getId() {
        return id;
    }

    public double getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public double getPrecioM2() {
        return precioM2;
    }

    public EstadoStand getEstado() {
        return estado;
    }

    public void setEstado(EstadoStand estado) {
        this.estado = estado;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    @Override
    public String toString() {
        return String.format("Stand #%d [%.1fm2, $%.2f/m2, costo=$%.2f] -> %s",
                id, metrosCuadrados, precioM2, calcularCosto(), estado);
    }
}
