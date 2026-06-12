package logistica.menu;

import logistica.servicios.SistemaLogistico;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase Main con menú interactivo para demostrar el funcionamiento del sistema.
 * Programación II - UADE | Centro Logístico de Distribución Avanzada
 * Grupo: Anzoategui Ignacio, Cortiñas Nicolas, Ponce Octavio
 */
public class Main {

    private static SistemaLogistico sistema = new SistemaLogistico();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     CENTRO LOGÍSTICO DE DISTRIBUCIÓN AVANZADA        ║");
        System.out.println("║     Programación II - UADE                           ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        cargarDatosDemostracion();

        boolean ejecutando = true;
        while (ejecutando) {
            mostrarMenu();
            int opcion = leerEntero("Ingrese opción: ");
            ejecutando = procesarOpcion(opcion);
        }

        System.out.println("\n✓ Sistema cerrado. ¡Hasta luego!");
        scanner.close();
    }

    // ── Menú ──────────────────────────────────────────────────────────────────

    private static void mostrarMenu() {
        System.out.println("\n══════════════════════════════════════════════════════");
        System.out.println("  MENÚ PRINCIPAL");
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("  --- INVENTARIO (Tabla Hash) ---");
        System.out.println("  1. Registrar producto");
        System.out.println("  2. Buscar producto por código");
        System.out.println("  3. Actualizar stock (entrada de mercadería)");
        System.out.println("  4. Actualizar stock (salida de mercadería)");
        System.out.println("  5. Listar todo el inventario");
        System.out.println("  --- STOCK CRÍTICO (Heap Mínimo) ---");
        System.out.println("  6. Ver producto con menor stock");
        System.out.println("  7. Ver top 3 productos con stock crítico");
        System.out.println("  --- PEDIDOS Y DESPACHO (Cola FIFO) ---");
        System.out.println("  8. Registrar nuevo pedido");
        System.out.println("  9. Despachar siguiente pedido");
        System.out.println(" 10. Ver cola de pedidos pendientes");
        System.out.println("  --- HISTORIAL (Pila LIFO) ---");
        System.out.println(" 11. Ver historial completo de movimientos");
        System.out.println(" 12. Ver último movimiento registrado");
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("  0. Salir");
        System.out.println("══════════════════════════════════════════════════════");
    }

    private static boolean procesarOpcion(int opcion) {
        System.out.println();
        switch (opcion) {
            case 1  -> registrarProducto();
            case 2  -> buscarProducto();
            case 3  -> actualizarStockEntrada();
            case 4  -> actualizarStockSalida();
            case 5  -> sistema.listarInventario();
            case 6  -> sistema.mostrarProductoConMenorStock();
            case 7  -> sistema.mostrarProductosCriticos(3);
            case 8  -> registrarPedido();
            case 9  -> sistema.despacharSiguiente();
            case 10 -> sistema.mostrarColaPedidos();
            case 11 -> sistema.mostrarHistorial();
            case 12 -> sistema.mostrarUltimoMovimiento();
            case 0  -> { return false; }
            default -> System.out.println("✗ Opción inválida. Intente nuevamente.");
        }
        return true;
    }

    // ── Acciones del menú ────────────────────────────────────────────────────

    private static void registrarProducto() {
        System.out.println("--- Registrar Producto ---");
        String codigo    = leerTexto("Código del producto: ");
        String nombre    = leerTexto("Nombre: ");
        int stock        = leerEntero("Stock inicial: ");
        int stockMin     = leerEntero("Stock mínimo: ");
        String ubicacion = leerTexto("Ubicación en almacén (ej: A1, B3): ");

        try {
            sistema.registrarProducto(codigo, nombre, stock, stockMin, ubicacion);
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void buscarProducto() {
        String codigo = leerTexto("Código del producto a buscar: ");
        sistema.buscarProducto(codigo);
    }

    private static void actualizarStockEntrada() {
        String codigo  = leerTexto("Código del producto: ");
        int cantidad   = leerEntero("Cantidad a ingresar: ");
        sistema.actualizarStock(codigo, cantidad, true);
    }

    private static void actualizarStockSalida() {
        String codigo  = leerTexto("Código del producto: ");
        int cantidad   = leerEntero("Cantidad a retirar: ");
        sistema.actualizarStock(codigo, cantidad, false);
    }

    private static void registrarPedido() {
        System.out.println("--- Registrar Pedido ---");
        String cliente = leerTexto("Nombre del cliente: ");
        int cantProd   = leerEntero("¿Cuántos productos incluye el pedido? ");
        List<String> codigos = new ArrayList<>();
        for (int i = 1; i <= cantProd; i++) {
            codigos.add(leerTexto("  Código producto " + i + ": "));
        }
        sistema.registrarPedido(cliente, codigos);
    }

    // ── Utilidades de lectura ─────────────────────────────────────────────────

    private static String leerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor < 0) {
                    System.out.println("✗ Ingrese un número mayor o igual a 0.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("✗ Ingrese un número válido.");
            }
        }
    }

    // ── Datos de demostración ─────────────────────────────────────────────────

    private static void cargarDatosDemostracion() {
        System.out.println("\n[Cargando datos de demostración...]\n");

        sistema.registrarProducto("P001", "Caja de cartón grande",     45,  10, "A1");
        sistema.registrarProducto("P002", "Film estirable",             8,  15, "A2");
        sistema.registrarProducto("P003", "Pallet de madera",          30,   5, "B1");
        sistema.registrarProducto("P004", "Cinta adhesiva industrial",  3,  10, "B2");
        sistema.registrarProducto("P005", "Bolsa de burbujas",         60,  20, "C1");
        sistema.registrarProducto("P006", "Esquinero de cartón",        2,   5, "C2");

        System.out.println("\n[Datos cargados. P002, P004 y P006 tienen stock crítico.]\n");
    }
}
