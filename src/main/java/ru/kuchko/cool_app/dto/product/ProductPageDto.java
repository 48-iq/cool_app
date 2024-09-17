package ru.kuchko.cool_app.dto.product;

import lombok.*;
import org.springframework.data.domain.Page;
import ru.kuchko.cool_app.entities.ProductEntity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductPageDto {
    private Integer number;
    private Integer size;
    private Integer count;
    private List<ProductDto> data;

    public static ProductPageDto from(Page<ProductEntity> page) {
        return ProductPageDto.builder()
                .number(page.getNumber())
                .size(page.getSize())
                .count(page.getTotalPages())
                .data(page.get().map(ProductDto::from).toList())
                .build();
    }
}
