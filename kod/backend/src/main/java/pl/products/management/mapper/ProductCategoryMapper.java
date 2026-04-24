package pl.products.management.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.products.management.model.api.response.ProductCategoryDetails;
import pl.products.management.model.entity.ProductCategoryEntity;
import pl.products.management.model.entity.ProductEntity;

import java.util.UUID;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductCategoryMapper {

    ProductCategoryDetails map(ProductCategoryEntity productCategory);
}
