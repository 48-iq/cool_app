package ru.kuchko.cool_app.dto.user;

import lombok.*;
import ru.kuchko.cool_app.dto.city.CityDto;
import ru.kuchko.cool_app.entities.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    private Integer id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private CityDto city;
    private Integer icon;

    public static UserDto from(UserEntity user) {
        CityDto city = null;
        if (user.getCity() != null)
            city = CityDto.from(user.getCity());
        Integer icon = null;
        if (user.getIcon() != null)
            icon = user.getIcon().getId();
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .city(city)
                .icon(icon)
                .build();
    }
}
