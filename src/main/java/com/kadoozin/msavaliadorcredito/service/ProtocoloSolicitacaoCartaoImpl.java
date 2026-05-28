package com.kadoozin.msavaliadorcredito.service;

import com.kadoozin.msavaliadorcredito.dto.request.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProtocoloSolicitacaoCartaoImpl implements ProtocoloSolicitacaoCartao {

    private final AvaliadorCreditoService avaliadorCreditoService;
    private final SolicitacaoEmissaoCartaoPublisher publisher;

    @Override
    public void solicitarEmissao(DadosSolicitacaoEmissaoCartao dados) {
        avaliadorCreditoService.obterSituacaoCliente(dados.cpf());
        publisher.solicitarCartao(dados);
    }
}
