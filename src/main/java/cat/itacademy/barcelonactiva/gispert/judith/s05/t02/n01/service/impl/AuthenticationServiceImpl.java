package cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.service.impl;

import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.domain.Role;
import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.domain.User;
import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.dto.response.JwtAuthenticationResponse;
import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.dto.request.SignInRequest;
import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.dto.request.SignUpRequest;
import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.exceptions.EmailAlreadyExistException;
import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.repository.UserRepository;
import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.service.AuthenticationService;
import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        if (request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("User email and, or password cannot be null");
        }

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistException("Email is already registered:" + user.getEmail());
                });

        User user = User.builder().firstname(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
                .build();
        userRepository.save(user);

        String jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
