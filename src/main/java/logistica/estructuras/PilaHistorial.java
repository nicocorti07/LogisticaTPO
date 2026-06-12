package logistica.estructuras;

import logistica.modelos.Movimiento;

/**
 * Pila LIFO implementada con nodos enlazados.
 * Almacena el historial de movimientos del inventario.
 * Permite consultar y deshacer el último movimiento registrado. O(1).
 */
public class PilaHistorial {

    private static class Nodo {
        Movimiento movimiento;
        Nodo anterior;

        Nodo(Movimiento movimiento) {
            this.movimiento = movimiento;
            this.anterior = null;
        }
    }

    private Nodo tope;
    private int tamanio;

    public PilaHistorial() {
        this.tope = null;
        this.tamanio = 0;
    }

    // ── Operaciones principales ───────────────────────────────────────────────

    /**
     * Apila un movimiento. O(1).
     */
    public void apilar(Movimiento movimiento) {
        if (movimiento == null) throw new IllegalArgumentException("El movimiento no puede ser nulo.");
        Nodo nuevo = new Nodo(movimiento);
        nuevo.anterior = tope;
        tope = nuevo;
        tamanio++;
    }

    /**
     * Desapila y retorna el último movimiento registrado. O(1).
     */
    public Movimiento desapilar() {
        if (estaVacia()) throw new IllegalStateException("El historial está vacío.");
        Movimiento movimiento = tope.movimiento;
        tope = tope.anterior;
        tamanio--;
        return movimiento;
    }

    /**
     * Consulta el último movimiento sin desapilarlo. O(1).
     */
    public Movimiento verTope() {
        if (estaVacia()) throw new IllegalStateException("El historial está vacío.");
        return tope.movimiento;
    }

    public boolean estaVacia() { return tamanio == 0; }
    public int getTamanio() { return tamanio; }

    /**
     * Muestra el historial completo (del más reciente al más antiguo).
     */
    public void mostrarHistorial() {
        if (estaVacia()) {
            System.out.println("  (Sin movimientos registrados)");
            return;
        }
        System.out.println("=== Historial de Movimientos (" + tamanio + " registros) ===");
        Nodo actual = tope;
        int nro = 1;
        while (actual != null) {
            System.out.println("  " + nro + ". " + actual.movimiento);
            actual = actual.anterior;
            nro++;
        }
    }

    @Override
    public String toString() {
        return "PilaHistorial [" + tamanio + " movimientos registrados]";
    }
}
