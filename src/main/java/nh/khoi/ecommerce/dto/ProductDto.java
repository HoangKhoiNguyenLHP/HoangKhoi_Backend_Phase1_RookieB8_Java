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
    private double price;
    private List<String> images;
    private Boolean isFeatured = false;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
