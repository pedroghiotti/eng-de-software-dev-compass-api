package br.facens.eng_de_software.dev_compass_api.notifications.model;

import br.facens.eng_de_software.dev_compass_api.model.Candidate;

public class EmailNotificationGateway extends NotificationGateway {
    @Override
    public void sendNotification(Candidate candidate, String message) {
        System.out.println("Usuário " + candidate.getUsername() + " notificado via email da vaga: " + message);
    }

}
