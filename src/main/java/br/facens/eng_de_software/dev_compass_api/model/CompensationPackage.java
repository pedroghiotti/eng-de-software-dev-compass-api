package br.facens.eng_de_software.dev_compass_api.model;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class CompensationPackage {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Embedded
    private Salary salary;

    @ElementCollection(targetClass=Benefit.class)
    private Set<Benefit> benefits;

    public CompensationPackage(Double salaryValue, Double... benefitValues) {
        this.salary = new Salary(salaryValue);
        this.benefits = Stream.of(benefitValues).map((value) -> new Benefit(value)).collect(Collectors.toSet());
    }
}
