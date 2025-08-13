package io.github.monthalcantara.acme.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "assistencia")
public class AssistenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id")
    private SolicitacaoEntity solicitacaoEntity;

    public AssistenciaEntity(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public SolicitacaoEntity getSolicitacaoEntity() { return solicitacaoEntity; }
    public void setSolicitacaoEntity(SolicitacaoEntity solicitacaoEntity) { this.solicitacaoEntity = solicitacaoEntity; }
}
