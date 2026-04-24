package pl.products.management.model.api.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    String username,

    @NotBlank(message = "Hasło jest wymagane")
    String password
){}
