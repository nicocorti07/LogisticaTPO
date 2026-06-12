package logistica.modelos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un movimiento registrado en el historial del inventario.
 */
public class Movimiento {

    public enum TipoMovimiento {
        ENTRADA, SALIDA, REGISTRO, ACTUALIZACION
    }

    private String codigoProducto;
    private TipoMovimiento tipo;
    private int cantidad;
    private String descripcion;
    private LocalDateTime fechaHora;

    public Movimiento(String codigoProducto, TipoMovimiento tipo, int cantidad, String descripcion) {
        this.codigoProducto = codigoProducto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.fechaHora = LocalDateTime.now();
    }

    public String getCodigoProducto() { return codigoProducto; }
    public TipoMovimiento getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("[%s] %s | Producto: %s | Cantidad: %d | %s",
                fechaHora.format(fmt), tipo, codigoProducto, cantidad, descripcion);
    }
}
