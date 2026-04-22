package pl.products.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.products.management.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
}
