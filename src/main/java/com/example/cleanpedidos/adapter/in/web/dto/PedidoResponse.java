package com.example.cleanpedidos.adapter.in.web.dto;

import com.example.cleanpedidos.domain.entity.Pedido;
import java.math.BigDecimal;
import java.util.List;

public record PedidoResponse(String id, String clienteNombre, String estado, List<LineaPedidoResponse> lineas, BigDecimal total) {
    public static PedidoResponse fromDomain(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId().toString(),
                pedido.getClienteNombre(),
                pedido.getEstado().name(),
                pedido.getLineas().stream().map(LineaPedidoResponse::fromDomain).toList(),
                pedido.calcularTotal().cantidad());
    }
}