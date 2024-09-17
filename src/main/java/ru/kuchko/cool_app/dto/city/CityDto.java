package ru.kuchko.cool_app.dto.city;

import lombok.*;
import ru.kuchko.cool_app.entities.CityEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CityDto {
    private Integer id;
    private String name;

    public static CityDto from(CityEntity city) {
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }
}
