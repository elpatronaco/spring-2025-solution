package edu.uoc.epcsd.user.domain.exception;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(Long productId) {
        super("Product with id '" + productId + "' not found");
    }
}