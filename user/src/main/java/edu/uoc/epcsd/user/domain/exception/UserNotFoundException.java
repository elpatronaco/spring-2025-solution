package edu.uoc.epcsd.user.domain.exception;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(Long userId) {
        super("User with id '" + userId + "' not found");
    }
}
