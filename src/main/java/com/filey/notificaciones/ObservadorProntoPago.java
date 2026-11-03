package com.filey.notificaciones;

/** RF03: notifica cuando el plazo de pronto pago (10% de descuento) esta por vencer. */
public class ObservadorProntoPago implements Observador {

    @Override
    public void actualizar(EventoNotificacion evento) {
        if (evento.getTipo() != TipoEvento.VENCIMIENTO_PRONTO_PAGO) {
            return;
        }
        System.out.printf("[NOTIFICACION -> %s] Stand %d: %s (%s)%n",
                evento.getNombreEditorial(), evento.getIdStand(), evento.getMensaje(), evento.getFecha());
    }
}
