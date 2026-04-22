package com.prabhas.ecommerce.beans;

import com.prabhas.ecommerce.models.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistrationRequest {
    @NotNull
    private String UserName;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    @NotEmpty
    private List<Address> address;


}
