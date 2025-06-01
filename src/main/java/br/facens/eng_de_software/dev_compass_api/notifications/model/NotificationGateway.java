package br.facens.eng_de_software.dev_compass_api.notifications.model;

public abstract class NotificationGateway {
    public abstract void sendNotification(String message);
}
