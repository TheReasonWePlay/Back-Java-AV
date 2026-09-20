package com.javaAV.coivoiturage.soap;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.cargo.api.soap.generated.VerifierPrixRequest;
import com.cargo.api.soap.generated.VerifierPrixResponse;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Service
public class PriceVerificationClient {

    private static final String SOAP_URL = "http://localhost:8080/services";

    private final WebServiceTemplate webServiceTemplate;

    public PriceVerificationClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
        
        this.webServiceTemplate.setInterceptors(
                new ClientInterceptor[]{
                        new SoapLoggingInterceptor()
                }
        );
    }

    public VerifierPrixResponse verifierPrix(
            BigDecimal prixPropose,
            int nombrePlaces,
            BigDecimal distanceKm) {

        VerifierPrixRequest request = new VerifierPrixRequest();
        request.setPrixPropose(prixPropose);
        request.setNombrePlaces(nombrePlaces);
        request.setDistanceKm(distanceKm);

        return (VerifierPrixResponse) webServiceTemplate.marshalSendAndReceive(
                SOAP_URL,
                request
        );
    }
}