package com.filey.datos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Mapa {

    private final List<Stand> stands = new ArrayList<>();

    public void agregarStand(Stand stand) {
        stands.add(stand);
    }

    public List<Stand> listarDisponibles() {
        return listarPorEstado(EstadoStand.DISPONIBLE);
    }

    public List<Stand> listarPorEstado(EstadoStand estado) {
        List<Stand> resultado = new ArrayList<>();
        for (Stand stand : stands) {
            if (stand.getEstado() == estado) {
                resultado.add(stand);
            }
        }
        return resultado;
    }

    public List<Stand> listarTodos() {
        return stands;
    }

    public Optional<Stand> buscarPorId(int id) {
        return stands.stream().filter(s -> s.getId() == id).findFirst();
    }
}
