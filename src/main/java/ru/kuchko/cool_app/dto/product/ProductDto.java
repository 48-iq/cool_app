package ru.kuchko.cool_app.dto.product;

import lombok.*;
import ru.kuchko.cool_app.entities.ImageEntity;
import ru.kuchko.cool_app.entities.ProductEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDto {
    private String title;
    private String description;
    private Integer image;
    private Integer creator;

    public static ProductDto from(ProductEntity product) {
        return ProductDto.builder()
                .title(product.getTitle())
                .description(product.getDescription())
                .image(product.getImage().getId())
                .creator(product.getCreator().getId())
                .build();
    }
}
