package pl.products.management.model.api.request;

public record LoginRequest(
    String username,
    String password
){}
