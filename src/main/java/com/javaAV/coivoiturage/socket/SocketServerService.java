package com.javaAV.coivoiturage.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.stereotype.Service;

@Service
public class SocketServerService {

    private static final int PORT = 6000;

    private ServerSocket serverSocket;

    private volatile boolean running = true;

    private final Map<String, PrintWriter> clients =
            new ConcurrentHashMap<>();

    @PostConstruct
    public void demarrerServeur() {

        Thread serverThread = new Thread(
                this::ecouterConnexions,
                "socket-server-thread"
        );

        serverThread.setDaemon(true);
        serverThread.start();

        System.out.println(
                "Serveur Socket démarré sur le port " + PORT
        );
    }

    private void ecouterConnexions() {

        try (ServerSocket serveur = new ServerSocket(PORT)) {

            this.serverSocket = serveur;

            while (running) {

                Socket socket = serveur.accept();

                Thread clientThread = new Thread(
                        () -> gererClient(socket),
                        "socket-client-thread"
                );

                clientThread.start();
            }

        } catch (IOException exception) {

            if (running) {
                System.err.println(
                        "Erreur du serveur Socket : "
                                + exception.getMessage()
                );
            }
        }
    }

    private void gererClient(Socket socket) {

        String nomClient = null;

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(),
                        true
                )
        ) {

            // Première ligne envoyée par le client :
            // REGISTER:Paul
            String inscription = reader.readLine();

            if (inscription == null
                    || !inscription.startsWith("REGISTER:")) {

                writer.println(
                        "ERREUR: Inscription obligatoire"
                );

                return;
            }

            nomClient = inscription
                    .substring("REGISTER:".length())
                    .trim();

            if (nomClient.isBlank()) {

                writer.println(
                        "ERREUR: Nom du client invalide"
                );

                return;
            }

            clients.put(nomClient, writer);

            writer.println(
                    "CONNECTE: Bienvenue " + nomClient
            );

            System.out.println(
                    "Client connecté : " + nomClient
            );

            String message;

            while ((message = reader.readLine()) != null) {

                System.out.println(
                        "Message de " + nomClient + " : " + message
                );
            }

        } catch (IOException exception) {

            System.out.println(
                    "Connexion interrompue : "
                            + exception.getMessage()
            );

        } finally {

            if (nomClient != null) {

                clients.remove(nomClient);

                System.out.println(
                        "Client déconnecté : " + nomClient
                );
            }

            try {
                socket.close();
            } catch (IOException exception) {
                // Connexion déjà fermée
            }
        }
    }

    public boolean envoyerNotification(
            String nomClient,
            String message) {

        PrintWriter writer = clients.get(nomClient);

        if (writer == null) {

            System.out.println(
                    "Client non connecté : " + nomClient
            );

            return false;
        }

        writer.println("NOTIFICATION:" + message);

        return true;
    }

    public int getNombreClientsConnectes() {
        return clients.size();
    }

    @PreDestroy
    public void arreterServeur() {

        running = false;

        try {

            if (serverSocket != null) {
                serverSocket.close();
            }

        } catch (IOException exception) {

            System.err.println(
                    "Erreur lors de l'arrêt du serveur Socket"
            );
        }

        clients.clear();

        System.out.println("Serveur Socket arrêté");
    }
}