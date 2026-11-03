package com.filey.notificaciones;

import java.util.ArrayList;
import java.util.List;

/** RFN04: registra todas las notificaciones emitidas para poder consultarlas luego. */
public class ObservadorTrazabilidad implements Observador {

    private final List<String> historial = new ArrayList<>();

    @Override
    public void actualizar(EventoNotificacion evento) {
        String registro = String.format("[%s] %s | stand=%d | editorial=%s | %s",
                evento.getFecha(), evento.getTipo(), evento.getIdStand(), evento.getNombreEditorial(), evento.getMensaje());
        historial.add(registro);
    }

    public List<String> getHistorial() {
        return historial;
    }
}
