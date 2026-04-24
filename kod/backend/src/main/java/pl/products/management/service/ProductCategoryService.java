package pl.products.management.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.products.management.model.entity.ProductCategoryEntity;
import pl.products.management.repository.ProductCategoryRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryEntity getById(UUID id) throws EntityNotFoundException{

        return productCategoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Nie istnieje kategoria produktu o identyfikatorze " + id));
    }

    public boolean existsById(UUID id){

        return productCategoryRepository.existsById(id);
    }
}
