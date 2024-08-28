package com.github.veljko121.gigster_search_engine.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisteredUser extends User {

    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
}
