package com.sprintflow.service.impl;

import com.sprintflow.dto.AuthResponse;
import com.sprintflow.dto.LoginRequest;
import com.sprintflow.dto.RegisterRequest;
import com.sprintflow.entity.Organization;
import com.sprintflow.entity.User;
import com.sprintflow.enums.Role;
import com.sprintflow.repository.OrganizationRepository;
import com.sprintflow.repository.UserRepository;
import com.sprintflow.security.JwtService;
import com.sprintflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email Already Exist");
        }
        Organization organization = organizationRepository
                .findByName(request.getOrganizationName())
                .orElse(null);

        if (organization == null) {

            organization = new Organization();
            organization.setName(request.getOrganizationName());

            organization = organizationRepository.save(organization);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setOrganization(organization);

        userRepository.save(user);

        String token = jwtService.generateToken(request.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);

    }
}