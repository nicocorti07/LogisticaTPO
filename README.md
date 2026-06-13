# Centro Logístico de Distribución Avanzada

**Materia:** Programación II — UADE  
**Entrega:** Segunda entrega del TPO

---

## Integrantes

| Integrante           | Actividades realizadas                                      |
|----------------------|-------------------------------------------------------------|
| Anzoategui Ignacio   | Implementación de `TablaHashInventario` y `SistemaLogistico` |
| Cortiñas Nicolas     | Implementación de `HeapMinStock` y modelos (`Producto`, `Movimiento`) |
| Ponce Octavio        | Implementación de `ColaDespacho`, `PilaHistorial` y clase `Main` |

---

## Alternativa elegida

**Centro Logístico de Distribución Avanzada**: sistema de gestión de inventario, pedidos y despacho para un depósito logístico.

---

## Estructuras de datos utilizadas

| Estructura       | Clase                   | Uso en el sistema                                          |
|------------------|-------------------------|------------------------------------------------------------|
| **Tabla Hash**   | `TablaHashInventario`   | Almacena y localiza productos por código en O(1) promedio  |
| **Heap Mínimo**  | `HeapMinStock`          | Detecta los productos con menor stock para reposición urgente |
| **Cola FIFO**    | `ColaDespacho`          | Administra el orden de despacho de pedidos completados      |
| **Pila LIFO**    | `PilaHistorial`         | Registra el historial de movimientos del inventario         |

---

## Funcionalidades implementadas en esta segunda etapa

### 1. Gestión de inventario (Tabla Hash)
- Registrar productos con código, nombre, stock, stock mínimo y ubicación física.
- Buscar un producto por código en tiempo O(1) promedio.
- Actualizar stock por entradas y salidas de mercadería.
- Listar todos los productos del inventario.

### 2. Detección de stock crítico (Heap Mínimo)
- Identificar automáticamente el producto con menor stock.
- Listar el top N de productos con stock más bajo.
- Detección de productos que superaron el umbral mínimo.

### 3. Cola de despacho (Cola FIFO)
- Registrar pedidos listos para despacho.
- Despachar pedidos en orden de llegada (FIFO).
- Consultar la cola de pedidos pendientes.

### 4. Historial de movimientos (Pila LIFO)
- Registrar automáticamente cada operación (registro, entrada, salida, pedido).
- Consultar el historial completo del más reciente al más antiguo.
- Ver el último movimiento registrado.

---

## Estructura del proyecto

```
LogisticaTPO/
├── src/
│   └── main/
│       └── java/
│           └── logistica/
│               ├── estructuras/
│               │   ├── TablaHashInventario.java   ← Tabla Hash con chaining
│               │   ├── HeapMinStock.java           ← Min-Heap por nivel de stock
│               │   ├── ColaDespacho.java           ← Cola FIFO de pedidos
│               │   └── PilaHistorial.java          ← Pila LIFO de movimientos
│               ├── modelos/
│               │   ├── Producto.java
│               │   ├── Pedido.java
│               │   └── Movimiento.java
│               ├── servicios/
│               │   └── SistemaLogistico.java       ← Lógica de negocio central
│               └── menu/
│                   └── Main.java                   ← Menú interactivo de prueba
└── README.md
```
