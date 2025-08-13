package io.github.monthalcantara.acme.infra.persistence.repository;

import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SolicitacaoRepository extends JpaRepository<SolicitacaoEntity, UUID> {
    Optional<SolicitacaoEntity> findByChaveIdempotencia(String chaveIdempotencia);

    // retornar LIST para consultas por cliente
    List<SolicitacaoEntity> findByClienteId(UUID clienteId);
}
