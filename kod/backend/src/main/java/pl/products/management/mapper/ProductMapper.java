package pl.products.management.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.products.management.model.api.request.CreateProductRequest;
import pl.products.management.model.api.request.UpdateProductRequest;
import pl.products.management.model.api.response.ProductDetailsResponse;
import pl.products.management.model.api.response.ProductHeaderResponse;
import pl.products.management.model.entity.ProductEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {ProductCategoryMapper.class})
public interface ProductMapper {

    @Mapping(source = "categoryId", target = "productCategory.id")
    ProductEntity map(CreateProductRequest request);

    @Mapping(source = "categoryId", target = "productCategory.id")
    ProductEntity map(UpdateProductRequest request);

    ProductDetailsResponse map(ProductEntity product);
    ProductHeaderResponse mapToHeader(ProductEntity product);
}
