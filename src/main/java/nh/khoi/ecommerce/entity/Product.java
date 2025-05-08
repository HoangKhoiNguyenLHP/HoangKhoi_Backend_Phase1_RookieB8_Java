package nh.khoi.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // GenerationType.IDENTITY : auto increment
    private UUID id;                                // GenerationType.UUID : generate random id

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "images")
    private List<String> images;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "position")
    private Integer position;

    @Column(name = "stock")
    private Integer stock = 0;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "created_on")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name = "last_updated_on")
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToMany
    @JoinTable(
            name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", price=" + price + ", images=" + images + ", isFeatured=" + isFeatured + ", position=" + position + ", stock=" + stock + ", deleted=" + deleted + ", slug='" + slug + '\'' + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + '}';
    }
}
