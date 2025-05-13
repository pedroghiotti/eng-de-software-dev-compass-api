package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListing {
    @Id
    private UUID id;

    private String title;

    private String description;

    @Enumerated
    private JobListingState state;

    @ManyToOne @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToMany(fetch = FetchType.EAGER) @JoinTable(
        name = "job_listing_technology",
        joinColumns = @JoinColumn(name = "job_listing_id"),
        inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private List<Technology> technologies;
}
