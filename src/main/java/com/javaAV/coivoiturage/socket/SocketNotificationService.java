package com.javaAV.coivoiturage.socket;

import org.springframework.stereotype.Service;

@Service
public class SocketNotificationService {

    private final SocketServerService socketServerService;

    public SocketNotificationService(
            SocketServerService socketServerService) {

        this.socketServerService = socketServerService;
    }

    public boolean notifierConducteur(
            String conducteur,
            String message) {

        return socketServerService.envoyerNotification(
                conducteur,
                message
        );
    }
}