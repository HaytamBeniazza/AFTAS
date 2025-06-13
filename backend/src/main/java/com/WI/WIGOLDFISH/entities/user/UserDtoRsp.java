package com.WI.WIGOLDFISH.entities.user;

import com.WI.WIGOLDFISH.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRsp {
    private UUID id;


    private String username;

    private String email;

    private String profilePicture;

    private Role role;


}
