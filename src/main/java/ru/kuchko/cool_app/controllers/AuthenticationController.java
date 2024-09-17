package ru.kuchko.cool_app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.kuchko.cool_app.dto.auth.AuthenticationDto;
import ru.kuchko.cool_app.dto.user.UserCreateDto;
import ru.kuchko.cool_app.dto.user.UserDto;
import ru.kuchko.cool_app.entities.UserEntity;
import ru.kuchko.cool_app.exceptions.EntityNotFoundException;
import ru.kuchko.cool_app.repositories.UserRepository;
import ru.kuchko.cool_app.security.JwtService;
import ru.kuchko.cool_app.security.UserDetailsImpl;
import ru.kuchko.cool_app.security.UserDetailsServiceImpl;
import ru.kuchko.cool_app.services.UserService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationDto authDto) {
        try {
            UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                    authDto.getUsername(), authDto.getPassword());
            authenticationManager.authenticate(upat);
            Optional<UserEntity> userOptional = userRepository.findByUsername(authDto.getUsername());
            if (userOptional.isEmpty()) throw new EntityNotFoundException("user with username " +
                    authDto.getUsername() + " not found");
            String jwtToken = jwtService.generate(userOptional.get());
            return ResponseEntity.ok(jwtToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("bad credentials");
        } catch (EntityNotFoundException|UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@ModelAttribute UserCreateDto userCreateDto) {
        UserEntity user = userService.registerUser(userCreateDto);
        String jwtToken = jwtService.generate(user);
        return ResponseEntity.ok(jwtToken);
    }
}
