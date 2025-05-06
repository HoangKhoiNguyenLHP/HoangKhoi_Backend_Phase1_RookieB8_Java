package nh.khoi.ecommerce.repository;

import nh.khoi.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>
{
    Page<Product> findAllByDeletedFalse(Pageable pageable);
    Optional<Product> findTopByOrderByPositionDesc();
    Boolean existsBySlug(String slug);
}
