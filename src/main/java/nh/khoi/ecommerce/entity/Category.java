package nh.khoi.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // GenerationType.IDENTITY : auto increment
    private UUID id;                                // GenerationType.UUID : generate random id

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent")
    private UUID parent; // Self-referencing parent ID; use @ManyToOne if you want object mapping

    @Column(name = "description")
    private String description;

    @Column(name = "position")
    private Integer position;

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

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name='" + name + '\'' + ", parent=" + parent + ", description='" + description + '\'' + ", position=" + position + ", deleted=" + deleted + ", slug='" + slug + '\'' + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + '}';
    }
}
