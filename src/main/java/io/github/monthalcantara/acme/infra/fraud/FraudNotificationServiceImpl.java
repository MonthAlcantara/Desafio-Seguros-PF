package io.github.monthalcantara.acme.infra.fraud;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.application.service.AtualizaSolicitacaoStatusService;
import io.github.monthalcantara.acme.application.service.FraudNotificationService;
import io.github.monthalcantara.acme.infra.fraud.dto.request.FraudCheckRequest;
import io.github.monthalcantara.acme.infra.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class FraudNotificationServiceImpl implements FraudNotificationService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final AtualizaSolicitacaoStatusService atualizaSolicitacaoStatusService;
    @Value("${acme.fraud-api.url}")
    private String fraudApiUrl;

    public FraudNotificationServiceImpl(final HttpClient httpClient, final ObjectMapper objectMapper, final AtualizaSolicitacaoStatusService atualizaSolicitacaoStatusService) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.atualizaSolicitacaoStatusService = atualizaSolicitacaoStatusService;
    }

    @Override
    public void notifyAsync(final Solicitacao solicitacao) {
        CompletableFuture.runAsync(() -> {
            try {
                final var payload = new FraudCheckRequest(solicitacao.getClienteId(), solicitacao.getId());
                final var jsonPayload = objectMapper.writeValueAsString(payload);

                log.info("[Fraude] Enviando requisição para API de fraudes. ID={}. Endpoint: {}", solicitacao.getId(), fraudApiUrl);

                final var request = HttpRequest.newBuilder()
                        .uri(URI.create(fraudApiUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

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