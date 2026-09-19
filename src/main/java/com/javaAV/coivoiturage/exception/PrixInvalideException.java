package com.javaAV.coivoiturage.exception;

import java.math.BigDecimal;

public class PrixInvalideException extends RuntimeException {

    private final BigDecimal prixRecommande;

    public PrixInvalideException(
            String message,
            BigDecimal prixRecommande) {

        super(message);
        this.prixRecommande = prixRecommande;
    }

    public BigDecimal getPrixRecommande() {
        return prixRecommande;
    }
}