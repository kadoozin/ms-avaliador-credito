package com.kadoozin.msavaliadorcredito.dto.response;

import java.math.BigDecimal;

public record CartaoElegivelResponse(
        Integer cartaoId,
        String nome,
        String bandeiraCartao,
        BigDecimal rendaMinima,
        BigDecimal rendaMaxima,
        BigDecimal limiteBasico
) {
}
