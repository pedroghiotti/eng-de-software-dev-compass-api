package br.facens.eng_de_software.dev_compass_api.notifications.model;

import org.springframework.context.ApplicationEvent;

import br.facens.eng_de_software.dev_compass_api.model.JobListing;

public class JobListingPublishedEvent extends ApplicationEvent {
    private JobListing jobListing;

    public JobListingPublishedEvent(Object source, JobListing jobListing) {
        super(source);
        this.jobListing = jobListing;
    }

    public JobListing getJobListing() {
        return this.jobListing;
    }
}
