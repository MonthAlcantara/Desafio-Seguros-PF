package io.github.monthalcantara.acme.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoCanalVendas {
    MOBILE("mobile"),
    WHATSAPP("whatsapp"),
    WEB_SITE("web_site");

    private final String descricao;
    private static final Map<String, TipoCanalVendas> DESCRICAO_MAP = new HashMap<>();

    static {
        for (TipoCanalVendas canal : TipoCanalVendas.values()) {
            DESCRICAO_MAP.put(canal.descricao, canal);
        }
    }

    TipoCanalVendas(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static boolean isDescricaoValida(String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toLowerCase());
    }

    public static TipoCanalVendas fromDescricao(String descricao) {
        return descricao == null || descricao.isBlank() ? null : DESCRICAO_MAP.get(descricao.toLowerCase());
    }
}

