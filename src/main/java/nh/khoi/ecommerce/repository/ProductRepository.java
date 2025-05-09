package nh.khoi.ecommerce.repository;

import nh.khoi.ecommerce.entity.Category;
import nh.khoi.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>
{
    Page<Product> findAllByDeletedFalse(Pageable pageable);
    List<Product> findAllByDeletedFalse();
    Optional<Product> findTopByOrderByPositionDesc();
    Boolean existsBySlug(String slug);
    Page<Product> findBySlugContainingIgnoreCaseAndDeletedFalse(String slug, Pageable pageable);
    List<Product> findTop6ByFeaturedAndDeletedOrderByPositionDesc(boolean isFeatured, boolean deleted);
}
