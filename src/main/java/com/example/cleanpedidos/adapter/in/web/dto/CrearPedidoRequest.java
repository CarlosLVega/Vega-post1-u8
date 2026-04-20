package com.example.cleanpedidos.adapter.in.web.dto;

import com.example.cleanpedidos.usecase.dto.LineaPedidoDto;
import java.util.List;

public record CrearPedidoRequest(String clienteNombre, List<LineaPedidoDto> lineas) {
}