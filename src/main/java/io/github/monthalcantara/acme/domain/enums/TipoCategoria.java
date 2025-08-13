
package io.github.monthalcantara.acme.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoCategoria {
    VIDA("vida"),
    AUTO("auto"),
    RESIDENCIAL("residencial"),
    EMPRESARIAL("empresarial"),
    OUTROS("outros"); // Para "qualquer outro tipo de seguro";

    private final String descricao;
    private static final Map<String, TipoCategoria> DESCRICAO_MAP = new HashMap<>();

    static {
        for (TipoCategoria categoria : TipoCategoria.values()) {
            DESCRICAO_MAP.put(categoria.descricao, categoria);
        }
    }

    TipoCategoria(final String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }


    public static boolean isDescricaoValida(final String descricao) {
        return descricao != null && !descricao.isBlank() && DESCRICAO_MAP.containsKey(descricao.toLowerCase());
    }

    public static TipoCategoria fromDescricao(final String descricao) {
        return  descricao == null || descricao.isBlank() ? null : DESCRICAO_MAP.get(descricao.toLowerCase());
    }
}
