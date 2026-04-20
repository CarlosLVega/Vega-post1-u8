package com.example.cleanpedidos.adapter.out.persistence;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.EstadoPedido;
import com.example.cleanpedidos.domain.valueobject.LineaPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {
    private final PedidoJpaRepository jpa;

    public PedidoRepositoryAdapter(PedidoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void guardar(Pedido pedido) {
        jpa.save(toEntity(pedido));
    }

    @Override
    public Optional<Pedido> buscarPorId(PedidoId id) {
        return jpa.findById(id.toString()).map(this::toDomain);
    }

    @Override
    public List<Pedido> buscarTodos() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    private Pedido toDomain(PedidoJpaEntity entity) {
        List<LineaPedido> lineas = entity.getLineas().stream()
                .map(linea -> new LineaPedido(linea.getProductoNombre(), linea.getCantidad(), new Dinero(linea.getPrecioUnitario())))
                .toList();
        return Pedido.reconstruir(PedidoId.desde(entity.getId()), entity.getClienteNombre(), EstadoPedido.valueOf(entity.getEstado()), lineas);
    }

    private PedidoJpaEntity toEntity(Pedido pedido) {
        List<LineaPedidoJpaEmbeddable> lineas = pedido.getLineas().stream()
                .map(linea -> new LineaPedidoJpaEmbeddable(linea.productoNombre(), linea.cantidad(), linea.precioUnitario().cantidad()))
                .toList();
        return new PedidoJpaEntity(pedido.getId().toString(), pedido.getClienteNombre(), pedido.getEstado().name(), lineas);
    }
}