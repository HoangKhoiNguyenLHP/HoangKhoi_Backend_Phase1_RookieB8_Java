package nh.khoi.ecommerce.repository;

import nh.khoi.ecommerce.entity.AccountAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountAdminRepository extends JpaRepository<AccountAdmin, UUID>
{}
