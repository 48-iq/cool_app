package ru.kuchko.cool_app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.kuchko.cool_app.dto.user.UserDto;
import ru.kuchko.cool_app.entities.UserEntity;
import ru.kuchko.cool_app.repositories.UserRepository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.duration.days}")
    private Integer durationDays;
    private final UserRepository userRepository;


    public String generate(UserEntity user) {
        Date experationDate = Date.from(ZonedDateTime.now()
                .plusDays(durationDays)
                .toInstant());
        return JWT.create()
                .withSubject("User details")
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(experationDate)
                .withIssuer("cool-app")
                .sign(Algorithm.HMAC256(secret));
    }

    public UserDetails validateAndRetrieveUserDetails(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("cool-app")
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);
        Integer userId = decodedJWT.getClaim("id").asInt();
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new JWTVerificationException("user with id " + userId + " not found");
        UserEntity user = userOptional.get();
        return new UserDetailsImpl(user);
    }
}
