package io.github.monthalcantara.acme.domain.enums;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

public enum ClassificacaoRisco {
    HIGH_RISK(Map.of(
            TipoCategoria.AUTO, new BigDecimal("250000"),
            TipoCategoria.RESIDENCIAL, new BigDecimal("150000"),
            TipoCategoria.OUTROS, new BigDecimal("125000")
    )),
    REGULAR(Map.of(
            TipoCategoria.VIDA, new BigDecimal("500000"),
            TipoCategoria.RESIDENCIAL, new BigDecimal("500000"),
            TipoCategoria.AUTO, new BigDecimal("350000"),
            TipoCategoria.OUTROS, new BigDecimal("255000")
    )),
    PREFERENTIAL(Map.of(
            TipoCategoria.VIDA, new BigDecimal("800000"),
            TipoCategoria.AUTO, new BigDecimal("450000"),
            TipoCategoria.RESIDENCIAL, new BigDecimal("450000"),
            TipoCategoria.OUTROS, new BigDecimal("375000")
    )),
    NO_INFO(Map.of(
            TipoCategoria.VIDA, new BigDecimal("200000"),
            TipoCategoria.RESIDENCIAL, new BigDecimal("200000"),
            TipoCategoria.AUTO, new BigDecimal("75000"),
            TipoCategoria.OUTROS, new BigDecimal("55000")
    ));

    private final Map<TipoCategoria, BigDecimal> limitesPorCategoria;

    ClassificacaoRisco(Map<TipoCategoria, BigDecimal> limites) {
        this.limitesPorCategoria = limites;
    }

    public TipoStatus podeAprovar(TipoCategoria categoria, BigDecimal capitalSegurado) {
        BigDecimal limite = limitesPorCategoria.getOrDefault(categoria, limitesPorCategoria.get(TipoCategoria.OUTROS));
        if (capitalSegurado == null || limite == null) return TipoStatus.REJEITADO;
        return capitalSegurado.compareTo(limite) <= 0 ? TipoStatus.APROVADO : TipoStatus.REJEITADO;
    }

    public static ClassificacaoRisco fromString(String s) {
        if (s == null) return NO_INFO;
        try {
            return ClassificacaoRisco.valueOf(s.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            return NO_INFO;
        }
    }
}
