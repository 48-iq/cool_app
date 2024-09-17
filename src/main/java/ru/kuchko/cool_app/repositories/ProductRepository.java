package ru.kuchko.cool_app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kuchko.cool_app.entities.ProductEntity;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    @Query("select p from ProductEntity p join p.tags as t " +
            "where p.city.id = :city and t.id in :tagsList and " +
            "p.title like %:query% group by p.id having count(t) = :listSize")
    Page<ProductEntity> findProductsByQuery(String query,
                                            Integer listSize,
                                            List<Integer> tagsList,
                                            Integer city,
                                            Pageable pageable);

    @Query("select p from ProductEntity p join p.tags as t " +
            "where t.id in :tagsList and " +
            "p.title like %:query% group by p.id having count(t) = :listSize")
    Page<ProductEntity> findProductsByQuery(String query,
                                            Integer listSize,
                                            List<Integer> tagsList,
                                            Pageable pageable);

}
