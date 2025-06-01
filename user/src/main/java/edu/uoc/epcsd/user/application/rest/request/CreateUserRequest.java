package edu.uoc.epcsd.user.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public final class CreateUserRequest {

    @NotBlank
    private final String fullName;

    @NotBlank
    private final String email;

    @NotBlank
    private final String password;

    @NotBlank
    private final String phoneNumber;
}
