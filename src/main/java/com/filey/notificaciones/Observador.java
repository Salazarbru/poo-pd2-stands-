package com.filey.notificaciones;

/** Interfaz del patron Observer (RF04): un observador reacciona a un evento. */
public interface Observador {
    void actualizar(EventoNotificacion evento);
}
