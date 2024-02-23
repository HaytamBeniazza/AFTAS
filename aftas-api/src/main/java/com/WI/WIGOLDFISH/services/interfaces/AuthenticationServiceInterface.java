package com.WI.WIGOLDFISH.services.interfaces;


import com.WI.WIGOLDFISH.entities.user.UserDtoRsp;
import com.WI.WIGOLDFISH.reqrsp.AuthenticationRequest;
import com.WI.WIGOLDFISH.reqrsp.AuthenticationResponse;
import com.WI.WIGOLDFISH.reqrsp.RegisterRequest;

public interface AuthenticationServiceInterface {
    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    AuthenticationResponse register(RegisterRequest registerRequest);

    UserDtoRsp getUser(String name);
}
