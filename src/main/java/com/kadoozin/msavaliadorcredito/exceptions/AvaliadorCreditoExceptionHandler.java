package com.kadoozin.msavaliadorcredito.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AvaliadorCreditoExceptionHandler {

    @ExceptionHandler(DadosClienteNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleDadosClienteNotFound(DadosClienteNotFoundException ex) {
        log.warn("Dados do cliente nao encontrados: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Dados do cliente nao encontrados");
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(ErroComunicacaoMicroserviceException.class)
    public ResponseEntity<ProblemDetail> handleErroComunicacao(ErroComunicacaoMicroserviceException ex) {
        HttpStatus statusResposta = resolveStatus(ex.getStatus());
        if (statusResposta.is4xxClientError()) {
            log.warn("Erro retornado por microservico. statusOrigem={}, statusResposta={}, mensagem={}",
                    ex.getStatus(), statusResposta.value(), ex.getMessage());
        } else {
            log.error("Erro de comunicacao com microservico. statusOrigem={}, statusResposta={}, mensagem={}",
                    ex.getStatus(), statusResposta.value(), ex.getMessage(), ex);
        }

        ProblemDetail problemDetail = ProblemDetail.forStatus(statusResposta);
        problemDetail.setTitle("Erro de comunicacao com microservico");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty("statusOrigem", ex.getStatus());
        return ResponseEntity.status(statusResposta).body(problemDetail);
    }

    private HttpStatus resolveStatus(int statusOrigem) {
        HttpStatus status = HttpStatus.resolve(statusOrigem);
        if (status == null) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }

        if (status.is4xxClientError()) {
            return status;
        }

        if (status.is5xxServerError()) {
            return HttpStatus.BAD_GATEWAY;
        }

        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}
