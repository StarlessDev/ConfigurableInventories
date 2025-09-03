package dev.starless.inventories.errors;

public record StaticError(String message, boolean forceClose) implements GenericError {
}
