package com.example.cleanpedidos.adapter.in.web.dto;

import com.example.cleanpedidos.domain.valueobject.LineaPedido;
import java.math.BigDecimal;

public record LineaPedidoResponse(String productoNombre, int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
    public static LineaPedidoResponse fromDomain(LineaPedido linea) {
        return new LineaPedidoResponse(linea.productoNombre(), linea.cantidad(), linea.precioUnitario().cantidad(), linea.subtotal().cantidad());
    }
}