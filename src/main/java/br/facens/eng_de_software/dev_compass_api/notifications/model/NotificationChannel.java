package br.facens.eng_de_software.dev_compass_api.notifications.model;

public enum NotificationChannel {
    EMAIL(new EmailNotificationGateway());

    private final NotificationGateway notificationGateway;

    private NotificationChannel(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    public NotificationGateway getNotificationGateway() {
        return this.notificationGateway;
    }
}
