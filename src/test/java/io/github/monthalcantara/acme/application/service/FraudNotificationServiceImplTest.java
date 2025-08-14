package io.github.monthalcantara.acme.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.infra.fraud.dto.response.FraudCheckResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de unidade para FraudNotificationServiceImpl")
class FraudNotificationServiceImplTest {

    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse<String> httpResponse;
    @Mock
    private AtualizaSolicitacaoStatusService atualizaSolicitacaoStatusService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private FraudNotificationServiceImpl fraudNotificationService;

    private Solicitacao solicitacao;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        objectMapper.registerModule(new JavaTimeModule());

        ReflectionTestUtils.setField(fraudNotificationService, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(fraudNotificationService, "fraudApiUrl", "http://localhost:8081/api/v1/fraud-check");

        solicitacao = Solicitacao.builder().id(UUID.randomUUID()).build();

        lenient().when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
    }

    @Test
    @DisplayName("Deve atualizar o status da solicitacao se a API retornar 200 OK")
    void deveAtualizarStatusComBaseEmRespostaSucesso() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        // Dado
        final String jsonResponse = "{\"orderId\":\"" + solicitacao.getId() + "\",\"customerId\":\"" + UUID.randomUUID() + "\",\"analyzedAt\":\"2024-05-15T10:30:00Z\",\"classification\":\"REGULAR\"}";
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(jsonResponse);

        // Quando
        final CompletableFuture<Void> future = fraudNotificationService.notifyAsync(solicitacao);

        // Então
        // Aguarda a execução da thread assíncrona
        future.get(5, TimeUnit.SECONDS);

        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(atualizaSolicitacaoStatusService, times(1))
                .atualizarStatusComBaseEmFraude(eq(solicitacao.getId()), any(FraudCheckResponse.class));
    }

    @Test
    @DisplayName("Nao deve atualizar o status se a API retornar erro (diferente de 200)")
    void naoDeveAtualizarStatusComBaseEmRespostaErro() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        // Dado
        when(httpResponse.statusCode()).thenReturn(500);
        when(httpResponse.body()).thenReturn("Erro interno do servidor");

        // Quando
        final CompletableFuture<Void> future = fraudNotificationService.notifyAsync(solicitacao);

        // Então
        // Aguarda a execução da thread assíncrona
        future.get(5, TimeUnit.SECONDS);

        verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(atualizaSolicitacaoStatusService, never())
                .atualizarStatusComBaseEmFraude(any(UUID.class), any(FraudCheckResponse.class));
    }
}