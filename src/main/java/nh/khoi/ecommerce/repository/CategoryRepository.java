package nh.khoi.ecommerce.repository;

import nh.khoi.ecommerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>
{
    Page<Category> findAllByDeletedFalse(Pageable pageable);
    List<Category> findAllByDeletedFalse();
    Optional<Category> findTopByOrderByPositionDesc();
    Boolean existsBySlug(String slug);
    Page<Category> findBySlugContainingIgnoreCaseAndDeletedFalse(String slug, Pageable pageable);

    Optional<Category> findBySlugAndDeletedFalse(String slug);
    List<Category> findByParentAndDeletedFalse(UUID parentId);
}
