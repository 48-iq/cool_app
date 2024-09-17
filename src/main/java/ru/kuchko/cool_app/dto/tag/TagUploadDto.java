package ru.kuchko.cool_app.dto.tag;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TagUploadDto {
    private String title;
}
