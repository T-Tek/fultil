package com.fultil.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "First name is required")
    @NotNull(message = "First name cannot be null")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @NotBlank(message = "Email is required")
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Phone Number is required")
    @NotNull(message = "Phone Number cannot be null")
    @Pattern(regexp = "^[0-9]{11}$", message = "Phone Number must be 11 digits long")
    private String phoneNumber;
}
