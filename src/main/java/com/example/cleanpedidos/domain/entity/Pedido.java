package com.example.cleanpedidos.domain.entity;

import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.EstadoPedido;
import com.example.cleanpedidos.domain.valueobject.LineaPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Pedido {
    private final PedidoId id;
    private final String clienteNombre;
    private final List<LineaPedido> lineas = new ArrayList<>();
    private EstadoPedido estado = EstadoPedido.BORRADOR;

    public Pedido(PedidoId id, String clienteNombre) {
        this.id = Objects.requireNonNull(id, "El id del pedido es obligatorio");
        if (clienteNombre == null || clienteNombre.isBlank()) throw new IllegalArgumentException("El cliente es obligatorio");
        this.clienteNombre = clienteNombre;
    }

    public static Pedido reconstruir(PedidoId id, String clienteNombre, EstadoPedido estado, List<LineaPedido> lineas) {
        Pedido pedido = new Pedido(id, clienteNombre);
        pedido.lineas.addAll(lineas);
        pedido.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
        return pedido;
    }

    public void agregarLinea(String producto, int cantidad, Dinero precio) {
        if (estado != EstadoPedido.BORRADOR) throw new IllegalStateException("Solo se pueden agregar lineas en estado BORRADOR");
        lineas.add(new LineaPedido(producto, cantidad, precio));
    }

    public void confirmar() {
        if (lineas.isEmpty()) throw new IllegalStateException("No se puede confirmar un pedido sin lineas");
        this.estado = EstadoPedido.CONFIRMADO;
    }

    public Dinero calcularTotal() {
        return lineas.stream().map(LineaPedido::subtotal).reduce(Dinero.CERO, Dinero::sumar);
    }

    public PedidoId getId() { return id; }
    public String getClienteNombre() { return clienteNombre; }
    public List<LineaPedido> getLineas() { return Collections.unmodifiableList(lineas); }
    public EstadoPedido getEstado() { return estado; }
}