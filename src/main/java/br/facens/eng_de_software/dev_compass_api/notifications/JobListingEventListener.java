package br.facens.eng_de_software.dev_compass_api.notifications;

import java.util.Set;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.notifications.model.JobListingPublishedEvent;
import br.facens.eng_de_software.dev_compass_api.notifications.service.NotificationService;
import br.facens.eng_de_software.dev_compass_api.repository.CandidateRepository;

@Component
public class JobListingEventListener {
    private final CandidateRepository candidateRepository;
    private final NotificationService notificationService;

    public JobListingEventListener(CandidateRepository candidateRepository, NotificationService notificationService) {
        this.candidateRepository = candidateRepository;
        this.notificationService = notificationService;
    }

    @EventListener
    public void onJobListingPublished(JobListingPublishedEvent event) {
        JobListing publishedJobListing = event.getJobListing();
        
        Set<Candidate> interestedCandidates = candidateRepository.findByRegionAndPreferredCategoriesOrTechnologies(
            publishedJobListing.getRegion(),
            publishedJobListing.getCategories(),
            publishedJobListing.getTechnologies()
        );

        for (Candidate candidate : interestedCandidates) {
            notificationService.sendNotifications(candidate, publishedJobListing);
        }
    }
}
