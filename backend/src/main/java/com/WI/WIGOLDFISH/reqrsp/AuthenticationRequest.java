package com.WI.WIGOLDFISH.reqrsp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class AuthenticationRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
