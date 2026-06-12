package logistica.estructuras;

import logistica.modelos.Producto;

/**
 * Heap Mínimo (Min-Heap) que organiza productos por nivel de stock.
 * El producto con menor stock siempre queda en la raíz → O(log n) inserción/extracción.
 */
public class HeapMinStock {

    private static final int CAPACIDAD_INICIAL = 20;

    private Producto[] heap;
    private int tamanio;

    public HeapMinStock() {
        this.heap = new Producto[CAPACIDAD_INICIAL];
        this.tamanio = 0;
    }

    // ── Helpers de índices ────────────────────────────────────────────────────

    private int padre(int i)    { return (i - 1) / 2; }
    private int hijoIzq(int i)  { return 2 * i + 1; }
    private int hijoDer(int i)  { return 2 * i + 2; }

    private void intercambiar(int i, int j) {
        Producto tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    // ── Operaciones principales ───────────────────────────────────────────────

    /**
     * Inserta un producto en el heap. O(log n).
     */
    public void insertar(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("Producto nulo.");
        if (tamanio == heap.length) expandir();

        heap[tamanio] = producto;
        tamanio++;
        subirUltimo();
    }

    /**
     * Extrae el producto con menor stock. O(log n).
     */
    public Producto extraerMinimo() {
        if (tamanio == 0) throw new IllegalStateException("El heap está vacío.");
        Producto minimo = heap[0];
        heap[0] = heap[tamanio - 1];
        heap[tamanio - 1] = null;
        tamanio--;
        if (tamanio > 0) bajarRaiz();
        return minimo;
    }

    /**
     * Consulta el producto con menor stock sin extraerlo. O(1).
     */
    public Producto verMinimo() {
        if (tamanio == 0) throw new IllegalStateException("El heap está vacío.");
        return heap[0];
    }

    /**
     * Reconstruye el heap completo desde un arreglo de productos.
     * Útil para sincronizar con la tabla hash. O(n).
     */
    public void reconstruir(Producto[] productos) {
        tamanio = 0;
        if (productos.length > heap.length) {
            heap = new Producto[productos.length * 2];
        }
        for (Producto p : productos) {
            if (p != null) {
                heap[tamanio++] = p;
            }
        }
        // Heapify desde abajo hacia arriba
        for (int i = tamanio / 2 - 1; i >= 0; i--) {
            bajarDesde(i);
        }
    }

    public boolean estaVacio() { return tamanio == 0; }
    public int getTamanio() { return tamanio; }

    // ── Sift Up / Sift Down ───────────────────────────────────────────────────

    private void subirUltimo() {
        int i = tamanio - 1;
        while (i > 0 && heap[i].getStock() < heap[padre(i)].getStock()) {
            intercambiar(i, padre(i));
            i = padre(i);
        }
    }

    private void bajarRaiz() {
        bajarDesde(0);
    }

    private void bajarDesde(int i) {
        int minIdx = i;
        int izq = hijoIzq(i);
        int der = hijoDer(i);

        if (izq < tamanio && heap[izq].getStock() < heap[minIdx].getStock()) minIdx = izq;
        if (der < tamanio && heap[der].getStock() < heap[minIdx].getStock()) minIdx = der;

        if (minIdx != i) {
            intercambiar(i, minIdx);
            bajarDesde(minIdx);
        }
    }

    private void expandir() {
        Producto[] nuevo = new Producto[heap.length * 2];
        System.arraycopy(heap, 0, nuevo, 0, tamanio);
        heap = nuevo;
    }

    /**
     * Lista los N productos con menor stock sin modificar el heap.
     */
    public void mostrarTopCriticos(int n) {
        System.out.println("=== Top " + n + " productos con menor stock ===");
        // Copiamos el heap para no destruirlo
        HeapMinStock copia = new HeapMinStock();
        copia.reconstruir(heap);
        int mostrados = 0;
        while (!copia.estaVacio() && mostrados < n) {
            Producto p = copia.extraerMinimo();
            System.out.println("  " + p);
            mostrados++;
        }
    }

    @Override
    public String toString() {
        if (tamanio == 0) return "Heap vacío.";
        StringBuilder sb = new StringBuilder("=== Heap de Stock (raíz = mínimo) ===\n");
        for (int i = 0; i < tamanio; i++) {
            sb.append("  [").append(i).append("] ").append(heap[i]).append("\n");
        }
        return sb.toString();
    }
}
