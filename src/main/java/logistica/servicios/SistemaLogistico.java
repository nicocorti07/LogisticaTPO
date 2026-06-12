package logistica.servicios;

import logistica.estructuras.*;
import logistica.modelos.*;

import java.util.List;

/**
 * Servicio central del sistema logístico.
 * Coordina la TablaHash, HeapMin, Cola y Pila para todas las operaciones.
 */
public class SistemaLogistico {

    private TablaHashInventario inventario;
    private HeapMinStock heapStock;
    private ColaDespacho colaDespacho;
    private PilaHistorial historial;

    public SistemaLogistico() {
        this.inventario    = new TablaHashInventario();
        this.heapStock     = new HeapMinStock();
        this.colaDespacho  = new ColaDespacho();
        this.historial     = new PilaHistorial();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 1. GESTIÓN DE INVENTARIO (Tabla Hash)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Registra un nuevo producto en el inventario.
     */
    public void registrarProducto(String codigo, String nombre, int stock, int stockMinimo, String ubicacion) {
        Producto producto = new Producto(codigo, nombre, stock, stockMinimo, ubicacion);
        inventario.registrar(producto);

        historial.apilar(new Movimiento(codigo, Movimiento.TipoMovimiento.REGISTRO, stock,
                "Producto registrado: " + nombre + " en ubicación " + ubicacion));

        sincronizarHeap();
        System.out.println("✓ Producto registrado correctamente: " + producto);
    }

    /**
     * Busca un producto por código. Muestra resultado en consola.
     */
    public Producto buscarProducto(String codigo) {
        Producto p = inventario.buscar(codigo);
        if (p == null) {
            System.out.println("✗ No se encontró ningún producto con código: " + codigo);
        } else {
            System.out.println("✓ Producto encontrado: " + p);
        }
        return p;
    }

    /**
     * Actualiza el stock de un producto (entrada o salida de mercadería).
     */
    public void actualizarStock(String codigo, int cantidad, boolean esEntrada) {
        Producto p = inventario.buscar(codigo);
        if (p == null) {
            System.out.println("✗ Producto no encontrado: " + codigo);
            return;
        }

        int stockAnterior = p.getStock();

        if (esEntrada) {
            p.setStock(stockAnterior + cantidad);
            historial.apilar(new Movimiento(codigo, Movimiento.TipoMovimiento.ENTRADA, cantidad,
                    "Entrada de mercadería. Stock: " + stockAnterior + " → " + p.getStock()));
            System.out.println("✓ Entrada registrada. Stock actualizado: " + p);
        } else {
            if (cantidad > stockAnterior) {
                System.out.println("✗ Stock insuficiente. Stock actual: " + stockAnterior + " | Solicitado: " + cantidad);
                return;
            }
            p.setStock(stockAnterior - cantidad);
            historial.apilar(new Movimiento(codigo, Movimiento.TipoMovimiento.SALIDA, cantidad,
                    "Salida de mercadería. Stock: " + stockAnterior + " → " + p.getStock()));
            System.out.println("✓ Salida registrada. Stock actualizado: " + p);
        }

        sincronizarHeap();
    }

    /**
     * Lista todos los productos del inventario.
     */
    public void listarInventario() {
        if (inventario.getTamanio() == 0) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println(inventario);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 2. STOCK CRÍTICO (Heap Mínimo)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Muestra los N productos con menor stock actual.
     */
    public void mostrarProductosCriticos(int n) {
        if (heapStock.estaVacio()) {
            System.out.println("No hay productos cargados en el sistema.");
            return;
        }
        heapStock.mostrarTopCriticos(n);
    }

    /**
     * Muestra el producto con el stock más bajo de todo el almacén.
     */
    public void mostrarProductoConMenorStock() {
        if (heapStock.estaVacio()) {
            System.out.println("No hay productos en el sistema.");
            return;
        }
        System.out.println("Producto con menor stock: " + heapStock.verMinimo());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 3. PEDIDOS Y DESPACHO (Cola FIFO)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Registra un nuevo pedido y lo encola para despacho.
     */
    public Pedido registrarPedido(String cliente, List<String> codigosProductos) {
        // Validar que todos los productos existan
        for (String cod : codigosProductos) {
            if (inventario.buscar(cod) == null) {
                System.out.println("✗ Producto no encontrado en inventario: " + cod);
                return null;
            }
        }

        Pedido pedido = new Pedido(cliente, codigosProductos);
        pedido.setEstado(Pedido.EstadoPedido.LISTO_PARA_DESPACHO);
        colaDespacho.encolar(pedido);

        historial.apilar(new Movimiento("PEDIDO#" + pedido.getId(),
                Movimiento.TipoMovimiento.SALIDA, codigosProductos.size(),
                "Pedido registrado para cliente: " + cliente));

        System.out.println("✓ Pedido registrado y encolado: " + pedido);
        return pedido;
    }

    /**
     * Despacha el siguiente pedido de la cola.
     */
    public Pedido despacharSiguiente() {
        if (colaDespacho.estaVacia()) {
            System.out.println("✗ No hay pedidos pendientes de despacho.");
            return null;
        }

        Pedido pedido = colaDespacho.desencolar();
        pedido.setEstado(Pedido.EstadoPedido.DESPACHADO);

        historial.apilar(new Movimiento("PEDIDO#" + pedido.getId(),
                Movimiento.TipoMovimiento.SALIDA, 0,
                "Pedido despachado al cliente: " + pedido.getCliente()));

        System.out.println("✓ Pedido despachado: " + pedido);
        return pedido;
    }

    /**
     * Muestra todos los pedidos en cola sin modificarla.
     */
    public void mostrarColaPedidos() {
        colaDespacho.mostrarCola();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 4. HISTORIAL (Pila LIFO)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Muestra el historial completo de movimientos.
     */
    public void mostrarHistorial() {
        historial.mostrarHistorial();
    }

    /**
     * Muestra el último movimiento registrado.
     */
    public void mostrarUltimoMovimiento() {
        if (historial.estaVacia()) {
            System.out.println("Sin movimientos registrados.");
            return;
        }
        System.out.println("Último movimiento: " + historial.verTope());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Utilidades internas
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Sincroniza el heap con el estado actual del inventario.
     */
    private void sincronizarHeap() {
        heapStock.reconstruir(inventario.obtenerTodos());
    }
}
