package com.WI.WIGOLDFISH.entities.user;

import com.WI.WIGOLDFISH.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoReq {
    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String profilePicture;
    @NotNull
    private Role role;
}
