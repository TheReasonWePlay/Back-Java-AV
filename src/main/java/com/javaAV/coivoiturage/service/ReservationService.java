package com.javaAV.coivoiturage.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaAV.coivoiturage.dto.ReservationRequest;
import com.javaAV.coivoiturage.entity.Reservation;
import com.javaAV.coivoiturage.entity.Trajet;
import com.javaAV.coivoiturage.exception.ReservationException;
import com.javaAV.coivoiturage.repository.ReservationRepository;
import com.javaAV.coivoiturage.repository.TrajetRepository;
import com.javaAV.coivoiturage.socket.SocketNotificationService;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TrajetRepository trajetRepository;
    private final SocketNotificationService socketNotificationService;

    public ReservationService(
            ReservationRepository reservationRepository,
            TrajetRepository trajetRepository,
            SocketNotificationService socketNotificationService) {

        this.reservationRepository = reservationRepository;
        this.trajetRepository = trajetRepository;
        this.socketNotificationService = socketNotificationService;
    }

    // Créer une réservation
    @Transactional
    public Reservation creerReservation(ReservationRequest request) {

        Trajet trajet = trajetRepository.findById(request.getTrajetId())
                .orElseThrow(() -> new ReservationException(
                        "Le trajet avec l'identifiant "
                                + request.getTrajetId()
                                + " n'existe pas"
                ));

        if (request.getNombrePlaces() <= 0) {
            throw new ReservationException(
                    "Le nombre de places doit être supérieur à zéro"
            );
        }

        if (trajet.getPlaces() < request.getNombrePlaces()) {
            throw new ReservationException(
                    "Nombre de places insuffisant. Places disponibles : "
                            + trajet.getPlaces()
            );
        }

        // Diminution des places disponibles
        trajet.setPlaces(
                trajet.getPlaces() - request.getNombrePlaces()
        );

        trajetRepository.save(trajet);

     // Création de la réservation
        Reservation reservation = new Reservation();

        reservation.setTrajet(trajet);
        reservation.setPassager(request.getPassager().trim());
        reservation.setNombrePlaces(request.getNombrePlaces());
        reservation.setStatut("CONFIRMEE");
        reservation.setDateReservation(LocalDateTime.now());

        Reservation reservationSauvegardee =
                reservationRepository.save(reservation);

        // Notification du conducteur
        String message = "Nouvelle réservation : "
                + request.getNombrePlaces()
                + " place(s) réservée(s) par "
                + request.getPassager();

        socketNotificationService.notifierConducteur(
                trajet.getConducteur(),
                message
        );

        return reservationSauvegardee;
    }

    // Récupérer toutes les réservations
    public List<Reservation> getToutesLesReservations() {
        return reservationRepository.findAll();
    }

    // Récupérer une réservation par son identifiant
    public Reservation getReservationById(Long id) {

        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationException(
                        "La réservation avec l'identifiant "
                                + id
                                + " n'existe pas"
                ));
    }

    // Récupérer les réservations d'un trajet
    public List<Reservation> getReservationsByTrajet(Long trajetId) {

        return reservationRepository.findByTrajetId(trajetId);
    }

    // Récupérer les réservations d'un passager
    public List<Reservation> getReservationsByPassager(String passager) {

        return reservationRepository.findByPassagerIgnoreCase(passager);
    }

    // Annuler une réservation
    @Transactional
    public Reservation annulerReservation(Long id) {

        Reservation reservation = getReservationById(id);

        if ("ANNULEE".equals(reservation.getStatut())) {
            throw new ReservationException(
                    "Cette réservation est déjà annulée"
            );
        }

        Trajet trajet = reservation.getTrajet();

        // Restitution des places
        trajet.setPlaces(
                trajet.getPlaces() + reservation.getNombrePlaces()
        );

        trajetRepository.save(trajet);

        reservation.setStatut("ANNULEE");

        return reservationRepository.save(reservation);
    }

    // Supprimer définitivement une réservation
    @Transactional
    public void supprimerReservation(Long id) {

        Reservation reservation = getReservationById(id);

        if ("CONFIRMEE".equals(reservation.getStatut())) {

            Trajet trajet = reservation.getTrajet();

            trajet.setPlaces(
                    trajet.getPlaces() + reservation.getNombrePlaces()
            );

            trajetRepository.save(trajet);
        }

        reservationRepository.delete(reservation);
    }
}