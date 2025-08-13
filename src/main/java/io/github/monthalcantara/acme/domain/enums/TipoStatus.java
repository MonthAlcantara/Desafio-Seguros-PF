package io.github.monthalcantara.acme.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoStatus {
    RECEBIDO("recebido"),
    VALIDADO("validado"),
    PENDENTE("pendente"),
    REJEITADO("rejeitado"),
    APROVADO("aprovado"),
    CANCELADA("cancelada");

    private final String descricao;
    private static final Map<String, TipoStatus> DESCRICAO_MAP = new HashMap<>();

    static {
        for (TipoStatus tipoStatus : TipoStatus.values()) {
            DESCRICAO_MAP.put(tipoStatus.descricao, tipoStatus);
        }
    }

    TipoStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static boolean isDescricaoValida(String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toLowerCase());
    }

    public static TipoStatus fromDescricao(String descricao) {
        return descricao == null || descricao.isBlank() ? null : DESCRICAO_MAP.get(descricao.toLowerCase());
    }
}
