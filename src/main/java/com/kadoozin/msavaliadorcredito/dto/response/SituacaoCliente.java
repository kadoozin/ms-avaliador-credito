package com.kadoozin.msavaliadorcredito.dto.response;

import com.kadoozin.msavaliadorcredito.clients.response.CartaoCliente;

import java.util.List;

public record SituacaoCliente(
        Long clienteId,
        String clienteNome,
        List<CartaoCliente> cartoes
) {
}
