package com.WI.WIGOLDFISH.controllers;

import com.WI.WIGOLDFISH.reqrsp.AuthenticationRequest;
import com.WI.WIGOLDFISH.reqrsp.AuthenticationResponse;
import com.WI.WIGOLDFISH.reqrsp.RegisterRequest;
import com.WI.WIGOLDFISH.services.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private  final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }



    @PostMapping("/user")
    public ResponseEntity<?> currentUserName(Principal principal) {
        if (principal == null) {
            // return that the user is not authenticated
            return ResponseEntity.status(401).body("User not authenticated");
        }
        return ResponseEntity.ok(authenticationService.getUser(principal.getName()));
    }
}
