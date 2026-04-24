package pl.products.management.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.products.management.model.api.request.CreateProductRequest;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductValidationTest {

    private static Validator validator;

    @BeforeAll
    private static void setup() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void createProductRequestShouldPass(){

        CreateProductRequest request = new CreateProductRequest(
            "nazwa",
            "opis",
            new BigDecimal("22.28"),
            UUID.randomUUID().toString()
        );

        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        assertEquals(0, violations.size());
    }

    @Test
    public void createProductRequestShouldPassWithoutDescription(){

        CreateProductRequest request = new CreateProductRequest(
                "nazwa",
                "",
                new BigDecimal("22.28"),
                UUID.randomUUID().toString()
        );

        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        assertEquals(0, violations.size());
    }

    @Test
    public void createProductRequestShouldPassWithoutCategoryId(){

        CreateProductRequest request = new CreateProductRequest(
            "nazwa",
            "opis",
            new BigDecimal("22.28"),
            null
        );

        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        assertEquals(0, violations.size());
    }

    @Test
    public void createProductRequestShouldFailWithBlankName(){

        CreateProductRequest request = new CreateProductRequest(
            "",
            "opis",
            new BigDecimal("22.28"),
            UUID.randomUUID().toString()
        );

        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
    }

    @Test
    public void createProductRequestShouldFailWithNoPrice(){

        CreateProductRequest request = new CreateProductRequest(
                "nazwa",
                "opis",
                null,
                UUID.randomUUID().toString()
        );

        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
    }
}