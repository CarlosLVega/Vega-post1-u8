package com.example.cleanpedidos.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record PedidoId(UUID valor) {
    public PedidoId {
        Objects.requireNonNull(valor, "PedidoId no puede ser nulo");
    }
    public static PedidoId nuevo() { return new PedidoId(UUID.randomUUID()); }
    public static PedidoId desde(String valor) { return new PedidoId(UUID.fromString(valor)); }
    @Override public String toString() { return valor.toString(); }
}