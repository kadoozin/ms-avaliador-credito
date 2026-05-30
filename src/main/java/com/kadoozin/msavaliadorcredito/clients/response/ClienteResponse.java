package com.kadoozin.msavaliadorcredito.clients.response;

public record ClienteResponse(
        Integer clienteId,
        String cpf,
        String nome,
        Integer idade,
        String endereco
) {
}
