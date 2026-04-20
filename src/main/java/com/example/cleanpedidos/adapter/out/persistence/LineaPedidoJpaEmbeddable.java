package com.example.cleanpedidos.adapter.out.persistence;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class LineaPedidoJpaEmbeddable {
    private String productoNombre;
    private int cantidad;
    private BigDecimal precioUnitario;

    public LineaPedidoJpaEmbeddable() {
    }

    public LineaPedidoJpaEmbeddable(String productoNombre, int cantidad, BigDecimal precioUnitario) {
        this.productoNombre = productoNombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public String getProductoNombre() { return productoNombre; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
}