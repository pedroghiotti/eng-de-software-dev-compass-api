package br.facens.eng_de_software.dev_compass_api.notifications.model;

public enum NotificationChannel {
    DEBUG(new DebugNotificationGateway());

    private final NotificationGateway notificationGateway;

    private NotificationChannel(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    public NotificationGateway getNotificationGateway() {
        return this.notificationGateway;
    }
}
