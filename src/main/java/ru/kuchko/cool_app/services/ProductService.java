package ru.kuchko.cool_app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kuchko.cool_app.dto.product.ProductDto;
import ru.kuchko.cool_app.dto.product.ProductUploadDto;
import ru.kuchko.cool_app.entities.*;
import ru.kuchko.cool_app.exceptions.EntityNoFoundByIdException;
import ru.kuchko.cool_app.repositories.CityRepository;
import ru.kuchko.cool_app.repositories.ProductRepository;
import ru.kuchko.cool_app.repositories.TagRepository;
import ru.kuchko.cool_app.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ImageService imageService;
    private final CityRepository cityRepository;

    public ProductDto createProduct(Integer creatorId, ProductUploadDto productUploadDto) {
        UserEntity creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNoFoundByIdException("user", creatorId));
        List<TagEntity> tags = null;
        if (productUploadDto.getTags() != null)
            tags = tagRepository.findTags(productUploadDto.getTags());
        ImageEntity image = null;
        if (productUploadDto.getImage() != null)
            image = imageService.save(productUploadDto.getImage());
        CityEntity city = null;
        if (productUploadDto.getCity() != null)
            city = cityRepository.findById(productUploadDto.getCity())
                    .orElseThrow(() -> new EntityNoFoundByIdException("city", productUploadDto.getCity()));
        ProductEntity product = ProductEntity.builder()
                .title(productUploadDto.getTitle())
                .description(productUploadDto.getDescription())
                .creator(creator)
                .tags(tags)
                .image(image)
                .city(city)
                .build();
        ProductEntity savedProduct = productRepository.save(product);
        return ProductDto.from(savedProduct);
    }

    public ProductDto updateProduct(Integer productId, ProductUploadDto productUploadDto) {

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNoFoundByIdException("product", productId));

        if (productUploadDto.getTags() != null) {
            product.setTags(tagRepository.findTags(productUploadDto.getTags()));
        }

        if (productUploadDto.getImage() != null) {
            if (product.getImage() == null)
                product.setImage(imageService.save(productUploadDto.getImage()));
            else
                imageService.update(product.getImage().getId(), productUploadDto.getImage());
        }

        product.setTitle(productUploadDto.getTitle());
        product.setDescription(productUploadDto.getDescription());

        if (productUploadDto.getCity() != null)
            product.setCity(cityRepository.findById(productUploadDto.getCity())
                    .orElseThrow(() -> new EntityNoFoundByIdException("city", productUploadDto.getCity()))
            );

        ProductEntity savedProduct = productRepository.save(product);
        return ProductDto.from(savedProduct);
    }

}
