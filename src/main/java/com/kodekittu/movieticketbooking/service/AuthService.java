package com.kodekittu.movieticketbooking.service;

import com.kodekittu.movieticketbooking.constant.SecurityConstants;
import com.kodekittu.movieticketbooking.dto.request.LoginRequest;
import com.kodekittu.movieticketbooking.dto.request.RegisterRequest;
import com.kodekittu.movieticketbooking.dto.response.AuthResponse;
import com.kodekittu.movieticketbooking.entity.Role;
import com.kodekittu.movieticketbooking.entity.User;
import com.kodekittu.movieticketbooking.exception.ConflictException;
import com.kodekittu.movieticketbooking.exception.ResourceNotFoundException;
import com.kodekittu.movieticketbooking.mapper.UserMapper;
import com.kodekittu.movieticketbooking.repository.RoleRepository;
import com.kodekittu.movieticketbooking.repository.UserRepository;
import com.kodekittu.movieticketbooking.security.JwtProperties;
import com.kodekittu.movieticketbooking.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final UserMapper userMapper;
    private final Clock clock;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ConflictException("Email is already registered");
        }
        Role customerRole = roleRepository.findByName(SecurityConstants.ROLE_CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("CUSTOMER role is not configured"));
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setPhone(request.phone());
        user.getRoles().add(customerRole);
        User saved = userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        return authResponse(authentication, saved);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return authResponse(authentication, user);
    }

    private AuthResponse authResponse(Authentication authentication, User user) {
        return new AuthResponse(
                jwtTokenProvider.generateAccessToken(authentication),
                "Bearer",
                clock.instant().plus(jwtProperties.accessTokenExpirationMinutes(), ChronoUnit.MINUTES),
                userMapper.toResponse(user));
    }
}
