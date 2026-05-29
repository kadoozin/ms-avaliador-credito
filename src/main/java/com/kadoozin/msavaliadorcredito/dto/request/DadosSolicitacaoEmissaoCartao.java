package com.kadoozin.msavaliadorcredito.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record DadosSolicitacaoEmissaoCartao(
        @NotNull(message = "idCartao e obrigatorio")
        Long idCartao,

        @NotBlank(message = "cpf e obrigatorio")
        @CPF(message = "cpf invalido")
        String cpf,

        @NotBlank(message = "endereco e obrigatorio")
        String endereco
) {
}
