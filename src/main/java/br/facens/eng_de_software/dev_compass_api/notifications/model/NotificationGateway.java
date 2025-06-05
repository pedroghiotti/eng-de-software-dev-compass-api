package br.facens.eng_de_software.dev_compass_api.notifications.model;

import br.facens.eng_de_software.dev_compass_api.model.Candidate;

public abstract class NotificationGateway {
    public abstract void sendNotification(Candidate candidate, String message);
}
