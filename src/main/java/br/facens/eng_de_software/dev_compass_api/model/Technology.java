package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Technology {
    @Id
    UUID id;

    String name;
}
