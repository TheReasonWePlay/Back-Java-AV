package com.javaAV.coivoiturage.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.javaAV.coivoiturage.entity.DistanceTrajet;
import com.javaAV.coivoiturage.exception.DistanceIntrouvableException;
import com.javaAV.coivoiturage.repository.DistanceTrajetRepository;

@Service
public class DistanceTrajetService {

    private final DistanceTrajetRepository distanceRepository;

    public DistanceTrajetService(
            DistanceTrajetRepository distanceRepository) {

        this.distanceRepository = distanceRepository;
    }

    public BigDecimal rechercherDistance(
            String depart,
            String destination) {

        DistanceTrajet distance =
                distanceRepository
                        .findByDepartIgnoreCaseAndDestinationIgnoreCase(
                                depart.trim(),
                                destination.trim()
                        )
                        .orElseThrow(() ->
                                new DistanceIntrouvableException(
                                        "Aucune distance enregistrée pour "
                                        + depart + " vers "
                                        + destination
                                )
                        );

        return distance.getDistanceKm();
    }
}