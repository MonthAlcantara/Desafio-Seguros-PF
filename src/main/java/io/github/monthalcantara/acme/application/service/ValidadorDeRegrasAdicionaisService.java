package io.github.monthalcantara.acme.application.service;

import io.github.monthalcantara.acme.domain.enums.ClassificacaoRisco;
import io.github.monthalcantara.acme.domain.enums.TipoCategoria;
import io.github.monthalcantara.acme.domain.enums.TipoStatus;
import io.github.monthalcantara.acme.domain.model.Solicitacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidadorDeRegrasAdicionaisService {

    public TipoStatus validar(final Solicitacao solicitacao, final String classificacaoFraude) {
        log.info("[Validador] Iniciando validação adicional para solicitação ID: {}", solicitacao.getId());
        
        final ClassificacaoRisco classificacao = ClassificacaoRisco.fromString(classificacaoFraude);
        final TipoCategoria categoria = TipoCategoria.fromDescricao(solicitacao.getCategoria());
        final BigDecimal capitalSegurado = solicitacao.getValorSegurado();

        if (categoria == null) {
            log.warn("[Validador] Categoria '{}' inválida para a solicitação ID: {}. Rejeitando.", solicitacao.getCategoria(), solicitacao.getId());
            return TipoStatus.REJEITADO;
        }

        final TipoStatus novoStatus = classificacao.validarCapitalSegurado(categoria, capitalSegurado);
        
        if (novoStatus == TipoStatus.REJEITADO) {
            log.warn("[Validador] Validação falhou para a solicitação ID: {}. Classificação: {}, Categoria: {}, Capital Segurado: {}. Status: {}", solicitacao.getId(), classificacaoFraude, categoria.getDescricao(), capitalSegurado, novoStatus.getDescricao());
        } else {
            log.info("[Validador] Validação aprovada para a solicitação ID: {}. Status: {}", solicitacao.getId(), novoStatus.getDescricao());
        }

        return novoStatus;
    }
}