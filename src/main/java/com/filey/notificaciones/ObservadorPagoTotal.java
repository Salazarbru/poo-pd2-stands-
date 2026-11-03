package com.filey.notificaciones;

/** RF02: notifica cuando la fecha limite de pago total esta por vencer. */
public class ObservadorPagoTotal implements Observador {

    @Override
    public void actualizar(EventoNotificacion evento) {
        if (evento.getTipo() != TipoEvento.VENCIMIENTO_PAGO_TOTAL) {
            return;
        }
        System.out.printf("[NOTIFICACION -> %s] Stand %d: %s (%s)%n",
                evento.getNombreEditorial(), evento.getIdStand(), evento.getMensaje(), evento.getFecha());
    }
}
