package ru.kuchko.cool_app.dto.product;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductUploadDto {
    private String title;
    private String description;
    private MultipartFile image;
    private List<Integer> tags;
    private Integer city;
}
