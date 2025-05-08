package nh.khoi.ecommerce.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountAdminDto
{
    private UUID id;
    private String firstName;
    private String lastName;
    // private String fullName;
    private String email;
    private String password;
    private String status;
}
