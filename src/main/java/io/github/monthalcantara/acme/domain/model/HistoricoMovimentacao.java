package io.github.monthalcantara.acme.domain.model;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoMovimentacao {
    private String status;
    private Instant dataMovimentacao;

}