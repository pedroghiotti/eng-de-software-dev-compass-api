package br.facens.eng_de_software.dev_compass_api.dto;

import java.util.Set;

import br.facens.eng_de_software.dev_compass_api.model.CompensationPackage;

public record CompensationPackageEditorDto(Double salaryValue, Set<Double> benefitValues) {
    public CompensationPackageEditorDto {
        new CompensationPackage(salaryValue, benefitValues.toArray(new Double[0]));
    }
}
