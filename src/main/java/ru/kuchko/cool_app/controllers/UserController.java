package ru.kuchko.cool_app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuchko.cool_app.dto.user.UserDto;
import ru.kuchko.cool_app.dto.user.UserUpdateDto;
import ru.kuchko.cool_app.entities.UserEntity;
import ru.kuchko.cool_app.exceptions.EntityNoFoundByIdException;
import ru.kuchko.cool_app.repositories.UserRepository;
import ru.kuchko.cool_app.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new EntityNoFoundByIdException("user", userId);
        return ResponseEntity.ok(UserDto.from(userOptional.get()));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer userId,
                                              @ModelAttribute UserUpdateDto userUpdateDto) {
        UserDto user = userService.updateUser(userId, userUpdateDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
