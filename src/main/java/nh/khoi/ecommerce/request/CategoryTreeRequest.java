package nh.khoi.ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTreeRequest
{
    private UUID id;
    private String name;
    private String slug;
    private List<CategoryTreeRequest> children = new ArrayList<>();
}
