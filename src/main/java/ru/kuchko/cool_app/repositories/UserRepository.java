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
            "where creator_id = ?1; " +
            "delete  from likes " +
            "where user_id = ?1; "
    )
    void deleteUserAssociations(Integer userId);

    @Modifying
    @Query(nativeQuery = true,
    value = "insert into likes(user_id, product_id) " +
            "values (?1, ?2) on conflict(user_id, product_id) do nothing")
    void createLike(Integer userId, Integer productId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from likes where " +
                    "user_id = ?1 and product_id = ?2")
    void deleteLike(Integer userId, Integer productId);
}
