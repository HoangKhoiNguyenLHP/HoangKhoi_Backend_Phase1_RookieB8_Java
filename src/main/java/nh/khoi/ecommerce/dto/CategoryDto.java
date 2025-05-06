package nh.khoi.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto
{
    private UUID id;

    @NotBlank(message = "Category name is required!")
    private String name;
    private UUID parent;
    private String description;
    private Integer position;
    private Boolean deleted = false;
    private String slug;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
