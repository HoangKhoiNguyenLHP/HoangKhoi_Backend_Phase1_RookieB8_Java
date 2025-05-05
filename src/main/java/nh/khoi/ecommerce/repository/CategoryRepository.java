package nh.khoi.ecommerce.repository;

import nh.khoi.ecommerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>
{
    Page<Category> findAllByDeletedFalse(Pageable pageable);
}
