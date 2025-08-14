package io.github.monthalcantara.acme.api.validator;

import io.github.monthalcantara.acme.api.dto.request.SolicitacaoRequest;
import io.github.monthalcantara.acme.domain.enums.TipoCanalVendas;
import io.github.monthalcantara.acme.domain.enums.TipoCategoria;
import io.github.monthalcantara.acme.domain.enums.TipoMetodoPagamento;
import io.github.monthalcantara.acme.exception.ValidacaoNegocioException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class SolicitacaoRequestValidator {

    public void validar(final SolicitacaoRequest dto) {
        List<String> erros = new ArrayList<>();
        validar("categoria", dto.getCategoria(), TipoCategoria::isDescricaoValida, erros);
        validar("canalVenda", dto.getCanalVenda(), TipoCanalVendas::isDescricaoValida, erros);
        validar("metodoPagamento", dto.getMetodoPagamento(), TipoMetodoPagamento::isDescricaoValida, erros);

        if (!erros.isEmpty()) {
            throw new ValidacaoNegocioException(erros);
        }
    }

    private void validar(final String nomeCampo, final String valor, final Predicate<String> validador, final List<String> erros) {
        if (valor == null || !validador.test(valor)) {
            erros.add(nomeCampo + " inválido: " + valor);
        }
    }
}
