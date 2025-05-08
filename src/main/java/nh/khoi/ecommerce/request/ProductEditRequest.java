package nh.khoi.ecommerce.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductEditRequest
{
    @NotBlank(message = "Product name is required!")
    private String name;
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative!")
    private Double price;
    private Boolean isFeatured;
    // private List<MultipartFile> images;
    private MultipartFile[] images; // for new uploads
    private List<String> existingImageUrls; // for images already in DB
    private Integer position;

    private List<UUID> categoryIds;
}