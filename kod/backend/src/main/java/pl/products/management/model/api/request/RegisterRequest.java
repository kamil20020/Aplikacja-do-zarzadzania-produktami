package pl.products.management.model.api.request;

public record RegisterRequest(
    String firstname,
    String lastname,
    String username,
    String password
) {}
