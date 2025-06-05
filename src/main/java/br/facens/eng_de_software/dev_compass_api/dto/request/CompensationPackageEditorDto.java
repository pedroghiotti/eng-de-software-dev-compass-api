package br.facens.eng_de_software.dev_compass_api.dto.request;

import java.util.Set;

import br.facens.eng_de_software.dev_compass_api.model.Benefit;
import br.facens.eng_de_software.dev_compass_api.model.Salary;

public record CompensationPackageEditorDto(Salary salary, Set<Benefit> benefits) {
}
