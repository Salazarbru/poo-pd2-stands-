package com.filey.notificaciones;

/** RF01: notifica cuando la reserva de un stand esta por vencer. */
public class ObservadorVencimientoReserva implements Observador {

    @Override
    public void actualizar(EventoNotificacion evento) {
        if (evento.getTipo() != TipoEvento.VENCIMIENTO_RESERVA) {
            return;
        }
        System.out.printf("[NOTIFICACION -> %s] Stand %d: %s (%s)%n",
                evento.getNombreEditorial(), evento.getIdStand(), evento.getMensaje(), evento.getFecha());
    }
}
