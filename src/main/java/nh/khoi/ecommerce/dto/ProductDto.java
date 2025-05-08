package nh.khoi.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto
{
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private List<String> images;
    private Boolean isFeatured = false;
    private Integer position;
    private Integer stock = 0;
    private Boolean deleted = false;
    private String slug;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    private List<UUID> categoryIds;
}
