package io.github.monthalcantara.acme.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoMetodoPagamento {
    CARTAO_CREDITO("CARTAO_CREDITO"),
    DEBITO_CONTA("DEBITO_CONTA"),
    BOLETO("BOLETO"),
    PIX("PIX");

    private final String descricao;
    private static final Map<String, TipoMetodoPagamento> DESCRICAO_MAP = new HashMap<>();

    static {
        for (final TipoMetodoPagamento forma : TipoMetodoPagamento.values()) {
            DESCRICAO_MAP.put(forma.descricao, forma);
        }
    }

    TipoMetodoPagamento(final String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static boolean isDescricaoValida(final String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toUpperCase());
    }

    public static TipoMetodoPagamento fromDescricao(final String descricao) {
        return descricao == null || descricao.isBlank() ? null : DESCRICAO_MAP.get(descricao.toUpperCase());
    }
}