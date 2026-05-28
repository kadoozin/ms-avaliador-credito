package com.kadoozin.msavaliadorcredito.service;

import com.kadoozin.msavaliadorcredito.dto.event.ClienteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClienteEventListener {

    @RabbitListener(queues = "${rabbitmq.queue.cliente-criado}")
    public void handleClienteCriado(ClienteEvent event) {
        log.info("Evento recebido: action={}, cpf={}, nome={}, idade={}",
                event.action(), maskCpf(event.cpf()), event.nome(), event.idade());
    }

    private String maskCpf(String cpf) {
        if (cpf == null || cpf.length() < 4) return "***";
        return "***" + cpf.substring(cpf.length() - 4);
    }
}
