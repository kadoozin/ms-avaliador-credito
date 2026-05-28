package com.kadoozin.msavaliadorcredito.controller;

import com.kadoozin.msavaliadorcredito.dto.request.DadosSolicitacaoEmissaoCartao;
import com.kadoozin.msavaliadorcredito.service.EmissaoCartaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avaliacoes-credito")
@RequiredArgsConstructor
public class EmissaoCartaoController {

    private final EmissaoCartaoService emissaoCartaoService;

    @PostMapping("/solicitacoes-cartao")
    public ResponseEntity<Void> solicitarEmissaoCartao(@Valid @RequestBody DadosSolicitacaoEmissaoCartao request) {
        emissaoCartaoService.solicitarEmissao(request);
        return ResponseEntity.accepted().build();
    }
}
