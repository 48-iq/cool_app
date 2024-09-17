package ru.kuchko.cool_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kuchko.cool_app.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    @Modifying
    @Query(nativeQuery = true,
    value = "delete from products " +
            "where creator_id = ?1")
    void deleteUserAssociations(Integer userId);
}
