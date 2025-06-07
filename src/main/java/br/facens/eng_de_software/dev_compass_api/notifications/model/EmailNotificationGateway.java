package br.facens.eng_de_software.dev_compass_api.notifications.model;

import br.facens.eng_de_software.dev_compass_api.model.Candidate;

public class EmailNotificationGateway extends NotificationGateway {
    @Override
    public void sendNotification(Candidate candidate, String message) {
        System.out.println("\n\nUsuário " + candidate.getUsername() + " notificado via email da vaga:\n" + message + "\n\n");
    }

}
