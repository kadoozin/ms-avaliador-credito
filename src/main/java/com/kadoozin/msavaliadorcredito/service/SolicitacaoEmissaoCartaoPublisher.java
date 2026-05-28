package com.kadoozin.msavaliadorcredito.service;

import com.kadoozin.msavaliadorcredito.dto.request.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.exchange}")
    private String exchange;

    @Value("${mq.queues.emissao-cartoes}")
    private String routingKey;

    public void solicitarCartao(DadosSolicitacaoEmissaoCartao dados) {
        rabbitTemplate.convertAndSend(exchange, routingKey, dados);
    }
}
