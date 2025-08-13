package io.github.monthalcantara.acme.domain.model;

import lombok.*;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cobertura {

    private String nome;
    private BigDecimal valor;

}
