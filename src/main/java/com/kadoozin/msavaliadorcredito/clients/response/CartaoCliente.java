package com.kadoozin.msavaliadorcredito.clients.response;

import java.math.BigDecimal;

public record CartaoCliente(
        String nome,
        String bandeira,
        BigDecimal limite
) {
}
