package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Region {
    @Id
    String name;

    @OneToMany(mappedBy = "region")
    List<JobListing> jobListings;
}
