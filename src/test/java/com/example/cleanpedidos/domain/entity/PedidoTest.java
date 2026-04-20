package com.example.cleanpedidos.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.EstadoPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PedidoTest {
    @Test
    void confirmsPedidoAndCalculatesTotalWithoutSpringContext() {
        Pedido pedido = new Pedido(PedidoId.nuevo(), "Ana Garcia");
        pedido.agregarLinea("Laptop", 1, new Dinero(BigDecimal.valueOf(1500)));
        pedido.agregarLinea("Mouse", 2, new Dinero(BigDecimal.valueOf(25)));
        pedido.confirmar();
        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
        assertEquals(BigDecimal.valueOf(1550), pedido.calcularTotal().cantidad());
    }

    @Test
    void rejectsEmptyCustomer() {
        assertThrows(IllegalArgumentException.class, () -> new Pedido(PedidoId.nuevo(), " "));
    }
}