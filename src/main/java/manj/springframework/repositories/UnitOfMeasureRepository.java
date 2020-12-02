package manj.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import manj.springframework.domain.UnitOfMeasure;

import java.util.Optional;


public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

    Optional<UnitOfMeasure> findByDescription(String description);
}
