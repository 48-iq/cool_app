package ru.kuchko.cool_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kuchko.cool_app.entities.TagEntity;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    @Query("select t from TagEntity t where t.id in :tagIds")
    List<TagEntity> findTags(List<Integer> tagIds);
}
