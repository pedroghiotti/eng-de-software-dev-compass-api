package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobListing {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "job_listing_technology",
        joinColumns = @JoinColumn(name = "job_listing_id"),
        inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    List<Technology> technologies;
}
