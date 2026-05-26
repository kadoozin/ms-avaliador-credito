package com.kadoozin.msavaliadorcredito.clients;

import com.kadoozin.msavaliadorcredito.clients.response.ClienteResponse;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ms-clientes", path = "/clientes")
public interface ClientResourceClient {

    @GetMapping("/{cpf}")
    ResponseEntity<ClienteResponse> findClienteByCpf(@PathVariable("cpf") @CPF String cpf);
}
