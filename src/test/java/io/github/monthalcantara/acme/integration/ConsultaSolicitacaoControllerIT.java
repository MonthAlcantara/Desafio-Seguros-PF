package io.github.monthalcantara.acme.integration;

import io.github.monthalcantara.acme.TestUtils;
import io.github.monthalcantara.acme.application.service.FraudNotificationServiceImpl;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infra.persistence.repository.SolicitacaoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes de integração para ConsultaSolicitacaoController")
class ConsultaSolicitacaoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @MockBean
    private FraudNotificationServiceImpl fraudNotificationServiceImpl;

    private SolicitacaoEntity solicitacaoSalva;
    private UUID clienteId;

    @BeforeEach
    void setup() {
        clienteId = UUID.randomUUID();
        solicitacaoSalva = TestUtils.criarSolicitacaoEntity(clienteId);
        solicitacaoRepository.save(solicitacaoSalva);
    }

    @AfterEach
    void tearDown() {
        solicitacaoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Teste do endpoint GET /v1/propostas/{id}")
    class PorIdTest {
        @Test
        @DisplayName("Deve retornar status 200 e os dados da solicitacao quando o ID existir")
        void deveRetornar200_QuandoIdExistir() throws Exception {
            final var id = solicitacaoSalva.getId();

            mockMvc.perform(get("/v1/propostas/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.customer_id").value(clienteId.toString()))
                    .andExpect(jsonPath("$.status").value(solicitacaoSalva.getStatus().getDescricao()))
                    .andExpect(jsonPath("$.category").value(solicitacaoSalva.getCategoria().getDescricao()));
        }

        @Test
        @DisplayName("Deve retornar status 404 quando o ID não for encontrado")
        void deveRetornar404_QuandoIdNaoExistir() throws Exception {
            final var idInexistente = UUID.randomUUID();

            mockMvc.perform(get("/v1/propostas/{id}", idInexistente)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Solicitacao não encontrada para o ID: " + idInexistente));
        }
    }

    @Nested
    @DisplayName("Testes do endpoint GET /v1/propostas/cliente/{clienteId}")
    class PorClienteIdTest {
        @Test
        @DisplayName("Deve retornar status 200 e a lista de solicitacoes quando o cliente tiver propostas")
        void deveRetornar200_QuandoClienteTiverSolicitacoes() throws Exception {
            mockMvc.perform(get("/v1/propostas/cliente/{clienteId}", clienteId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].customer_id").value(clienteId.toString()))
                    .andExpect(jsonPath("$[0].id").value(solicitacaoSalva.getId().toString()));
        }

        @Test
        @DisplayName("Deve retornar status 200 e uma lista vazia quando o cliente nao tiver propostas")
        void deveRetornar200_QuandoClienteNaoTiverSolicitacoes() throws Exception {
            final var clienteIdSemSolicitacoes = UUID.randomUUID();

            mockMvc.perform(get("/v1/propostas/cliente/{clienteId}", clienteIdSemSolicitacoes)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }
}