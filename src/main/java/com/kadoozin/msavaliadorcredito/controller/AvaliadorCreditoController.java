package com.kadoozin.msavaliadorcredito.controller;

import com.kadoozin.msavaliadorcredito.dto.request.DadosAvaliacaoRequest;
import com.kadoozin.msavaliadorcredito.dto.response.CartaoAprovadoResponse;
import com.kadoozin.msavaliadorcredito.dto.response.SituacaoCliente;
import com.kadoozin.msavaliadorcredito.service.AvaliadorCreditoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping(value = "/situacao-cliente", params = "cpf")
    public ResponseEntity<SituacaoCliente> consultaSituacaoCliente (@RequestParam("cpf") String cpf){
        SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
        return ResponseEntity.ok(situacaoCliente);
    }

    @PostMapping
    public ResponseEntity<List<CartaoAprovadoResponse>> realizarAvaliacaoCliente(@Valid @RequestBody DadosAvaliacaoRequest request) {
        List<CartaoAprovadoResponse> cartoesAprovados = avaliadorCreditoService.realizarAvaliacaoCliente(request.cpf());
        return ResponseEntity.ok(cartoesAprovados);
    }
}
