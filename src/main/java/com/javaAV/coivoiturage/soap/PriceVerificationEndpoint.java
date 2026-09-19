package com.javaAV.coivoiturage.soap;

import java.math.BigDecimal;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.cargo.api.soap.generated.VerifierPrixRequest;
import com.cargo.api.soap.generated.VerifierPrixResponse;

@Endpoint
public class PriceVerificationEndpoint {

    private static final String NAMESPACE_URI =
            "http://cargo.com/soap/price";

    private final PriceVerificationService priceService;

    public PriceVerificationEndpoint(
            PriceVerificationService priceService) {

        this.priceService = priceService;
    }

    @PayloadRoot(
            namespace = NAMESPACE_URI,
            localPart = "verifierPrixRequest"
    )
    @ResponsePayload
    public VerifierPrixResponse verifierPrix(
            @RequestPayload VerifierPrixRequest request) {

        BigDecimal prixPropose =
                request.getPrixPropose();

        int nombrePlaces =
                request.getNombrePlaces();

        BigDecimal distanceKm =
                request.getDistanceKm();

        BigDecimal prixRecommande =
                priceService.calculerPrixRecommande(
                        distanceKm,
                        nombrePlaces
                );

        boolean valide =
                priceService.verifierPrix(
                        prixPropose,
                        prixRecommande
                );

        VerifierPrixResponse response =
                new VerifierPrixResponse();

        response.setPrixRecommande(prixRecommande);

        if (valide) {
            response.setStatut("ACCEPTE");
            response.setMessage(
                    "Le prix proposé est acceptable."
            );
        } else {
            response.setStatut("REFUSE");
            response.setMessage(
                    "Le prix proposé est trop éloigné du prix recommandé."
            );
        }

        return response;
    }
}