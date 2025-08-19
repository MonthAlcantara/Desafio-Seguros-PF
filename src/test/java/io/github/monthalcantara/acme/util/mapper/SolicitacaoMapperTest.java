package io.github.monthalcantara.acme.util.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monthalcantara.acme.TestUtils;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import io.github.monthalcantara.acme.infrastructure.client.fraud.dto.response.FraudCheckResponse;
import io.github.monthalcantara.acme.infrastructure.persistence.entity.SolicitacaoEntity;
import io.github.monthalcantara.acme.infrastructure.web.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.infrastructure.web.dto.response.SolicitacaoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SolicitacaoMapperTest {

    @Test
    @DisplayName("Deve mapear SolicitacaoRequest para Solicitacao com sucesso")
    void deveMapearSolicitacaoRequestParaModelComSucesso() throws Exception {

        String payloadResponse = TestUtils.lerPayloadDoArquivo("solicitacao/create-solicitacao-valida.json");
        SolicitacaoRequest request =  new ObjectMapper().readValue(payloadResponse, SolicitacaoRequest.class);


        Solicitacao model = SolicitacaoMapper.toModel(request);


        assertNotNull(model);
        assertEquals(request.getCategoria(), model.getCategoria());
        assertEquals(request.getClienteId(), model.getClienteId());
    }

    @Test
    @DisplayName("Deve mapear Solicitacao para SolicitacaoEntity com sucesso")
    void deveMapearModelParaEntityComSucesso() {

        Solicitacao model = Solicitacao.builder()
                .id(UUID.randomUUID())
                .status(TipoStatus.RECEBIDO.getDescricao())
                .criadoEm(Instant.now())
                .build();


        SolicitacaoEntity entity = SolicitacaoMapper.toEntity(model);


        assertNotNull(entity);
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getStatus(), entity.getStatus().getDescricao());
    }

    @Test
    @DisplayName("Deve mapear SolicitacaoEntity para Solicitacao com sucesso")
    void deveMapearEntityParaModelComSucesso() {

        SolicitacaoEntity entity = SolicitacaoEntity.builder()
                .id(UUID.randomUUID())
                .status(TipoStatus.RECEBIDO)
                .criadoEm(Instant.now())
                .build();


        Solicitacao model = SolicitacaoMapper.toModel(entity);


        assertNotNull(model);
        assertEquals(entity.getId(), model.getId());
        assertEquals(TipoStatus.RECEBIDO.getDescricao(), model.getStatus());
    }

    @Test
    @DisplayName("Deve mapear Solicitacao para SolicitacaoResponse com sucesso")
    void deveMapearModelParaResponseComSucesso() {

        Solicitacao model = Solicitacao.builder()
                .id(UUID.randomUUID())
                .clienteId(UUID.randomUUID())
                .status(TipoStatus.RECEBIDO.getDescricao())
                .build();


        SolicitacaoResponse response = SolicitacaoMapper.toResponse(model);


        assertNotNull(response);
        assertEquals(model.getId(), response.getId());
        assertEquals(model.getClienteId(), response.getClienteId());
    }
}