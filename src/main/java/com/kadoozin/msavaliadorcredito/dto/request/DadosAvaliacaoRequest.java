package com.kadoozin.msavaliadorcredito.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.br.CPF;

public record DadosAvaliacaoRequest(
        @NotBlank(message = "cpf e obrigatorio")
        @CPF(message = "cpf invalido")
        String cpf,

        @NotNull(message = "renda e obrigatoria")
        @Positive(message = "renda deve ser maior que zero")
        Long renda
) {
}
