package nh.khoi.ecommerce.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {
    private int currentPage;
    private int totalPages;
    private long totalRecords;
    private int skip;
    private List<T> data;
}