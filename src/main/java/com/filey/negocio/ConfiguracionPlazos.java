package com.filey.negocio;

/** RFN03: los plazos son configurables, no hardcodeados en la logica de negocio. */
public class ConfiguracionPlazos {

    private int diasPlazoReserva = 30;
    private int diasPlazoProntoPago = 15;
    private int diasPlazoPagoTotal = 45;
    private int diasAvisoPrevio = 5;

    public int getDiasPlazoReserva() {
        return diasPlazoReserva;
    }

    public void setDiasPlazoReserva(int diasPlazoReserva) {
        this.diasPlazoReserva = diasPlazoReserva;
    }

    public int getDiasPlazoProntoPago() {
        return diasPlazoProntoPago;
    }

    public void setDiasPlazoProntoPago(int diasPlazoProntoPago) {
        this.diasPlazoProntoPago = diasPlazoProntoPago;
    }

    public int getDiasPlazoPagoTotal() {
        return diasPlazoPagoTotal;
    }

    public void setDiasPlazoPagoTotal(int diasPlazoPagoTotal) {
        this.diasPlazoPagoTotal = diasPlazoPagoTotal;
    }

    public int getDiasAvisoPrevio() {
        return diasAvisoPrevio;
    }

    public void setDiasAvisoPrevio(int diasAvisoPrevio) {
        this.diasAvisoPrevio = diasAvisoPrevio;
    }
}
