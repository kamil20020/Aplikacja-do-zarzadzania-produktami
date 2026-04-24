package pl.products.management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.products.management.mapper.ProductMapper;
import pl.products.management.model.api.request.CreateProductRequest;
import pl.products.management.model.api.request.UpdateProductRequest;
import pl.products.management.model.api.response.ProductDetailsResponse;
import pl.products.management.model.api.response.ProductHeaderResponse;
import pl.products.management.model.entity.ProductEntity;
import pl.products.management.service.ProductService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController{

    private final ProductService productService;

    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsResponse> getDetailsById(@PathVariable("id") UUID id){

       ProductEntity gotProduct = productService.getById(id);
       ProductDetailsResponse gotProductDetails = productMapper.map(gotProduct);

       return ResponseEntity.ok(gotProductDetails);
    }

    @GetMapping
    public ResponseEntity<Page<ProductHeaderResponse>> getHeadersPage(@ParameterObject Pageable pageable){

        Page<ProductEntity> gotProductsPage = productService.findPage(pageable);
        Page<ProductHeaderResponse> gotProductsHeadersPage = gotProductsPage.map(product -> productMapper.mapToHeader(product));

        return ResponseEntity.ok(gotProductsHeadersPage);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<ProductHeaderResponse>> getCategoryHeadersPage(
        @PathVariable("categoryName") String categoryName,
        @ParameterObject Pageable pageable
    ){
        Page<ProductEntity> gotProductsPage = productService.findPageByCategory(pageable, categoryName);
        Page<ProductHeaderResponse> gotProductsHeadersPage = gotProductsPage.map(product -> productMapper.mapToHeader(product));

        return ResponseEntity.ok(gotProductsHeadersPage);
    }

    @PostMapping
    public ResponseEntity<ProductDetailsResponse> create(@RequestBody @Valid CreateProductRequest request){

        ProductEntity toCreateProductData = productMapper.map(request);

        ProductEntity createdProduct = productService.create(toCreateProductData);
        ProductDetailsResponse response = productMapper.map(createdProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailsResponse> updateById(
        @PathVariable("id") UUID id,
        @RequestBody @Valid UpdateProductRequest request
    ){
        ProductEntity toChangeData = productMapper.map(request);

        ProductEntity changedProduct = productService.updateById(id, toChangeData);
        ProductDetailsResponse changedProductDetails = productMapper.map(changedProduct);

        return ResponseEntity.ok(changedProductDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByid(@PathVariable("id") UUID id){

        productService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
