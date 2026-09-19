package com.javaAV.coivoiturage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaAV.coivoiturage.entity.Trajet;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
}