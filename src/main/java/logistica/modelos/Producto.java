package logistica.modelos;

/**
 * Representa un producto dentro del inventario del almacén.
 */
public class Producto {

    private String codigo;
    private String nombre;
    private int stock;
    private int stockMinimo;
    private String ubicacion; // Ej: "A1", "B3", "C2"

    public Producto(String codigo, String nombre, int stock, int stockMinimo, String ubicacion) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("El código no puede estar vacío.");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
        if (stockMinimo < 0) throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");

        this.codigo = codigo.toUpperCase();
        this.nombre = nombre;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.ubicacion = ubicacion;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }
    public String getUbicacion() { return ubicacion; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
        this.stock = stock;
    }

    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public boolean tieneStockCritico() {
        return this.stock <= this.stockMinimo;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Stock: %d | Mínimo: %d | Ubicación: %s%s",
                codigo, nombre, stock, stockMinimo, ubicacion,
                tieneStockCritico() ? " ⚠ STOCK CRÍTICO" : "");
    }
}
