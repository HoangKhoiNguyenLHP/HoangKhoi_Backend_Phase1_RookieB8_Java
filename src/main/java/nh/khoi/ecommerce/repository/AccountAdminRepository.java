package nh.khoi.ecommerce.repository;

import nh.khoi.ecommerce.entity.AccountAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountAdminRepository extends JpaRepository<AccountAdmin, UUID>
{
    Optional<AccountAdmin> findByEmail(String email);

    Optional<AccountAdmin> findByIdAndEmailAndStatus(UUID id, String email, String active);
}
