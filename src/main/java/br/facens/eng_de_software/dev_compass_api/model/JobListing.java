package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class JobListing {
    @Id
    private UUID id;

    private String title;

    private String description;

    @Enumerated
    @Setter(AccessLevel.NONE)
    private JobListingState state;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "job_listing_technology", joinColumns = @JoinColumn(name = "job_listing_id"), inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private List<Technology> technologies;

    public JobListing(UUID id, String title, String description, Region region, Business owner,
            List<Technology> technologies) {
        this.state = JobListingState.UNPUBLISHED;
        this.id = id;
        this.title = title;
        this.description = description;
        this.region = region;
        this.owner = owner;
        this.technologies = technologies;
    }

    public void unpublish() {
        if (this.state.equals(JobListingState.CLOSED) || this.state.equals(JobListingState.UNPUBLISHED))
            throw new IllegalStateException(
                    String.format(
                            "Transição de estado inválida: %s -> %s",
                            this.state.toString(),
                            JobListingState.UNPUBLISHED.toString()));

        this.state = JobListingState.UNPUBLISHED;
    }

    public void publish() {
        if (!this.state.equals(JobListingState.UNPUBLISHED))
            throw new IllegalStateException(
                    String.format(
                            "Transição de estado inválida: %s -> %s",
                            this.state.toString(),
                            JobListingState.PUBLISHED.toString()));

        this.state = JobListingState.PUBLISHED;
    }

    public void startSelection() {
        if (!this.state.equals(JobListingState.PUBLISHED))
            throw new IllegalStateException(
                    String.format(
                            "Transição de estado inválida: %s -> %s",
                            this.state.toString(),
                            JobListingState.SELECTION_IN_PROCESS.toString()));

        this.state = JobListingState.SELECTION_IN_PROCESS;
    }

    public void close() {
        if (!this.state.equals(JobListingState.SELECTION_IN_PROCESS))
            throw new IllegalStateException(
                    String.format(
                            "Transição de estado inválida: %s -> %s",
                            this.state.toString(),
                            JobListingState.CLOSED.toString()));

        this.state = JobListingState.CLOSED;
    }
}
