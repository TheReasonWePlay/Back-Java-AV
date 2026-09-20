package com.javaAV.coivoiturage.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationRequest {

    @NotNull(message = "L'identifiant du trajet est obligatoire")
    private Long trajetId;

    @NotBlank(message = "Le nom du passager est obligatoire")
    private String passager;

    @Min(value = 1, message = "Le nombre de places doit être au moins 1")
    private int nombrePlaces;

    public ReservationRequest() {
    }

    public Long getTrajetId() {
        return trajetId;
    }

    public void setTrajetId(Long trajetId) {
        this.trajetId = trajetId;
    }

    public String getPassager() {
        return passager;
    }

    public void setPassager(String passager) {
        this.passager = passager;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }
}