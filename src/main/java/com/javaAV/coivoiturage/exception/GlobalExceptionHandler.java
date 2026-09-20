package com.javaAV.coivoiturage.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PrixInvalideException.class)
    public ResponseEntity<Map<String, Object>>
            gererPrixInvalide(
                    PrixInvalideException exception) {

        Map<String, Object> erreur = new LinkedHashMap<>();

        erreur.put(
                "timestamp",
                Instant.now().toString()
        );

        erreur.put("status", 400);
        erreur.put("error", "Bad Request");

        erreur.put(
                "message",
                exception.getMessage()
        );

        erreur.put(
                "prixRecommande",
                exception.getPrixRecommande()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erreur);
    }

    @ExceptionHandler(DistanceIntrouvableException.class)
    public ResponseEntity<Map<String, Object>>
            gererDistanceIntrouvable(
                    DistanceIntrouvableException exception) {

        Map<String, Object> erreur = new LinkedHashMap<>();

        erreur.put(
                "timestamp",
                Instant.now().toString()
        );

        erreur.put("status", 404);
        erreur.put("error", "Not Found");

        erreur.put(
                "message",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erreur);
    }
    
    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<Map<String, Object>> gererReservationException(
            ReservationException exception) {

        Map<String, Object> erreur = new LinkedHashMap<>();

        erreur.put("timestamp", Instant.now().toString());
        erreur.put("status", 400);
        erreur.put("error", "Bad Request");
        erreur.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erreur);
    }
}