package nh.khoi.ecommerce.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest
{
    @NotBlank(message = "Product name is required!")
    private String name;
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative!")
    private Double price;
    private List<MultipartFile> images;
    private Boolean isFeatured = false;
    private Integer position;

    private List<UUID> categoryIds;
}
