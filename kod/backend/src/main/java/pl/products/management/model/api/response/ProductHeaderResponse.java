package pl.products.management.model.api.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductHeaderResponse(
    UUID id,
    String name,
    BigDecimal price
) {
}
