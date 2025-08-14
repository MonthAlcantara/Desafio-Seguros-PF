package io.github.monthalcantara.acme.domain.enums;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum ClassificacaoFraude {
    HIGH_RISK("HIGH_RISK"),
    REGULAR("REGULAR"),
    PREFERENTIAL("PREFERENTIAL"),
    NO_INFO("NO_INFO"),
    UNKNOWN("UNKNOWN");

    private final String descricao;
    private static final Map<String, ClassificacaoFraude> DESCRICAO_MAP = new HashMap<>();

    static {
        for (final ClassificacaoFraude classificacao : ClassificacaoFraude.values()) {
            DESCRICAO_MAP.put(classificacao.descricao, classificacao);
        }
    }

    ClassificacaoFraude(final String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static boolean isDescricaoValida(final String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toUpperCase(Locale.ROOT));
    }

    public static ClassificacaoFraude fromDescricao(final String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return UNKNOWN;
        }
        return DESCRICAO_MAP.getOrDefault(descricao.toUpperCase(Locale.ROOT), UNKNOWN);
    }
}