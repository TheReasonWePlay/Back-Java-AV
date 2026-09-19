package com.javaAV.coivoiturage.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "distance_trajet",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_distance_trajet",
        columnNames = {"depart", "destination"}
    )
)
public class DistanceTrajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String depart;

    @Column(nullable = false, length = 120)
    private String destination;

    @Column(
        name = "distance_km",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal distanceKm;

    public DistanceTrajet() {
    }

    public Long getId() {
        return id;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }
}