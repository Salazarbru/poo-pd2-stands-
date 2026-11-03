package com.filey.datos;

public class Editorial {

    private final int id;
    private final String nombre;
    private final String usuarioLibreria;

    public Editorial(int id, String nombre, String usuarioLibreria) {
        this.id = id;
        this.nombre = nombre;
        this.usuarioLibreria = usuarioLibreria;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuarioLibreria() {
        return usuarioLibreria;
    }

    @Override
    public String toString() {
        return "Editorial{id=" + id + ", nombre='" + nombre + "', usuarioLibreria='" + usuarioLibreria + "'}";
    }
}
