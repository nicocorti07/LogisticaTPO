package logistica.estructuras;

import logistica.modelos.Pedido;

/**
 * Cola FIFO implementada con nodos enlazados.
 * Garantiza que los pedidos se despachen en el orden en que se completaron.
 * Enqueue/Dequeue en O(1).
 */
public class ColaDespacho {

    private static class Nodo {
        Pedido pedido;
        Nodo siguiente;

        Nodo(Pedido pedido) {
            this.pedido = pedido;
            this.siguiente = null;
        }
    }

    private Nodo frente;
    private Nodo fondo;
    private int tamanio;

    public ColaDespacho() {
        this.frente = null;
        this.fondo = null;
        this.tamanio = 0;
    }

    // ── Operaciones principales ───────────────────────────────────────────────

    /**
     * Encola un pedido listo para despacho. O(1).
     */
    public void encolar(Pedido pedido) {
        if (pedido == null) throw new IllegalArgumentException("El pedido no puede ser nulo.");
        Nodo nuevo = new Nodo(pedido);
        if (fondo == null) {
            frente = nuevo;
            fondo = nuevo;
        } else {
            fondo.siguiente = nuevo;
            fondo = nuevo;
        }
        tamanio++;
    }

    /**
     * Desencola el próximo pedido a despachar. O(1).
     */
    public Pedido desencolar() {
        if (estaVacia()) throw new IllegalStateException("La cola de despacho está vacía.");
        Pedido pedido = frente.pedido;
        frente = frente.siguiente;
        if (frente == null) fondo = null;
        tamanio--;
        return pedido;
    }

    /**
     * Consulta el próximo pedido sin retirarlo. O(1).
     */
    public Pedido verFrente() {
        if (estaVacia()) throw new IllegalStateException("La cola de despacho está vacía.");
        return frente.pedido;
    }

    public boolean estaVacia() { return tamanio == 0; }
    public int getTamanio() { return tamanio; }

    /**
     * Muestra todos los pedidos en cola sin modificarla.
     */
    public void mostrarCola() {
        if (estaVacia()) {
            System.out.println("  (Cola de despacho vacía)");
            return;
        }
        System.out.println("=== Cola de Despacho (" + tamanio + " pedidos) ===");
        Nodo actual = frente;
        int posicion = 1;
        while (actual != null) {
            System.out.println("  " + posicion + ". " + actual.pedido);
            actual = actual.siguiente;
            posicion++;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cola de despacho [");
        Nodo actual = frente;
        while (actual != null) {
            sb.append("Pedido#").append(actual.pedido.getId());
            if (actual.siguiente != null) sb.append(" → ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}
