package com.WI.WIGOLDFISH.reqrsp;

import com.WI.WIGOLDFISH.enums.IndentityDocumentType;
import com.WI.WIGOLDFISH.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Data
public class    RegisterRequest {
    @NotNull
    private String username;
    @NotNull
    private String familyName;
    @NotNull
    private String password;
    @NotNull
    private LocalDate accessionDate;
    @NotNull
    private String nationality;
    @NotNull
    private String indentityNumber;
    @NotNull
    private IndentityDocumentType indentityDocumentType;
}
