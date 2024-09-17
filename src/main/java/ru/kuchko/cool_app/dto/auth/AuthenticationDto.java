package ru.kuchko.cool_app.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthenticationDto {
    private String username;
    private String password;
}
