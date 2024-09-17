package ru.kuchko.cool_app.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kuchko.cool_app.exceptions.EntityNoFoundByIdException;
import ru.kuchko.cool_app.repositories.ProductRepository;
import ru.kuchko.cool_app.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void like(Integer userId, Integer productId) {
        if (!userRepository.existsById(userId))
            throw new EntityNoFoundByIdException("user", userId);
        if (!productRepository.existsById(productId))
            throw new EntityNoFoundByIdException("product", productId);
        userRepository.createLike(userId, productId);
    }

    @Transactional
    public void deleteLike(Integer userId, Integer productId) {
        userRepository.deleteLike(userId, productId);
    }
}
