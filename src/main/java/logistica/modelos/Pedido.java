package logistica.modelos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un pedido compuesto por uno o más productos.
 */
public class Pedido {

    private static int contadorId = 1;

    private int id;
    private String cliente;
    private List<String> codigosProductos; // códigos de los productos pedidos
    private LocalDateTime fechaCreacion;
    private EstadoPedido estado;

    public enum EstadoPedido {
        PENDIENTE, EN_PREPARACION, LISTO_PARA_DESPACHO, DESPACHADO
    }

    public Pedido(String cliente, List<String> codigosProductos) {
        if (cliente == null || cliente.isBlank()) throw new IllegalArgumentException("El cliente no puede estar vacío.");
        if (codigosProductos == null || codigosProductos.isEmpty()) throw new IllegalArgumentException("El pedido debe contener al menos un producto.");

        this.id = contadorId++;
        this.cliente = cliente;
        this.codigosProductos = new ArrayList<>(codigosProductos);
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public List<String> getCodigosProductos() { return codigosProductos; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public EstadoPedido getEstado() { return estado; }

    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Pedido #%d | Cliente: %s | Productos: %s | Estado: %s | Fecha: %s",
                id, cliente, codigosProductos, estado, fechaCreacion.format(fmt));
    }
}
