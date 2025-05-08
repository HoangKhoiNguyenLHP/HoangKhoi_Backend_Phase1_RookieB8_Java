package nh.khoi.ecommerce.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Please enter your first name!")
    @Size(max = 50, message = "Name must be between 2 and 50 characters!")
    private String firstName;

    @NotBlank(message = "Please enter your last name!")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters!")
    private String lastName;

    @NotBlank(message = "Please enter your email!")
    @Email(message = "Email format is invalid!")
    private String email;

    @NotBlank(message = "Please enter your password!")
    @Size(min = 8, message = "Password must be at least 8 characters long!")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character!"
    )
    private String password;
}
