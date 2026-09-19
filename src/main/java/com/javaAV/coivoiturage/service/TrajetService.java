package com.javaAV.coivoiturage.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.javaAV.coivoiturage.dto.TrajetRequest;
import com.javaAV.coivoiturage.entity.Trajet;
import com.javaAV.coivoiturage.exception.PrixInvalideException;
import com.javaAV.coivoiturage.repository.TrajetRepository;
import com.javaAV.coivoiturage.soap.PriceVerificationClient;
import com.cargo.api.soap.generated.VerifierPrixResponse;

@Service
public class TrajetService {

    private final TrajetRepository trajetRepository;
    private final PriceVerificationClient priceVerificationClient;
    private final DistanceTrajetService distanceTrajetService;

    public TrajetService(
            TrajetRepository trajetRepository,
            PriceVerificationClient priceVerificationClient,
            DistanceTrajetService distanceTrajetService) {

        this.trajetRepository = trajetRepository;
        this.priceVerificationClient = priceVerificationClient;
        this.distanceTrajetService = distanceTrajetService;
    }

    public Trajet creerTrajet(TrajetRequest request) {

        // 1. Recherche automatique de la distance
        BigDecimal distanceKm =
                distanceTrajetService.rechercherDistance(
                        request.getDepart(),
                        request.getDestination()
                );

        // 2. Vérification du prix avec SOAP
        VerifierPrixResponse verification =
                priceVerificationClient.verifierPrix(
                        request.getPrix(),
                        request.getPlaces(),
                        distanceKm
                );

        // 3. Refus si le prix est invalide
        if (!"ACCEPTE".equalsIgnoreCase(
                verification.getStatut())) {

        	throw new PrixInvalideException(
        	        verification.getMessage(),
        	        verification.getPrixRecommande()
        	);
        }

        // 4. Création du trajet
        Trajet trajet = new Trajet();

        trajet.setConducteur(request.getConducteur());
        trajet.setDepart(request.getDepart());
        trajet.setDestination(request.getDestination());
        trajet.setDate(request.getDate());
        trajet.setHeure(request.getHeure());
        trajet.setPlaces(request.getPlaces());
        trajet.setPrix(request.getPrix());


        trajet.setStatut("EN_ATTENTE");

        // 5. Enregistrement en base
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