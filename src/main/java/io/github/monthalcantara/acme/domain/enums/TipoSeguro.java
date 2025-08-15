package io.github.monthalcantara.acme.domain.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TipoSeguro {
    VIDA("Seguro de Vida"),
    AUTO("Seguro de Automóvel"),
    RESIDENCIAL("Seguro Residencial"),
    OUTROS("Outros Seguros");

    private final String descricao;

    private static final Map<String, TipoSeguro> DESCRICAO_MAP = new HashMap<>();

    static {
        for (final TipoSeguro seguro : TipoSeguro.values()) {
            DESCRICAO_MAP.put(seguro.descricao.toLowerCase(), seguro);
        }
    }

    TipoSeguro(final String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static boolean isDescricaoValida(final String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toLowerCase());
    }

    public static TipoSeguro fromDescricao(final String descricao) {
        return descricao == null || descricao.isBlank() ? null : DESCRICAO_MAP.get(descricao.toLowerCase());
    }
}
