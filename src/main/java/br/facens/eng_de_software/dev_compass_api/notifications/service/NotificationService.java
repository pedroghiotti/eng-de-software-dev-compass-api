package br.facens.eng_de_software.dev_compass_api.notifications.service;

import org.springframework.stereotype.Service;

import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.model.JobListing;

@Service
public class NotificationService {
    public void sendNotifications(Candidate candidate, JobListing publishedJobListing) {
        String message = publishedJobListing.toString();

        candidate.getPreferredNotificationChannels().forEach((x) ->
            x.getNotificationGateway().sendNotification(candidate, message)
        );
    }
}
