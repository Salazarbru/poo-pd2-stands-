package com.filey.notificaciones;

/** RF05: notifica cuando el estado de un stand cambia (ej. RESERVADO -> OCUPADO). */
public class ObservadorCambioEstado implements Observador {

    @Override
    public void actualizar(EventoNotificacion evento) {
        if (evento.getTipo() != TipoEvento.CAMBIO_ESTADO) {
            return;
        }
        System.out.printf("[NOTIFICACION -> %s] Stand %d: %s (%s)%n",
                evento.getNombreEditorial(), evento.getIdStand(), evento.getMensaje(), evento.getFecha());
    }
}
