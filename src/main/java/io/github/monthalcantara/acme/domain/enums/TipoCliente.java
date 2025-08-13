package io.github.monthalcantara.acme.domain.enums;


import java.util.HashMap;
import java.util.Map;

public enum TipoCliente {
    REGULAR("cliente_regular"),
    AUTO_RISCO("cliente_auto_risco"),
    PREFERENCIAL("cliente_preferencial"),
    SEM_INFORMACAO("cliente_sem_informacao");

    private final String descricao;
    private static final Map<String, TipoCliente> DESCRICAO_MAP = new HashMap<>();

    static {
        for (TipoCliente tipo : TipoCliente.values()) {
            DESCRICAO_MAP.put(tipo.descricao, tipo);
        }
    }

    TipoCliente(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static boolean isDescricaoValida(String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toLowerCase());

    }

    public static TipoCliente fromDescricao(String descricao) {
        return descricao == null || descricao.isBlank() ? null : DESCRICAO_MAP.get(descricao.toLowerCase());

    }
}
