package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class JobListing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToMany
    @JoinTable(
        name = "job_listing_technology",
        joinColumns = @JoinColumn(name = "job_listing_id"),
        inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    List<Technology> technologies;
}
