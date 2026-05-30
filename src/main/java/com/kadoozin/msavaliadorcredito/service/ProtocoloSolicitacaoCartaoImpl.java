package com.kadoozin.msavaliadorcredito.service;

import com.kadoozin.msavaliadorcredito.clients.ClientResourceClient;
import com.kadoozin.msavaliadorcredito.clients.response.ClienteResponse;
import com.kadoozin.msavaliadorcredito.dto.request.DadosSolicitacaoEmissaoCartao;
import com.kadoozin.msavaliadorcredito.exceptions.DadosClienteNotFoundException;
import com.kadoozin.msavaliadorcredito.exceptions.ErroComunicacaoMicroserviceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProtocoloSolicitacaoCartaoImpl implements ProtocoloSolicitacaoCartao {

    private final ClientResourceClient clientResourceClient;
    private final SolicitacaoEmissaoCartaoPublisher publisher;

    @Override
    public void solicitarEmissao(DadosSolicitacaoEmissaoCartao dados) {
        ClienteResponse cliente = buscarDadosCliente(dados.cpf());

        DadosSolicitacaoEmissaoCartao dadosComEndereco = new DadosSolicitacaoEmissaoCartao(
                dados.idCartao(),
                dados.cpf(),
                cliente.endereco(),
                dados.limiteAprovado()
        );

        publisher.solicitarCartao(dadosComEndereco);
    }

    private ClienteResponse buscarDadosCliente(String cpf) {
        try {
            ResponseEntity<ClienteResponse> response = clientResourceClient.findClienteByCpf(cpf);
            if (response == null || response.getBody() == null) {
                throw new ErroComunicacaoMicroserviceException(
                        "Erro ao consultar dados do cliente no ms-clientes: resposta sem corpo.", 502);
            }
            return response.getBody();
        } catch (FeignException.NotFound ex) {
            throw new DadosClienteNotFoundException("Dados do cliente nao encontrados para o cpf informado.");
        } catch (FeignException ex) {
            throw new ErroComunicacaoMicroserviceException(
                    "Erro ao consultar dados do cliente no ms-clientes.", ex.status(), ex);
        }
    }
}
