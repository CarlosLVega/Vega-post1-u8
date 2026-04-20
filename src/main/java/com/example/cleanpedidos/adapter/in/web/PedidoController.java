package com.example.cleanpedidos.adapter.in.web;

import com.example.cleanpedidos.adapter.in.web.dto.CrearPedidoRequest;
import com.example.cleanpedidos.adapter.in.web.dto.PedidoResponse;
import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import com.example.cleanpedidos.usecase.CrearPedidoUseCase;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final CrearPedidoUseCase crearUseCase;
    private final ConsultarPedidoUseCase consultarUseCase;

    public PedidoController(CrearPedidoUseCase crearUseCase, ConsultarPedidoUseCase consultarUseCase) {
        this.crearUseCase = crearUseCase;
        this.consultarUseCase = consultarUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crear(@RequestBody CrearPedidoRequest request) {
        PedidoId id = crearUseCase.ejecutar(request.clienteNombre(), request.lineas());
        return Map.of("pedidoId", id.toString());
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable String id) {
        Pedido pedido = consultarUseCase.buscarPorId(PedidoId.desde(id));
        return PedidoResponse.fromDomain(pedido);
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return consultarUseCase.listarTodos().stream().map(PedidoResponse::fromDomain).toList();
    }
}