package com.mercadolibre.quasarfire.controllers;

import com.mercadolibre.quasarfire.dtos.SatelliteDataDTO;
import com.mercadolibre.quasarfire.dtos.SatelliteMessageDTO;
import com.mercadolibre.quasarfire.dtos.TopSecretResponseDTO;
import com.mercadolibre.quasarfire.exceptions.ResourceNotFoundException;
import com.mercadolibre.quasarfire.managers.TopSecretManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TopSecretController {
    @Autowired
    private TopSecretManager topSecretManager;

    @PostMapping(value = "/topsecret", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public TopSecretResponseDTO topSecret(@RequestBody @Valid SatelliteDataDTO satelliteMessage) {
        List<SatelliteMessageDTO> messages = satelliteMessage.getSatellites();
        return topSecretManager.processAllSatellites(messages);
    }

    @PostMapping(value = "/topsecret_split/{satellite_name}")
    public TopSecretResponseDTO topSecretSplit(
            @PathVariable("satellite_name") String satelliteName,
            @RequestBody SatelliteMessageDTO message
    ) {
        List<SatelliteMessageDTO> satelliteMessage = new ArrayList<>();
        message.setName(satelliteName);
        satelliteMessage.add(message);
        return topSecretManager.processAllSatellites(satelliteMessage);
    }
    @GetMapping("/topsecret/position")
    public TopSecretResponseDTO topSecretPosition() {
        return topSecretManager.processDBPosition();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException ex) {
        List<String> errorMessages = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            Path propertyPath = violation.getPropertyPath();
            String message = violation.getMessage();
            errorMessages.add(propertyPath + ": " + message);
        }

        String errorMessage = String.join(", ", errorMessages);
        throw new ResourceNotFoundException(errorMessage);
    }
}
