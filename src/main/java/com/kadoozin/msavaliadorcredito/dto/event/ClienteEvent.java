package com.kadoozin.msavaliadorcredito.dto.event;

public record ClienteEvent(
        String action,
        String cpf,
        String nome,
        Integer idade
) {
    public static final String ACTION_CLIENTE_CRIADO = "CLIENTE_CRIADO";
}
