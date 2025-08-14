package io.github.monthalcantara.acme.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.TestUtils;
import io.github.monthalcantara.acme.application.service.FraudNotificationServiceImpl;
import io.github.monthalcantara.acme.infra.persistence.repository.SolicitacaoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes de integração para CriaSolicitacaoController")
class CriaSolicitacaoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @MockBean
    private FraudNotificationServiceImpl fraudNotificationServiceImpl;

    private final String idempotencyKey = UUID.randomUUID().toString();


    @AfterEach
    void tearDown() {
        solicitacaoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar uma nova solicitacao e retornar 201 Created")
    void deveCriarNovaSolicitacaoComSucesso() throws Exception {
        // Dado
        final var requestBody = TestUtils.lerPayloadDoArquivo("create-solicitacao-valida.json");

        // Quando
        final var result = mockMvc.perform(post("/v1/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-idempotency-key", idempotencyKey)
                .content(requestBody));

        // Então
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists());

        assertEquals(1, solicitacaoRepository.findAll().size());
    }

    @Test
    @DisplayName("Deve retornar a mesma solicitacao para a mesma chave de idempotencia")
    void deveRetornarSolicitacaoExistenteComMesmaChaveDeIdempotencia() throws Exception {
        // Dado
        final var request = TestUtils.lerPayloadDoArquivo("create-solicitacao-valida.json");

        // Primeira chamada
        final var result1 = mockMvc.perform(post("/v1/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-idempotency-key", idempotencyKey)
                .content(request));

        final var response1 = result1.andReturn().getResponse().getContentAsString();
        final var id1 = objectMapper.readTree(response1).get("id").asText();

        // Verificação no banco de dados após a primeira chamada
        assertEquals(1, solicitacaoRepository.findAll().size());

        // Segunda chamada com a mesma chave de idempotência
        final var result2 = mockMvc.perform(post("/v1/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-idempotency-key", idempotencyKey)
                .content(request));

        final var response2 = result2.andReturn().getResponse().getContentAsString();
        final var id2 = objectMapper.readTree(response2).get("id").asText();

        // Então
        result2.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists());

        assertEquals(id1, id2); // Os IDs devem ser os mesmos
        assertEquals(1, solicitacaoRepository.findAll().size());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request para payload com dados invalidos")
    void deveRetornar400ParaPayloadComDadosInvalidos() throws Exception {
        // Dado
        final var requestBody = TestUtils.lerPayloadDoArquivo("create-solicitacao-invalida-dados.json");

        // Quando
        final var result = mockMvc.perform(post("/v1/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-idempotency-key", idempotencyKey)
                .content(requestBody));

        // Então
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.error.categoria").value("A categoria é obrigatória"));

        assertFalse(solicitacaoRepository.findAll().iterator().hasNext());
    }
}