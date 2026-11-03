package com.filey.cli;

import com.filey.datos.*;
import com.filey.negocio.ConfiguracionPlazos;
import com.filey.negocio.GestorReservas;
import com.filey.notificaciones.GestorNotificaciones;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static LocalDate hoy = LocalDate.now();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Mapa mapa = new Mapa();
        List<Editorial> editoriales = datosDeEjemplo(mapa);
        ConfiguracionPlazos configuracion = new ConfiguracionPlazos();
        GestorNotificaciones notificaciones = new GestorNotificaciones();
        GestorReservas gestor = new GestorReservas(mapa, configuracion, notificaciones);

        System.out.println("=== Sistema de venta de stands - FILEY ===");
        System.out.println("Fecha del sistema (simulada): " + hoy);
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();
            try {
                switch (opcion) {
                    case "1" -> listarMapa(mapa);
                    case "2" -> listarPorEstado(mapa);
                    case "3" -> reservar(gestor, editoriales);
                    case "4" -> bloquear(gestor);
                    case "5" -> transferir(gestor, editoriales);
                    case "6" -> pagarAnticipo(gestor);
                    case "7" -> pagarTotal(gestor);
                    case "8" -> gestor.chequearVencimientos(hoy);
                    case "9" -> avanzarFecha();
                    case "10" -> verHistorial(notificaciones);
                    case "11" -> listarEditoriales(editoriales);
                    case "0" -> salir = true;
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        System.out.println("Hasta pronto.");
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENU (fecha actual: " + hoy + ") ---");
        System.out.println(" 1. Listar mapa completo");
        System.out.println(" 2. Listar stands por estado");
        System.out.println(" 3. Reservar stand (editorial)");
        System.out.println(" 4. Bloquear stand (admin)");
        System.out.println(" 5. Transferir stand bloqueado a editorial");
        System.out.println(" 6. Pagar anticipo (15%)");
        System.out.println(" 7. Pagar total (con descuento si aplica)");
        System.out.println(" 8. Chequear vencimientos (dispara notificaciones)");
        System.out.println(" 9. Avanzar la fecha del sistema (simular dias)");
        System.out.println("10. Ver historial de notificaciones (trazabilidad)");
        System.out.println("11. Listar editoriales");
        System.out.println(" 0. Salir");
        System.out.print("Opcion: ");
    }

    private static List<Editorial> datosDeEjemplo(Mapa mapa) {
        mapa.agregarStand(new Stand(1, 20, 50));
        mapa.agregarStand(new Stand(2, 15, 60));
        mapa.agregarStand(new Stand(3, 30, 45));
        mapa.agregarStand(new Stand(4, 10, 70));

        return List.of(
                new Editorial(1, "Editorial Planeta", "planeta_lib"),
                new Editorial(2, "Editorial Alfaguara", "alfaguara_lib"),
                new Editorial(3, "Editorial Santillana", "santillana_lib")
        );
    }

    private static void listarMapa(Mapa mapa) {
        System.out.println("Mapa del salon:");
        mapa.listarTodos().forEach(System.out::println);
    }

    private static void listarPorEstado(Mapa mapa) {
        EstadoStand estado = pedirEstado();
        List<Stand> resultado = mapa.listarPorEstado(estado);
        if (resultado.isEmpty()) {
            System.out.println("No hay stands en estado " + estado);
        }
        resultado.forEach(System.out::println);
    }

    private static void reservar(GestorReservas gestor, List<Editorial> editoriales) {
        int idStand = pedirEntero("ID del stand a reservar: ");
        Editorial editorial = pedirEditorial(editoriales);
        gestor.reservar(idStand, editorial, hoy);
        System.out.println("Stand " + idStand + " reservado para " + editorial.getNombre());
    }

    private static void bloquear(GestorReservas gestor) {
        int idStand = pedirEntero("ID del stand a bloquear: ");
        gestor.bloquear(idStand, hoy);
        System.out.println("Stand " + idStand + " bloqueado.");
    }

    private static void transferir(GestorReservas gestor, List<Editorial> editoriales) {
        int idStand = pedirEntero("ID del stand bloqueado a transferir: ");
        Editorial editorial = pedirEditorial(editoriales);
        gestor.transferir(idStand, editorial, hoy);
        System.out.println("Stand " + idStand + " transferido a " + editorial.getNombre());
    }

    private static void pagarAnticipo(GestorReservas gestor) {
        int idStand = pedirEntero("ID del stand: ");
        double monto = pedirDouble("Monto del anticipo: ");
        TipoPago tipo = pedirTipoPago();
        gestor.pagarAnticipo(idStand, monto, tipo, hoy);
        System.out.println("Anticipo registrado. Stand " + idStand + " ahora OCUPADO.");
    }

    private static void pagarTotal(GestorReservas gestor) {
        int idStand = pedirEntero("ID del stand: ");
        TipoPago tipo = pedirTipoPago();
        gestor.pagarTotal(idStand, tipo, hoy);
        System.out.println("Pago total registrado. Stand " + idStand + " confirmado.");
    }

    private static void avanzarFecha() {
        int dias = pedirEntero("Cuantos dias avanzar: ");
        hoy = hoy.plusDays(dias);
        System.out.println("Nueva fecha del sistema: " + hoy);
    }

    private static void verHistorial(GestorNotificaciones notificaciones) {
        List<String> historial = notificaciones.getTrazabilidad().getHistorial();
        if (historial.isEmpty()) {
            System.out.println("Aun no se ha emitido ninguna notificacion.");
            return;
        }
        historial.forEach(System.out::println);
    }

    private static void listarEditoriales(List<Editorial> editoriales) {
        editoriales.forEach(System.out::println);
    }

    private static Editorial pedirEditorial(List<Editorial> editoriales) {
        System.out.println("Editoriales disponibles:");
        editoriales.forEach(System.out::println);
        int id = pedirEntero("ID de la editorial: ");
        return editoriales.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe la editorial " + id));
    }

    private static EstadoStand pedirEstado() {
        System.out.println("Estados: 1=DISPONIBLE 2=RESERVADO 3=OCUPADO 4=BLOQUEADO");
        int opcion = pedirEntero("Opcion: ");
        return switch (opcion) {
            case 1 -> EstadoStand.DISPONIBLE;
            case 2 -> EstadoStand.RESERVADO;
            case 3 -> EstadoStand.OCUPADO;
            case 4 -> EstadoStand.BLOQUEADO;
            default -> throw new IllegalArgumentException("Opcion de estado invalida");
        };
    }

    private static TipoPago pedirTipoPago() {
        System.out.println("Tipo de pago: 1=TRANSFERENCIA 2=ELECTRONICO");
        int opcion = pedirEntero("Opcion: ");
        return switch (opcion) {
            case 1 -> TipoPago.TRANSFERENCIA;
            case 2 -> TipoPago.ELECTRONICO;
            default -> throw new IllegalArgumentException("Opcion de pago invalida");
        };
    }

    private static int pedirEntero(String etiqueta) {
        System.out.print(etiqueta);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static double pedirDouble(String etiqueta) {
        System.out.print(etiqueta);
        return Double.parseDouble(scanner.nextLine().trim());
    }
}
