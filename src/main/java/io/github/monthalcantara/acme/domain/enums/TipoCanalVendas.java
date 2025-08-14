package io.github.monthalcantara.acme.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoCanalVendas {
    MOBILE("MOBILE"),
    WHATSAPP("WHATSAPP"),
    WEB_SITE("WEB_SITE");

    private final String descricao;
    private static final Map<String, TipoCanalVendas> DESCRICAO_MAP = new HashMap<>();

    static {
        for (final TipoCanalVendas canal : TipoCanalVendas.values()) {
            DESCRICAO_MAP.put(canal.descricao, canal);
        }
    }

    TipoCanalVendas(final String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static boolean isDescricaoValida(final String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toUpperCase());
    }

    public static TipoCanalVendas fromDescricao(final String descricao) {
        return descricao == null || descricao.isBlank() ? null : DESCRICAO_MAP.get(descricao.toUpperCase());
    }
}

