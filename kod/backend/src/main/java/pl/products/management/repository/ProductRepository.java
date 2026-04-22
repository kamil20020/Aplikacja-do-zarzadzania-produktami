package pl.products.management.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.products.management.model.entity.ProductEntity;

import java.util.UUID;

@Repository
public interface ProductRepository extends
    CrudRepository<ProductEntity, UUID>,
    ListPagingAndSortingRepository<ProductEntity, UUID> {
}
