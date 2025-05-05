package nh.khoi.ecommerce.repository;

import nh.khoi.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>
{
    Page<Product> findAllByDeletedFalse(Pageable pageable);
}
