package ru.kuchko.cool_app.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kuchko.cool_app.dto.user.UserCreateDto;
import ru.kuchko.cool_app.dto.user.UserDto;
import ru.kuchko.cool_app.dto.user.UserUpdateDto;
import ru.kuchko.cool_app.entities.CityEntity;
import ru.kuchko.cool_app.entities.ImageEntity;
import ru.kuchko.cool_app.entities.Role;
import ru.kuchko.cool_app.entities.UserEntity;
import ru.kuchko.cool_app.exceptions.EntityNoFoundByIdException;
import ru.kuchko.cool_app.repositories.CityRepository;
import ru.kuchko.cool_app.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityRepository cityRepository;

    public UserEntity registerUser(UserCreateDto userCreateDto) {
        UserEntity user = UserEntity.builder()
                .username(userCreateDto.getUsername())
                .name(userCreateDto.getName())
                .surname(userCreateDto.getSurname())
                .email(userCreateDto.getEmail())
                .phone(userCreateDto.getPhone())
                .role(Role.valueOf(userCreateDto.getRole()))
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .build();
        if (userCreateDto.getIcon() != null) {
            ImageEntity image = imageService.save(userCreateDto.getIcon());
            user.setIcon(image);
        }
        if (userCreateDto.getCity() != null) {
            CityEntity city = cityRepository.findById(userCreateDto.getCity())
                    .orElseThrow(() -> new EntityNoFoundByIdException("city", userCreateDto.getCity()));
            user.setCity(city);
        }
        return userRepository.save(user);
    }

    public UserDto updateUser(Integer userId, UserUpdateDto userUpdateDto) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new EntityNoFoundByIdException("user", userId);
        UserEntity user = userOptional.get();
        user.setName(userUpdateDto.getName());
        user.setSurname(userUpdateDto.getSurname());
        user.setPhone(userUpdateDto.getPhone());
        user.setEmail(userUpdateDto.getEmail());
        Optional<CityEntity> cityOptional = cityRepository.findById(userUpdateDto.getCity());
        if (cityOptional.isEmpty())
            throw new EntityNoFoundByIdException("city", userUpdateDto.getCity());
        user.setCity(cityOptional.get());
        if (userUpdateDto.getIcon() != null) {
            if (user.getIcon() != null)
                imageService.update(user.getIcon().getId(), userUpdateDto.getIcon());
            else
                user.setIcon(imageService.save(userUpdateDto.getIcon()));
        }
        UserEntity savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

    public void deleteUser(Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if (user.getIcon() != null)
                imageService.delete(user.getIcon().getId());
            userRepository.deleteUserAssociations(user.getId());
            userRepository.deleteById(user.getId());
        }
    }
}
