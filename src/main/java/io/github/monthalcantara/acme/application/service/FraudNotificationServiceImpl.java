package io.github.monthalcantara.acme.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.infra.fraud.dto.request.FraudCheckRequest;
import io.github.monthalcantara.acme.infra.fraud.dto.response.FraudCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class FraudNotificationServiceImpl implements FraudNotificationService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final AtualizaSolicitacaoStatusService atualizaSolicitacaoStatusService;

    public FraudNotificationServiceImpl(HttpClient httpClient, ObjectMapper objectMapper, AtualizaSolicitacaoStatusService atualizaSolicitacaoStatusService) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.atualizaSolicitacaoStatusService = atualizaSolicitacaoStatusService;
    }

    @Value("${acme.fraud-api.url}")
    private String fraudApiUrl;

    @Override
    public CompletableFuture<Void> notifyAsync(Solicitacao solicitacao) {
        return CompletableFuture.runAsync(() -> {
            try {
                FraudCheckRequest payload = new FraudCheckRequest(solicitacao.getClienteId(), solicitacao.getId());
                String jsonPayload = objectMapper.writeValueAsString(payload);

                String apiUrlWithScenario = String.format("%s?scenario=%d", fraudApiUrl, new Random().nextInt(4));
                log.info("[Fraude] Enviando requisição para API de fraudes. ID={}. Endpoint: {}", solicitacao.getId(), apiUrlWithScenario);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrlWithScenario))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                log.info("[Fraude] Resposta recebida da API. ID={}. Status: {}", solicitacao.getId(), response.statusCode());

                if (response.statusCode() == 200) {
                    final var fraudResponse = objectMapper.readValue(response.body(), FraudCheckResponse.class);
                    log.info("[Fraude] Resposta processada com sucesso. ID={}. Classificação: {}", solicitacao.getId(), fraudResponse.getClassificacao());
                    atualizaSolicitacaoStatusService.atualizarStatusComBaseEmFraude(solicitacao.getId(), fraudResponse);
                } else {
                    log.error("[Fraude] Erro na resposta da API. ID={}. Status: {}. Corpo: {}", solicitacao.getId(), response.statusCode(), response.body());
                }
            } catch (Exception e) {
                log.error("[Fraude] Falha inesperada ao notificar API. ID={}. Erro: {}", solicitacao.getId(), e.getMessage());
            }
        });
    }
}