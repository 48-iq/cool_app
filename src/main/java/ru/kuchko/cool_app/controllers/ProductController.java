package ru.kuchko.cool_app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuchko.cool_app.dto.product.ProductDto;
import ru.kuchko.cool_app.dto.product.ProductPageDto;
import ru.kuchko.cool_app.dto.product.ProductUploadDto;
import ru.kuchko.cool_app.exceptions.EntityNoFoundByIdException;
import ru.kuchko.cool_app.repositories.ProductRepository;
import ru.kuchko.cool_app.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(ProductDto.from(
                productRepository.findById(productId)
                        .orElseThrow(() -> new EntityNoFoundByIdException("product", productId))
        ));
    }

    @GetMapping
    public ResponseEntity<ProductPageDto> findProducts(@RequestParam String query,
                                                       @RequestParam Integer number,
                                                       @RequestParam Integer size,
                                                       @RequestParam List<Integer> tags,
                                                       @RequestParam(required = false) Integer city) {
        if (city != null)
            return ResponseEntity.ok(ProductPageDto.from(
                    productRepository.findProductsByQuery(query,
                            tags.size(),
                            tags,
                            city,
                            PageRequest.of(number, size))
            ));
        else
            return ResponseEntity.ok(ProductPageDto.from(
                    productRepository.findProductsByQuery(query,
                            tags.size(),
                            tags,
                            PageRequest.of(number, size))
            ));
    }

    @PostMapping("/{creatorId}")
    public ResponseEntity<ProductDto> createProduct(@ModelAttribute ProductUploadDto productUploadDto,
                                                    @PathVariable Integer creatorId) {
        return ResponseEntity.ok(productService.createProduct(creatorId, productUploadDto));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@ModelAttribute ProductUploadDto productUploadDto,
                                                    @PathVariable Integer productId) {
        return ResponseEntity.ok(productService.updateProduct(productId, productUploadDto));
    }

}
