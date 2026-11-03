package com.filey.notificaciones;

import java.time.LocalDate;

/**
 * DTO desacoplado de las clases de datos: solo lleva la info minima
 * necesaria para que un observador redacte la notificacion (RFN02).
 */
public class EventoNotificacion {

    private final TipoEvento tipo;
    private final int idStand;
    private final String nombreEditorial;
    private final String mensaje;
    private final LocalDate fecha;

    public EventoNotificacion(TipoEvento tipo, int idStand, String nombreEditorial, String mensaje, LocalDate fecha) {
        this.tipo = tipo;
        this.idStand = idStand;
        this.nombreEditorial = nombreEditorial;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public int getIdStand() {
        return idStand;
    }

    public String getNombreEditorial() {
        return nombreEditorial;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
