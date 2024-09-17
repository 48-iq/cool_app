package ru.kuchko.cool_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuchko.cool_app.entities.CityEntity;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer> {
}
