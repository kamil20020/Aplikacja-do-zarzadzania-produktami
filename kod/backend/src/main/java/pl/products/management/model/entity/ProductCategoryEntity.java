package pl.products.management.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCTS_CATEGORIES")
public class ProductCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Override
    public String toString() {
        return "ProductCategoryEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
