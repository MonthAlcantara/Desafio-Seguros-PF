package io.github.monthalcantara.acme.domain.enums;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

public enum ClassificacaoRisco {
    HIGH_RISK(Map.of(
            TipoCategoria.AUTO, new BigDecimal("250000.00"),
            TipoCategoria.RESIDENCIAL, new BigDecimal("150000.00"),
            TipoCategoria.OUTROS, new BigDecimal("125000.00")
    )),
    REGULAR(Map.of(
            TipoCategoria.VIDA, new BigDecimal("500000.00"),
            TipoCategoria.RESIDENCIAL, new BigDecimal("500000.00"),
            TipoCategoria.AUTO, new BigDecimal("350000.00"),
            TipoCategoria.OUTROS, new BigDecimal("255000.00")
    )),
    PREFERENTIAL(Map.of(
            TipoCategoria.VIDA, new BigDecimal("800000.00"),
            TipoCategoria.AUTO, new BigDecimal("450000.00"),
            TipoCategoria.RESIDENCIAL, new BigDecimal("450000.00"),
            TipoCategoria.OUTROS, new BigDecimal("375000.00")
    )),
    NO_INFO(Map.of(
            TipoCategoria.VIDA, new BigDecimal("200000.00"),
            TipoCategoria.RESIDENCIAL, new BigDecimal("200000.00"),
            TipoCategoria.AUTO, new BigDecimal("75000.00"),
            TipoCategoria.OUTROS, new BigDecimal("55000.00")
    ));

    private final Map<TipoCategoria, BigDecimal> limitesPorCategoria;

    ClassificacaoRisco(final Map<TipoCategoria, BigDecimal> limites) {
        this.limitesPorCategoria = limites;
    }

    public TipoStatus validarCapitalSegurado(final TipoCategoria categoria, final BigDecimal capitalSegurado) {
        BigDecimal limite = limitesPorCategoria.getOrDefault(categoria, limitesPorCategoria.get(TipoCategoria.OUTROS));
        if (capitalSegurado != null && capitalSegurado.compareTo(limite) <= 0) {
            return TipoStatus.VALIDADO;
        } else {
            return TipoStatus.REJEITADO;
        }
    }

    public static ClassificacaoRisco fromString(final String classificacao) {
        if (classificacao == null) return NO_INFO;
        try {
            return ClassificacaoRisco.valueOf(classificacao.trim().replace(" ", "_").toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            return NO_INFO;
        }
    }
}