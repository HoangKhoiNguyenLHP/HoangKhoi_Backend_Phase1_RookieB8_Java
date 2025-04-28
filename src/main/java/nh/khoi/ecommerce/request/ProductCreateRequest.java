package nh.khoi.ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest
{
    private String name;
    private String description;
    private Double price;
    private List<MultipartFile> images;
    private Boolean isFeatured = false;
}
