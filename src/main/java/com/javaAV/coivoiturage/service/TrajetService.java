package com.javaAV.coivoiturage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javaAV.coivoiturage.dto.TrajetRequest;
import com.javaAV.coivoiturage.entity.Trajet;
import com.javaAV.coivoiturage.repository.TrajetRepository;

@Service
public class TrajetService {

    private final TrajetRepository trajetRepository;

    public TrajetService(TrajetRepository trajetRepository) {
        this.trajetRepository = trajetRepository;
    }

    // Créer un trajet
    public Trajet creerTrajet(TrajetRequest request) {

        Trajet trajet = new Trajet();

        trajet.setConducteur(request.getConducteur());
        trajet.setDepart(request.getDepart());
        trajet.setDestination(request.getDestination());
        trajet.setDate(request.getDate());
        trajet.setHeure(request.getHeure());
        trajet.setPlaces(request.getPlaces());
        trajet.setPrix(request.getPrix());

        trajet.setStatut("EN_ATTENTE");

        return trajetRepository.save(trajet);
    }

    // Afficher tous les trajets
    public List<Trajet> obtenirTousLesTrajets() {
        return trajetRepository.findAll();
    }

    // Afficher un trajet par son ID
    public Trajet obtenirTrajetParId(Long id) {

        return trajetRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Trajet introuvable : " + id)
                );
    }

    // Supprimer un trajet
    public void supprimerTrajet(Long id) {

        if (!trajetRepository.existsById(id)) {
            throw new RuntimeException("Trajet introuvable : " + id);
        }

        trajetRepository.deleteById(id);
    }
}