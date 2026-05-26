package com.kadoozin.msavaliadorcredito.dto.response;

import java.math.BigDecimal;

public record CartaoAprovadoResponse(
        String cartao,
        String bandeira,
        BigDecimal limiteAprovado
) {
}
