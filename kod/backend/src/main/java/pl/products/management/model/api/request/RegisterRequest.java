package pl.products.management.model.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "Imię jest wymagane")
    String firstname,

    @NotBlank(message = "Nazwisko jest wymagane")
    String lastname,

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    String username,

    @Size(min = 8, message = "Hasło powinno mieć co najmniej 8 znaków")
    @NotBlank(message = "Hasło jest wymagane")
    String password
) {}
