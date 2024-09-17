package ru.kuchko.cool_app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {
    private String username;
    private String password;
    private String role;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private Integer city;
    private MultipartFile icon;
}
