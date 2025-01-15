package br.com.mounts.order_api.infraestructure.repository;

import br.com.mounts.order_api.domain.entiy.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing Product entities
 *
 * @author Raphael Braga
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
