package nh.khoi.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts_admin")
public class AccountAdmin
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // GenerationType.IDENTITY : auto increment
    private UUID id;                                // GenerationType.UUID : generate random id

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    // @Column(name = "full_name", nullable = false)
    // private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private String status;

    @Override
    public String toString() {
        return "AccountAdmin{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", password='" + password + '\'' + ", status='" + status + '\'' + '}';
    }
}
