package com.prabhas.ecommerce.beans;

import com.prabhas.ecommerce.models.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A class representing a registration request for user account creation.
 * This class encapsulates the essential information required for user registration,
 * including username, password, and email address.
 *
 * Core functionality:
 * - Stores and provides access to user registration data
 * - Validates that all required fields are present (via @NotNull annotations)
 *
 * Usage example:
 * <pre>
 * RegistrationRequest request = new RegistrationRequest();
 * request.setUserName("john_doe");
 * request.setPassword("securePassword123");
 * request.setEmail("john@example.com");
 * </pre>
 *
 * Constructor parameters:
 * - This class uses a no-arg constructor with setters for property initialization
 *
 * Notes:
 * - All fields are marked with @NotNull and must be provided before processing
 * - Password should be properly hashed before storage in any persistent storage
 * - Email format validation should be performed before processing
 *
 * @author Prabhas
 * @version 1.0
 */
@Getter
@Setter
public class RegistrationRequest {
    @NotNull
    private String UserName;

    @NotNull
    private String password;

    @NotNull
    private String email;



}
