package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Salary {
    private Double amount;

    public Salary(Double amount) {
        this.amount = amount;
        this.validate();
    }

    private void validate() {
        if (this.amount == null)
            throw new IllegalStateException("Valor do salário não pode ser nulo.");
            
        if (this.amount < 0)
            throw new IllegalStateException("Valor do salário deve ser maior que 0.");
    }
}
