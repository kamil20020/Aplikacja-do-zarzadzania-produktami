package pl.products.management.model.api.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDetailsResponse(
     UUID id,
     String name,
     BigDecimal price,
     String description,
     ProductCategoryDetails productCategory
) {
}
