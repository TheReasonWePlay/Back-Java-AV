package com.javaAV.coivoiturage.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.javaAV.coivoiturage.dto.ReservationRequest;
import com.javaAV.coivoiturage.entity.Reservation;
import com.javaAV.coivoiturage.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(
            ReservationService reservationService) {

        this.reservationService = reservationService;
    }

    // Créer une réservation
    @PostMapping
    public ResponseEntity<Reservation> creerReservation(
            @Valid @RequestBody ReservationRequest request) {

        Reservation reservation =
                reservationService.creerReservation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservation);
    }

    // Récupérer toutes les réservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getToutesLesReservations() {

        return ResponseEntity.ok(
                reservationService.getToutesLesReservations()
        );
    }

    // Récupérer une réservation par son identifiant
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservationService.getReservationById(id)
        );
    }

    // Récupérer les réservations d'un trajet
    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<List<Reservation>> getReservationsByTrajet(
            @PathVariable Long trajetId) {

        return ResponseEntity.ok(
                reservationService.getReservationsByTrajet(trajetId)
        );
    }

    // Récupérer les réservations d'un passager
    @GetMapping("/passager/{passager}")
    public ResponseEntity<List<Reservation>> getReservationsByPassager(
            @PathVariable String passager) {

        return ResponseEntity.ok(
                reservationService.getReservationsByPassager(passager)
        );
    }

    // Annuler une réservation
    @PutMapping("/{id}/annuler")
    public ResponseEntity<Reservation> annulerReservation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservationService.annulerReservation(id)
        );
    }

    // Supprimer une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerReservation(
            @PathVariable Long id) {

        reservationService.supprimerReservation(id);

        return ResponseEntity.noContent().build();
    }
}