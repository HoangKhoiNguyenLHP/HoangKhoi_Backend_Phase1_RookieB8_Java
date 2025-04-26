package nh.khoi.ecommerce.mapper;

import nh.khoi.ecommerce.dto.AccountAdminDto;
import nh.khoi.ecommerce.entity.AccountAdmin;

public class AccountAdminMapper
{
    public static AccountAdminDto mapToAccountAdminDto(AccountAdmin accountAdmin) {
        return new AccountAdminDto(
            accountAdmin.getId(),
            accountAdmin.getFirstName(),
            accountAdmin.getLastName(),
            accountAdmin.getEmail(),
            accountAdmin.getPassword(),
            accountAdmin.getStatus()
        );
    }

    public static AccountAdmin mapToAccountAdmin(AccountAdminDto accountAdminDto) {
        return new AccountAdmin(
            accountAdminDto.getId(),
            accountAdminDto.getFirstName(),
            accountAdminDto.getLastName(),
            accountAdminDto.getEmail(),
            accountAdminDto.getPassword(),
            accountAdminDto.getStatus()
        );
    }
}
