package com.filey.notificaciones;

import java.util.ArrayList;
import java.util.List;

/**
 * Sujeto observable: mantiene la lista de observadores suscritos y
 * les propaga los eventos, sin conocer su implementacion concreta.
 */
public class SujetoNotificaciones {

    private final List<Observador> observadores = new ArrayList<>();

    public void suscribir(Observador observador) {
        observadores.add(observador);
    }

    public void desuscribir(Observador observador) {
        observadores.remove(observador);
    }

    public void notificar(EventoNotificacion evento) {
        for (Observador observador : observadores) {
            observador.actualizar(evento);
        }
    }
}
