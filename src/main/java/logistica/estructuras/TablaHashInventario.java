package logistica.estructuras;

import logistica.modelos.Producto;

/**
 * Tabla Hash con manejo de colisiones por encadenamiento (chaining).
 * Permite búsqueda, inserción y eliminación en tiempo promedio O(1).
 */
public class TablaHashInventario {

    private static final int CAPACIDAD_INICIAL = 16;
    private static final double FACTOR_CARGA_MAX = 0.75;

    // Nodo para encadenamiento
    private static class Nodo {
        String clave;
        Producto producto;
        Nodo siguiente;

        Nodo(String clave, Producto producto) {
            this.clave = clave;
            this.producto = producto;
            this.siguiente = null;
        }
    }

    private Nodo[] tabla;
    private int tamanio;
    private int capacidad;

    public TablaHashInventario() {
        this.capacidad = CAPACIDAD_INICIAL;
        this.tabla = new Nodo[capacidad];
        this.tamanio = 0;
    }

    // ── Función de hash ───────────────────────────────────────────────────────

    private int hash(String clave) {
        int hashCode = 0;
        for (char c : clave.toCharArray()) {
            hashCode = (hashCode * 31 + c) % capacidad;
        }
        return Math.abs(hashCode);
    }

    // ── Operaciones principales ───────────────────────────────────────────────

    /**
     * Registra un producto en la tabla hash.
     * Si ya existe, lanza excepción.
     */
    public void registrar(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto no puede ser nulo.");
        String clave = producto.getCodigo();

        if (buscar(clave) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + clave);
        }

        if ((double)(tamanio + 1) / capacidad > FACTOR_CARGA_MAX) {
            rehash();
        }

        int indice = hash(clave);
        Nodo nuevoNodo = new Nodo(clave, producto);

        if (tabla[indice] == null) {
            tabla[indice] = nuevoNodo;
        } else {
            // Encadenamiento: agregar al frente
            nuevoNodo.siguiente = tabla[indice];
            tabla[indice] = nuevoNodo;
        }
        tamanio++;
    }

    /**
     * Busca un producto por su código. Retorna null si no existe.
     */
    public Producto buscar(String codigo) {
        if (codigo == null) return null;
        int indice = hash(codigo.toUpperCase());
        Nodo actual = tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(codigo.toUpperCase())) {
                return actual.producto;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Elimina un producto de la tabla. Retorna true si lo eliminó.
     */
    public boolean eliminar(String codigo) {
        if (codigo == null) return false;
        int indice = hash(codigo.toUpperCase());
        Nodo actual = tabla[indice];
        Nodo anterior = null;

        while (actual != null) {
            if (actual.clave.equals(codigo.toUpperCase())) {
                if (anterior == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Devuelve todos los productos cargados.
     */
    public Producto[] obtenerTodos() {
        Producto[] productos = new Producto[tamanio];
        int idx = 0;
        for (Nodo nodo : tabla) {
            Nodo actual = nodo;
            while (actual != null) {
                productos[idx++] = actual.producto;
                actual = actual.siguiente;
            }
        }
        return productos;
    }

    public int getTamanio() { return tamanio; }

    // ── Rehash ────────────────────────────────────────────────────────────────

    private void rehash() {
        int nuevaCapacidad = capacidad * 2;
        Nodo[] nuevaTabla = new Nodo[nuevaCapacidad];
        int capacidadAnterior = capacidad;
        capacidad = nuevaCapacidad;

        for (int i = 0; i < capacidadAnterior; i++) {
            Nodo actual = tabla[i];
            while (actual != null) {
                int nuevoIndice = hash(actual.clave);
                Nodo siguiente = actual.siguiente;
                actual.siguiente = nuevaTabla[nuevoIndice];
                nuevaTabla[nuevoIndice] = actual;
                actual = siguiente;
            }
        }
        tabla = nuevaTabla;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== Inventario (" + tamanio + " productos) ===\n");
        for (Producto p : obtenerTodos()) {
            sb.append("  ").append(p).append("\n");
        }
        return sb.toString();
    }
}
