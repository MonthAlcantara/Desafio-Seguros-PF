package io.github.monthalcantara.acme.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ClassificacaoFraude {
    HIGH_RISK("HIGH_RISK", TipoStatus.REJEITADO),
    MEDIUM_RISK("MEDIUM_RISK", TipoStatus.PENDENTE),
    LOW_RISK("LOW_RISK", TipoStatus.VALIDADO),
    UNKNOWN("UNKNOWN", TipoStatus.PENDENTE);

    private final String descricao;
    private final TipoStatus status;

    ClassificacaoFraude(String descricao, TipoStatus status) {
        this.descricao = descricao;
        this.status = status;
    }

    public TipoStatus getStatus() {
        return this.status;
    }

    public static ClassificacaoFraude fromDescricao(String descricao) {
        return Arrays.stream(values())
                .filter(c -> c.descricao.equalsIgnoreCase(descricao))
                .findFirst()
                .orElse(UNKNOWN);
    }
}