package nh.khoi.ecommerce.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductEditRequest
{
    private String name;
    private String description;
    private Double price;
    private Boolean isFeatured;
    // private List<MultipartFile> images;
    private MultipartFile[] images;
}