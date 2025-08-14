package io.github.monthalcantara.acme.infra.persistence.repository;

import io.github.monthalcantara.acme.infra.persistence.entity.SolicitacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SolicitacaoRepository extends JpaRepository<SolicitacaoEntity, UUID> {
    Optional<SolicitacaoEntity> findByChaveIdempotencia(final String chaveIdempotencia);

    List<SolicitacaoEntity> findByClienteId(final UUID clienteId);
}
