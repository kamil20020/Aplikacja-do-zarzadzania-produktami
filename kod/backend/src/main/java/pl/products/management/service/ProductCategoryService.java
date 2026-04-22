package pl.products.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.products.management.repository.ProductCategoryRepository;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
}
