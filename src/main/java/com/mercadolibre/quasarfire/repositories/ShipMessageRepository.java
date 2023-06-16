package com.mercadolibre.quasarfire.repositories;

import com.mercadolibre.quasarfire.entities.ShipMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipMessageRepository extends CrudRepository<ShipMessage, Integer> {
    Optional<ShipMessage> findByShip(String ship);
}
