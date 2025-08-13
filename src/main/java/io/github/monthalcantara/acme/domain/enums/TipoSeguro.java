package io.github.monthalcantara.acme.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoSeguro {
    VIDA("Seguro de Vida"),
    AUTO("Seguro de Automóvel"),
    RESIDENCIAL("Seguro Residencial"),
    OUTROS("Outros Seguros");

    private final String descricao;
}
