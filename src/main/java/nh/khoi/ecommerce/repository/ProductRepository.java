package nh.khoi.ecommerce.repository;

import nh.khoi.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>
{}
