package io.github.monthalcantara.acme.infra.client.fraud;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.infra.client.fraud.dto.request.FraudCheckRequest;
import io.github.monthalcantara.acme.infra.client.fraud.dto.response.FraudCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;


@Slf4j
@Service
public
class FraudClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    @Value("${acme.fraud-api.url}")
    private String fraudApiUrl;

    public FraudClient(
            final HttpClient httpClient,
            final ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public FraudCheckResponse checkFraud(final Solicitacao solicitacao) {
        try {
            final FraudCheckRequest payload = new FraudCheckRequest(solicitacao.getClienteId(), solicitacao.getId());
            final String jsonPayload = objectMapper.writeValueAsString(payload);

            final String apiUrlWithScenario = String.format("%s?scenario=%d", fraudApiUrl, new Random().nextInt(4));
            log.info("[Fraude] Enviando requisição para API de fraudes. ID={}. Endpoint: {}", solicitacao.getId(), apiUrlWithScenario);

            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrlWithScenario))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("[Fraude] Resposta recebida da API. ID={}. Status: {}", solicitacao.getId(), response.statusCode());

            if (response.statusCode() == 200) {
                final var fraudResponse = objectMapper.readValue(response.body(), FraudCheckResponse.class);
                log.info("[Fraude] Resposta processada com sucesso. ID={}. Classificação: {}", solicitacao.getId(), fraudResponse.getClassificacao());
                return fraudResponse;
            } else {
                log.error("[Fraude] Erro na resposta da API. ID={}. Status: {}. Corpo: {}", solicitacao.getId(), response.statusCode(), response.body());
                // Lidar com a falha da API, como lançar uma exceção para que o evento não seja removido do outbox
                throw new IOException("Falha na chamada da API de fraudes com status: " + response.statusCode());
            }
            //TODO criar erro personalizado
        } catch (final Exception e) {
            log.error("[Fraude] Falha inesperada ao notificar API. ID={}. Erro: {}", solicitacao.getId(), e.getMessage());
            // A exceção será capturada no Scheduler, garantindo que o evento não seja excluído
            throw new RuntimeException("Falha na checagem de fraude", e);
        }
    }
}