package br.facens.eng_de_software.dev_compass_api.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CompensationPackage {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Embedded
    private Salary salary;

    @ElementCollection(targetClass=Benefit.class)
    private Set<Benefit> benefits;

    public CompensationPackage(Salary salary, Benefit... benefits) {
        this.salary = salary;
        this.benefits = Set.of(benefits);
    }
}
