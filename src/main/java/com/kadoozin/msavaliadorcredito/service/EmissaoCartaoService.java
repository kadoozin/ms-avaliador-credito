package com.kadoozin.msavaliadorcredito.service;

import com.kadoozin.msavaliadorcredito.dto.request.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmissaoCartaoService {

    private final ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao;

    public void solicitarEmissao(DadosSolicitacaoEmissaoCartao dados) {
        protocoloSolicitacaoCartao.solicitarEmissao(dados);
    }
}
