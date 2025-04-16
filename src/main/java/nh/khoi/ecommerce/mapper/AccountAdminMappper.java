package nh.khoi.ecommerce.mapper;

import nh.khoi.ecommerce.dto.AccountAdminDto;
import nh.khoi.ecommerce.entity.AccountAdmin;

public class AccountAdminMappper
{
    public static AccountAdminDto mapToAccountAdminDto(AccountAdmin accountAdmin) {
        return new AccountAdminDto(
                accountAdmin.getId(),
                accountAdmin.getFirstName(),
                accountAdmin.getLastName(),
                accountAdmin.getEmail()
        );
    }

    public static AccountAdmin mapToAccountAdmin(AccountAdminDto accountAdminDto) {
        return new AccountAdmin(
                accountAdminDto.getId(),
                accountAdminDto.getFirstName(),
                accountAdminDto.getLastName(),
                accountAdminDto.getEmail()
        );
    }
}
