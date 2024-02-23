package com.WI.WIGOLDFISH.entities.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredential {
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
}
