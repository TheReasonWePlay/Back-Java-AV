package com.javaAV.coivoiturage.soap;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

@Service
public class PriceVerificationService {

    private static final BigDecimal
            TARIF_PAR_PLACE = new BigDecimal("20000");

    private static final BigDecimal
            TOLERANCE = new BigDecimal("0.20");

    public BigDecimal calculerPrixRecommande(int nombrePlaces) {

        return TARIF_PAR_PLACE.multiply(
                BigDecimal.valueOf(nombrePlaces)
        );
    }

    public boolean verifierPrix(
            BigDecimal prixPropose,
            BigDecimal prixRecommande) {

        BigDecimal difference = prixPropose
                .subtract(prixRecommande)
                .abs();

        BigDecimal limite = prixRecommande
                .multiply(TOLERANCE);

        return difference.compareTo(limite) <= 0;
    }
}