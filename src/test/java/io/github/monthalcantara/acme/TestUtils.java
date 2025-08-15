package io.github.monthalcantara.acme;

import io.github.monthalcantara.acme.domain.enums.TipoCanalVendas;
import io.github.monthalcantara.acme.domain.enums.TipoCategoria;
import io.github.monthalcantara.acme.domain.enums.TipoMetodoPagamento;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

public final class TestUtils {

    private TestUtils() {}

    public static String lerPayloadDoArquivo(String nomeArquivo) throws Exception {
        return new String(Files.readAllBytes(Paths.get("src/test/resources/payloads", nomeArquivo)));
    }

    public static SolicitacaoEntity criarSolicitacaoEntity(UUID clienteId) {
        final var solicitacao = new SolicitacaoEntity();
        solicitacao.setId(UUID.randomUUID());
        solicitacao.setClienteId(clienteId);
        solicitacao.setProdutoId(UUID.randomUUID());
        solicitacao.setCategoria(TipoCategoria.AUTO);
        solicitacao.setCanalVenda(TipoCanalVendas.MOBILE);
        solicitacao.setMetodoPagamento(TipoMetodoPagamento.CARTAO_CREDITO);
        solicitacao.setTotalPremioMensal(BigDecimal.valueOf(150.75));
        solicitacao.setValorSegurado(BigDecimal.valueOf(50000.00));
        solicitacao.setStatus(TipoStatus.RECEBIDO);
        solicitacao.setCriadoEm(Instant.now());
        return solicitacao;
    }
}