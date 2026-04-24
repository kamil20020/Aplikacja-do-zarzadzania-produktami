package pl.products.management.model.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(

    @NotBlank(message = "Nazwa jest wymagana")
    String name,

    String description,

    @DecimalMin(value = "0", inclusive = false, message = "Cena powinna być większa od 0")
    @NotNull(message = "Cena jest wymagana")
    BigDecimal price,

    @org.hibernate.validator.constraints.UUID(
        allowEmpty = true,
        message = "Identyfikator katalogu powinien być w formacie uuid"
    )
    String categoryId
) {
}
