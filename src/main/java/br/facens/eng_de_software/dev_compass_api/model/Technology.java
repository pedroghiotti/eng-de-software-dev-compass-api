package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Technology {
    @Id
    String name;

    @ManyToMany(mappedBy = "technologies")
    List<JobListing> jobListings;
}
