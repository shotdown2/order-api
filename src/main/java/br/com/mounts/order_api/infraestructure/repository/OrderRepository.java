package br.com.mounts.order_api.infraestructure.repository;

import br.com.mounts.order_api.domain.entiy.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Order entities
 *
 * @author Raphael Braga
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    Optional<OrderEntity> findByExternalOrderId(String externalOrderId);
}
