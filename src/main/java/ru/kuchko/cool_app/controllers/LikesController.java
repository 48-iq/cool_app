package ru.kuchko.cool_app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuchko.cool_app.dto.product.ProductPageDto;
import ru.kuchko.cool_app.repositories.ProductRepository;
import ru.kuchko.cool_app.services.LikeService;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesController {
    private final ProductRepository productRepository;
    private final LikeService likeService;

    @GetMapping("/{userId}")
    public ResponseEntity<ProductPageDto> getProductsUserLikes(@PathVariable Integer userId,
                                                               @RequestParam Integer number,
                                                               @RequestParam Integer size) {
        return ResponseEntity.ok(ProductPageDto.from(
                productRepository.findProductsUserLiked(userId, PageRequest.of(number, size))
        ));
    }

    @PostMapping("/{userId}/like{productId}")
    public ResponseEntity<Void> like(@PathVariable Integer userId,
                                     @PathVariable Integer productId) {
        likeService.like(userId, productId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{userId}/remove{productId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Integer userId,
                                           @PathVariable Integer productId) {
        likeService.deleteLike(userId, productId);
        return ResponseEntity.ok().build();
    }
}
