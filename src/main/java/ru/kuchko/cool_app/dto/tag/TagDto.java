package ru.kuchko.cool_app.dto.tag;

import lombok.*;
import ru.kuchko.cool_app.entities.TagEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TagDto {
    private Integer id;
    private String title;

    public static TagDto from(TagEntity tag) {
        return TagDto.builder()
                .id(tag.getId())
                .title(tag.getTitle())
                .build();
    }
}
