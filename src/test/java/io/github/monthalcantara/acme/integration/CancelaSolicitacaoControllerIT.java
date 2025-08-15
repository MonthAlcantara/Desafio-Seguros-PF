package io.github.monthalcantara.acme.integration;

import io.github.monthalcantara.acme.api.CancelaSolicitacaoController;
import io.github.monthalcantara.acme.application.service.AtualizaSolicitacaoStatusService;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.exception.SolicitacaoNaoEncontradaException;
import io.github.monthalcantara.acme.exception.StatusNaoPermitidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CancelaSolicitacaoController.class) // Apenas carrega o controller especificado
@DisplayName("Testes de integração para CancelaSolicitacaoController")
class CancelaSolicitacaoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtualizaSolicitacaoStatusService atualizaSolicitacaoStatusService;

    @Nested
    @DisplayName("Cenários de sucesso")
    class CenariosDeSucesso {
        @Test
        @DisplayName("Deve retornar status 204 quando a solicitacao for cancelada com sucesso")
        void deveRetornar204_QuandoSolicitacaoCanceladaComSucesso() throws Exception {
            final var solicitacaoIdValida = UUID.randomUUID();

            doNothing().when(atualizaSolicitacaoStatusService).cancelar(solicitacaoIdValida);

            mockMvc.perform(patch("/v1/propostas/{id}/cancelar", solicitacaoIdValida)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Cenários de falha")
    class CenariosDeFalha {
        @Test
        @DisplayName("Deve retornar status 404 quando a solicitacao nao for encontrada")
        void deveRetornar404_QuandoSolicitacaoNaoEncontrada() throws Exception {
            final var idInexistente = UUID.randomUUID();

            doThrow(new SolicitacaoNaoEncontradaException("Solicitação não encontrada: " + idInexistente))
                    .when(atualizaSolicitacaoStatusService)
                    .cancelar(idInexistente);

            mockMvc.perform(patch("/v1/propostas/{id}/cancelar", idInexistente)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Solicitação não encontrada: " + idInexistente));
        }

        @Test
        @DisplayName("Deve retornar status 400 quando o status da solicitacao nao permite cancelamento")
        void deveRetornar400_QuandoStatusNaoPermiteCancelamento() throws Exception {
            final var idAprovado = UUID.randomUUID();

            doThrow(new StatusNaoPermitidoException("Não é possível cancelar uma solicitação com status " + TipoStatus.APROVADO.getDescricao()))
                    .when(atualizaSolicitacaoStatusService)
                    .cancelar(idAprovado);

            mockMvc.perform(patch("/v1/propostas/{id}/cancelar", idAprovado)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Não é possível cancelar uma solicitação com status " + TipoStatus.APROVADO.getDescricao()));
        }
    }
}