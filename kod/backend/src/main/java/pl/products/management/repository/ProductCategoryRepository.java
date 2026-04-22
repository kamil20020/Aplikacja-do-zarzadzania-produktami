package pl.products.management.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.products.management.model.entity.ProductCategoryEntity;

import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends
    CrudRepository<ProductCategoryEntity, UUID>,
    ListPagingAndSortingRepository<ProductCategoryEntity, UUID> {
}
