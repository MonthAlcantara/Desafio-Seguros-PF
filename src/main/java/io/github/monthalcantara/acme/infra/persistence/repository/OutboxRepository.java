package io.github.monthalcantara.acme.infra.persistence.repository;

import io.github.monthalcantara.acme.infra.persistence.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {
}