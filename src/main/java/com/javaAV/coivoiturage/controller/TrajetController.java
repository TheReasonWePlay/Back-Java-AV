package com.javaAV.coivoiturage.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.javaAV.coivoiturage.dto.TrajetRequest;
import com.javaAV.coivoiturage.entity.Trajet;
import com.javaAV.coivoiturage.service.TrajetService;

@RestController
@RequestMapping("/api/trajets")
public class TrajetController {

    private final TrajetService trajetService;

    public TrajetController(TrajetService trajetService) {
        this.trajetService = trajetService;
    }

    // POST : créer un trajet
    @PostMapping
    public ResponseEntity<Trajet> creerTrajet(
            @Valid @RequestBody TrajetRequest request) {

        Trajet trajet = trajetService.creerTrajet(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(trajet);
    }

    // GET : afficher tous les trajets
    @GetMapping
    public ResponseEntity<List<Trajet>> obtenirTousLesTrajets() {

        return ResponseEntity.ok(
                trajetService.obtenirTousLesTrajets()
        );
    }

    // GET : afficher un trajet
    @GetMapping("/{id}")
    public ResponseEntity<Trajet> obtenirTrajetParId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                trajetService.obtenirTrajetParId(id)
        );
    }

    // DELETE : supprimer un trajet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTrajet(
            @PathVariable Long id) {

        trajetService.supprimerTrajet(id);

        return ResponseEntity.noContent().build();
    }
}