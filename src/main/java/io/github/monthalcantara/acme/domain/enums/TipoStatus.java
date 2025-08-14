package io.github.monthalcantara.acme.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoStatus {
    RECEBIDO("RECEBIDO"),
    VALIDADO("VALIDADO"),
    PENDENTE("PENDENTE"),
    REJEITADO("REJEITADO"),
    APROVADO("APROVADO"),
    CANCELADA("CANCELADA");

    private final String descricao;
    private static final Map<String, TipoStatus> DESCRICAO_MAP = new HashMap<>();

    static {
        for (final TipoStatus tipoStatus : TipoStatus.values()) {
            DESCRICAO_MAP.put(tipoStatus.descricao, tipoStatus);
        }
    }

    TipoStatus(final String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static boolean isDescricaoValida(final String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toUpperCase());
    }

    public static TipoStatus fromDescricao(final String descricao) {
        return descricao == null || descricao.isBlank() ? null : DESCRICAO_MAP.get(descricao.toUpperCase());
    }
}
