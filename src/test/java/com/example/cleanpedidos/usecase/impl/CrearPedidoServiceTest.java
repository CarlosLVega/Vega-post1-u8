package com.example.cleanpedidos.usecase.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.dto.LineaPedidoDto;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CrearPedidoServiceTest {
    @Test
    void createsPedidoUsingRepositoryPortWithoutSpring() {
        InMemoryPedidoRepository repo = new InMemoryPedidoRepository();
        CrearPedidoService service = new CrearPedidoService(repo);
        PedidoId id = service.ejecutar("Ana Garcia", List.of(new LineaPedidoDto("Laptop", 1, BigDecimal.valueOf(1500))));
        assertNotNull(id);
        assertEquals(1, repo.buscarTodos().size());
    }

    private static class InMemoryPedidoRepository implements PedidoRepositoryPort {
        private final List<Pedido> pedidos = new ArrayList<>();
        @Override public void guardar(Pedido pedido) { pedidos.add(pedido); }
        @Override public Optional<Pedido> buscarPorId(PedidoId id) { return pedidos.stream().filter(p -> p.getId().equals(id)).findFirst(); }
        @Override public List<Pedido> buscarTodos() { return pedidos; }
    }
}