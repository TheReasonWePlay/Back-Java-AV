package com.javaAV.coivoiturage.soap;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class PriceVerificationService {

    private static final BigDecimal TARIF_BASE =
            new BigDecimal("1000");

    private static final BigDecimal TARIF_MOINS_100 =
            new BigDecimal("80");

    private static final BigDecimal TARIF_MOINS_200 =
            new BigDecimal("100");

    private static final BigDecimal TARIF_MOINS_450 =
            new BigDecimal("120");

    private static final BigDecimal TARIF_PLUS_450 =
            new BigDecimal("140");

    private static final BigDecimal TOLERANCE =
            new BigDecimal("0.10");

    public BigDecimal calculerPrixRecommande(
            BigDecimal distanceKm,
            int nombrePlaces) {

        BigDecimal tarifParKm;

        if (distanceKm.compareTo(new BigDecimal("100")) < 0) {

            tarifParKm = TARIF_MOINS_100;

        } else if (distanceKm.compareTo(new BigDecimal("200")) < 0) {

            tarifParKm = TARIF_MOINS_200;

        } else if (distanceKm.compareTo(new BigDecimal("450")) <= 0) {

            tarifParKm = TARIF_MOINS_450;

        } else {

            tarifParKm = TARIF_PLUS_450;
        }

        BigDecimal prixParPlace =
                TARIF_BASE.add(
                        distanceKm.multiply(tarifParKm)
                );

        return prixParPlace;
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