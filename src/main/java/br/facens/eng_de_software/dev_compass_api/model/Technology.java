package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Technology {
    @Id
    UUID id;
    String name;

    @ManyToMany
    Set<Category> category = new HashSet<>();

    public Technology(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
