package io.github.monthalcantara.acme.application.usecase;

import io.github.monthalcantara.acme.domain.enums.ClassificacaoRisco;
import io.github.monthalcantara.acme.domain.enums.TipoCategoria;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidadorDeRegrasAdicionaisServiceTest {

    private final ValidadorDeRegrasAdicionaisServiceImpl service = new ValidadorDeRegrasAdicionaisServiceImpl();

    @Test
    @DisplayName("deveValidarSolicitacaoDeClienteRegularComSucesso")
    void deveValidarSolicitacaoDeClienteRegularComSucesso() {
        var solicitacao = Solicitacao.builder()
                .id(UUID.randomUUID())
                .categoria(TipoCategoria.AUTO.getDescricao())
                .valorSegurado(BigDecimal.valueOf(300_000))
                .build();

        var resultado = service.validar(solicitacao, ClassificacaoRisco.REGULAR.name());

        assertEquals(TipoStatus.VALIDADO, resultado);
    }

    @Test
    @DisplayName("deveRejeitarSolicitacaoComCapitalAcimaDoPermitido")
    void deveRejeitarSolicitacaoComCapitalAcimaDoPermitido() {
        var solicitacao = Solicitacao.builder()
                .id(UUID.randomUUID())
                .categoria(TipoCategoria.AUTO.getDescricao())
                .valorSegurado(BigDecimal.valueOf(500_000))
                .build();

        var resultado = service.validar(solicitacao, ClassificacaoRisco.REGULAR.name());

        assertEquals(TipoStatus.REJEITADO, resultado);
    }
}
