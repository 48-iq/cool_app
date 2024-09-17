package ru.kuchko.cool_app.dto.city;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CityUploadDto {
    private String name;
}
