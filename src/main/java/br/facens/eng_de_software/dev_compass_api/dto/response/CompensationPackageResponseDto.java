package br.facens.eng_de_software.dev_compass_api.dto.response;

import java.util.Set;

import br.facens.eng_de_software.dev_compass_api.model.Benefit;
import br.facens.eng_de_software.dev_compass_api.model.CompensationPackage;
import br.facens.eng_de_software.dev_compass_api.model.Salary;

public record CompensationPackageResponseDto(Salary salary, Set<Benefit> benefits) {
    public static CompensationPackageResponseDto fromCompensationPackage(CompensationPackage compensationPackage) {
        return new CompensationPackageResponseDto(
            compensationPackage.getSalary(),
            compensationPackage.getBenefits()
        );
    }
}
