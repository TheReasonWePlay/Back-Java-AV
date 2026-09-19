package com.javaAV.coivoiturage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaAV.coivoiturage.entity.DistanceTrajet;

public interface DistanceTrajetRepository
        extends JpaRepository<DistanceTrajet, Long> {

    Optional<DistanceTrajet>
    findByDepartIgnoreCaseAndDestinationIgnoreCase(
            String depart,
            String destination
    );
}