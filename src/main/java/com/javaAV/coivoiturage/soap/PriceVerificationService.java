package com.javaAV.coivoiturage.soap;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

@Service
public class PriceVerificationService {

    private static final BigDecimal TARIF_BASE =
            new BigDecimal("2000");

    private static final BigDecimal TARIF_PAR_KM =
            new BigDecimal("500");

    private static final BigDecimal TOLERANCE =
            new BigDecimal("0.20");

    public BigDecimal calculerPrixRecommande(
            BigDecimal distanceKm,
            int nombrePlaces) {

        BigDecimal prixParPlace =
                TARIF_BASE.add(
                        distanceKm.multiply(TARIF_PAR_KM)
                );

        return prixParPlace
                .multiply(BigDecimal.valueOf(nombrePlaces))
                .setScale(2, RoundingMode.HALF_UP);
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