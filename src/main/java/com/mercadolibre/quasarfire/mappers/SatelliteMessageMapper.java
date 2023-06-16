package com.mercadolibre.quasarfire.mappers;

import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;
import com.mercadolibre.quasarfire.entities.ShipMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class SatelliteMessageMapper {
    public List<SatelliteMessageDTO> fromDatabaseToList (Iterable<ShipMessage> dbShipMessages){
        return StreamSupport.stream(dbShipMessages.spliterator(), false)
                .map(shipMessage -> {
                    SatelliteMessageDTO satelliteMessageDTO = new SatelliteMessageDTO();
                    satelliteMessageDTO.setName(shipMessage.getShip());
                    satelliteMessageDTO.setDistance(shipMessage.getDistance());
                    satelliteMessageDTO.setMessage(shipMessage.getMessage());
                    return satelliteMessageDTO;
                })
                .toList();
    }
}
