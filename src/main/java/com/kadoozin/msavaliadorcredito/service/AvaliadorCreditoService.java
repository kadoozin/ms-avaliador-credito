package com.kadoozin.msavaliadorcredito.service;

import com.kadoozin.msavaliadorcredito.clients.CartoesResourceClient;
import com.kadoozin.msavaliadorcredito.clients.ClientResourceClient;
import com.kadoozin.msavaliadorcredito.clients.response.CartaoCliente;
import com.kadoozin.msavaliadorcredito.clients.response.ClienteResponse;
import com.kadoozin.msavaliadorcredito.dto.request.DadosAvaliacaoRequest;
import com.kadoozin.msavaliadorcredito.dto.response.CartaoAprovadoResponse;
import com.kadoozin.msavaliadorcredito.dto.response.CartaoElegivelResponse;
import com.kadoozin.msavaliadorcredito.dto.response.SituacaoCliente;
import com.kadoozin.msavaliadorcredito.exceptions.DadosClienteNotFoundException;
import com.kadoozin.msavaliadorcredito.exceptions.ErroComunicacaoMicroserviceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClientResourceClient clientResourceClient;
    private final CartoesResourceClient cartoesResourceClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) {
        ClienteResponse clienteResponse = buscarDadosCliente(cpf);
        List<CartaoCliente> cartoes = buscarCartoesDoCliente(cpf);

        return montarSituacaoCliente(clienteResponse, cartoes);
    }

    public List<CartaoAprovadoResponse> realizarAvaliacaoCliente(DadosAvaliacaoRequest request) {
        ClienteResponse clienteResponse = buscarDadosCliente(request.cpf());
        List<CartaoElegivelResponse> cartoesElegiveis = buscarCartoesElegiveis(request.renda());
        Integer idadeCliente = obterIdadeObrigatoria(clienteResponse);

        return cartoesElegiveis.stream()
                .map(cartaoElegivel -> montarCartaoAprovado(idadeCliente, cartaoElegivel))
                .toList();
    }

    private ClienteResponse buscarDadosCliente(String cpf) {
        try {
            return obterBodyObrigatorio(
                    clientResourceClient.findClienteByCpf(cpf),
                    "Erro ao consultar dados do cliente no ms-clientes: resposta sem corpo."
            );
        } catch (FeignException.NotFound ex) {
            throw new DadosClienteNotFoundException("Dados do cliente nao encontrados para o cpf informado.");
        } catch (FeignException ex) {
            throw new ErroComunicacaoMicroserviceException(
                    "Erro ao consultar dados do cliente no ms-clientes.",
                    ex.status(),
                    ex
            );
        }
    }

    private List<CartaoCliente> buscarCartoesDoCliente(String cpf) {
        try {
            ResponseEntity<List<CartaoCliente>> response = cartoesResourceClient.getCartoesByCpf(cpf);
            if (response == null || response.getBody() == null) {
                return List.of();
            }
            return response.getBody();
        } catch (FeignException.NotFound ex) {
            return List.of();
        } catch (FeignException ex) {
            throw new ErroComunicacaoMicroserviceException(
                    "Erro ao consultar cartoes do cliente no ms-cartoes.",
                    ex.status(),
                    ex
            );
        }
    }

    private List<CartaoElegivelResponse> buscarCartoesElegiveis(Long renda) {
        try {
            ResponseEntity<List<CartaoElegivelResponse>> response =
                    cartoesResourceClient.getCartoesElegiveisByRenda(renda);
            if (response == null || response.getBody() == null) {
                return List.of();
            }
            return response.getBody();
        } catch (FeignException.NotFound ex) {
            return List.of();
        } catch (FeignException ex) {
            throw new ErroComunicacaoMicroserviceException(
                    "Erro ao consultar cartoes elegiveis no ms-cartoes.",
                    ex.status(),
                    ex
            );
        }
    }

    private <T> T obterBodyObrigatorio(ResponseEntity<T> responseEntity, String mensagemErro) {
        if (responseEntity == null) {
            throw new ErroComunicacaoMicroserviceException(
                    mensagemErro + " ResponseEntity nulo.",
                    502
            );
        }

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            int status = responseEntity.getStatusCode().value();
            throw new ErroComunicacaoMicroserviceException(
                    mensagemErro + " Status HTTP inesperado: " + status + ".",
                    status
            );
        }

        T body = responseEntity.getBody();
        if (body == null) {
            throw new ErroComunicacaoMicroserviceException(mensagemErro, 502);
        }
        return body;
    }

    private SituacaoCliente montarSituacaoCliente(ClienteResponse clienteResponse, List<CartaoCliente> cartoes) {
        Long clienteId = clienteResponse.clienteId() != null
                ? clienteResponse.clienteId().longValue()
                : null;
        return new SituacaoCliente(clienteId, clienteResponse.nome(), cartoes == null ? List.of() : cartoes);
    }

    private Integer obterIdadeObrigatoria(ClienteResponse clienteResponse) {
        Integer idadeCliente = clienteResponse.idade();
        if (idadeCliente == null || idadeCliente <= 0) {
            throw new ErroComunicacaoMicroserviceException(
                    "Erro ao consultar dados do cliente no ms-clientes: idade invalida para avaliacao.",
                    502
            );
        }
        return idadeCliente;
    }

    private CartaoAprovadoResponse montarCartaoAprovado(Integer idadeCliente, CartaoElegivelResponse cartaoElegivel) {
        if (cartaoElegivel.limiteBasico() == null) {
            throw new ErroComunicacaoMicroserviceException(
                    "Erro ao consultar cartoes elegiveis no ms-cartoes: limiteBasico ausente.",
                    502
            );
        }

        BigDecimal fatorIdade = BigDecimal.valueOf(idadeCliente)
                .divide(BigDecimal.TEN);
        BigDecimal limiteAprovado = cartaoElegivel.limiteBasico()
                .multiply(fatorIdade);

        return new CartaoAprovadoResponse(
                cartaoElegivel.nome(),
                cartaoElegivel.bandeiraCartao(),
                limiteAprovado
        );
    }
}
