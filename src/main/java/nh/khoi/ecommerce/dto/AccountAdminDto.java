package nh.khoi.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountAdminDto
{
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
}
