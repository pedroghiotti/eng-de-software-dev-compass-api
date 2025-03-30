package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Technology {
    @Id
    String name;

    @ManyToMany(mappedBy = "technologies")
    List<JobListing> jobListings;

    public Technology(String name) {
        this.name = name;
    }
}
