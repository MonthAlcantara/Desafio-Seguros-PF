package io.github.monthalcantara.acme.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoMetodoPagamento {
    CARTAO_CREDITO("cartao_credito"),
    DEBITO_CONTA("debito_conta"),
    BOLETO("boleto"),
    PIX("pix");

    private final String descricao;
    private static final Map<String, TipoMetodoPagamento> DESCRICAO_MAP = new HashMap<>();

    static {
        for (TipoMetodoPagamento forma : TipoMetodoPagamento.values()) {
            DESCRICAO_MAP.put(forma.descricao, forma);
        }
    }

    TipoMetodoPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static boolean isDescricaoValida(String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toLowerCase());
    }

    public static TipoMetodoPagamento fromDescricao(String descricao) {
        return descricao == null || descricao.isBlank() ? null : DESCRICAO_MAP.get(descricao.toLowerCase());
    }
}