package io.github.monthalcantara.acme.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoMovimentacao {
    private String status;
    private Instant dataMovimentacao;

}