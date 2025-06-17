package com.WI.WIGOLDFISH.services.impl;


import com.WI.WIGOLDFISH.configs.JwtService;
import com.WI.WIGOLDFISH.entities.member.DBUser;
import com.WI.WIGOLDFISH.entities.user.UserDtoRsp;
import com.WI.WIGOLDFISH.enums.IndentityDocumentType;
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

import jakarta.annotation.PostConstruct;
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

    /**
     * Create default manager account if none exists
     */
    @PostConstruct
    public void createDefaultManager() {
        // Check if any manager exists
        if (userRepository.findAllByRole(Role.MANAGER).isEmpty()) {
            DBUser manager = new DBUser();
            manager.setUsername("admin");
            manager.setFamilyName("Administrator");
            manager.setPassword(passwordEncoder.encode("admin123"));
            manager.setRole(Role.MANAGER);
            manager.setAccessionDate(LocalDate.now());
            manager.setNationality("System");
            manager.setIndentityDocumentType(IndentityDocumentType.CIN);
            manager.setIndentityNumber("ADMIN001");
            
            userRepository.save(manager);
            System.out.println("🎣 Default AFTAS Manager created:");
            System.out.println("   Username: admin");
            System.out.println("   Password: admin123");
            System.out.println("   Role: MANAGER");
        }
    }

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
        // Check if username already exists
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        DBUser user = modelMapper.map(registerRequest, DBUser.class);
        user.setRole(Role.NONE); // New users start with NONE role and need approval
        user.setAccessionDate(registerRequest.getAccessionDate());
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
