package com.WI.WIGOLDFISH.services.impl;


import com.WI.WIGOLDFISH.configs.JwtService;
import com.WI.WIGOLDFISH.entities.member.DBUser;
import com.WI.WIGOLDFISH.entities.user.UserDtoRsp;
import com.WI.WIGOLDFISH.enums.Role;
import com.WI.WIGOLDFISH.exceptions.NotFoundEx;
import com.WI.WIGOLDFISH.repositories.DBUserRepository;
import com.WI.WIGOLDFISH.reqrsp.AuthenticationRequest;
import com.WI.WIGOLDFISH.reqrsp.AuthenticationResponse;
import com.WI.WIGOLDFISH.reqrsp.RegisterRequest;
import com.WI.WIGOLDFISH.services.interfaces.AuthenticationServiceInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Service
public class AuthenticationService implements AuthenticationServiceInterface {

    private  final DBUserRepository userRepository;


    private PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final JwtService jwtService;


    private  final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        DBUser user = modelMapper.map(registerRequest, DBUser.class);
        user.setRole(Role.NONE);
        user.setAccessionDate(LocalDate.now());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    @Override
    public UserDtoRsp getUser(String name) {
        DBUser user = userRepository.findByUsername(name).orElseThrow(() -> new NotFoundEx("User not found"));
        return modelMapper.map(user, UserDtoRsp.class);
    }
}
