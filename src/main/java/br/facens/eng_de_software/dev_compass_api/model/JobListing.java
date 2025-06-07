package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class JobListing {
    @Id
    private UUID id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private JobListingState state = JobListingState.UNPUBLISHED;

    @ManyToOne
    private Region region;

    @ManyToOne
    private Business owner;

    @ManyToMany private Set<Technology> technologies;

    @ManyToMany private Set<Category> categories;

    @OneToOne(optional=false, cascade=CascadeType.ALL) private CompensationPackage compensationPackage;
    
    public JobListing(UUID id, String title, String description, Region region, Business owner, Set<Technology> technologies, Set<Category> categories, CompensationPackage compensationPackage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.region = region;
        this.owner = owner;
        this.technologies = technologies;
        this.categories = categories;
        this.compensationPackage = compensationPackage;
    }

    public void unpublish() {
        if (!this.state.equals(JobListingState.PUBLISHED))
            throw new IllegalStateException("Transição de estado inválida: " + this.state + " -> " + JobListingState.UNPUBLISHED);
        this.state = JobListingState.UNPUBLISHED;
    }

    public void publish() {
        if (!this.state.equals(JobListingState.UNPUBLISHED))
            throw new IllegalStateException("Transição de estado inválida: " + this.state + " -> " + JobListingState.PUBLISHED);
        this.state = JobListingState.PUBLISHED;
    }

    public void beginCandidateSelection() {
        if (!this.state.equals(JobListingState.PUBLISHED))
            throw new IllegalStateException("Transição de estado inválida: " + this.state + " -> " + JobListingState.SELECTION_IN_PROCESS);
        this.state = JobListingState.SELECTION_IN_PROCESS;
    }

    public void close() {
        if (!this.state.equals(JobListingState.SELECTION_IN_PROCESS))
            throw new IllegalStateException("Transição de estado inválida: " + this.state + " -> " + JobListingState.CLOSED);
        this.state = JobListingState.CLOSED;
    }

    @Override
    public String toString() {
        return "JobListing{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", region=" + region +
                ", owner=" + owner +
                ", technologies=" + technologies +
                ", categories=" + categories +
                ", compensationPackage=" + compensationPackage +
                '}';
    }
}
