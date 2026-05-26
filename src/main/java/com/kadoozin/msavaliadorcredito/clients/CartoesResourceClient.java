package com.kadoozin.msavaliadorcredito.clients;

import com.kadoozin.msavaliadorcredito.database.model.CartaoCliente;
import com.kadoozin.msavaliadorcredito.dto.response.CartaoElegivelResponse;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "ms-cartoes", path = "/cartoes")
public interface CartoesResourceClient {

    @GetMapping("/elegiveis")
    ResponseEntity<List<CartaoElegivelResponse>> getCartoesElegiveisByRenda(@RequestParam("renda") Long renda);

    @GetMapping("/cliente")
    ResponseEntity<List<CartaoCliente>> getCartoesByCpf(@RequestParam("cpf") @CPF String cpf);
}
