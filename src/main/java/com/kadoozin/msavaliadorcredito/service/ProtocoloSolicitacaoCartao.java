package com.kadoozin.msavaliadorcredito.service;

import com.kadoozin.msavaliadorcredito.dto.request.DadosSolicitacaoEmissaoCartao;

public interface ProtocoloSolicitacaoCartao {
    void solicitarEmissao(DadosSolicitacaoEmissaoCartao dados);
}
