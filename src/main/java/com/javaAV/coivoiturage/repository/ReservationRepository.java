package com.javaAV.coivoiturage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaAV.coivoiturage.entity.Reservation;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    List<Reservation> findByTrajetId(Long trajetId);

    List<Reservation> findByPassagerIgnoreCase(String passager);
}