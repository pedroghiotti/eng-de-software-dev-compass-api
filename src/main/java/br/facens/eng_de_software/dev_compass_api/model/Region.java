package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    @Id
    UUID id;

    String name;

    @OneToMany(mappedBy = "region")
    List<JobListing> jobListings;

    public Region(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
