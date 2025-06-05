package br.facens.eng_de_software.dev_compass_api.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(force=true)
public class Benefit {
    private final  String name;
    private final Double amount;

    public Benefit(String name, Double amount) {
        this.name = name;
        this.amount = amount;
        this.validate();
    }

    private void validate() {
        if (this.name == null)
            throw new IllegalStateException("Nome do benefício não pode ser nulo.");
        if (this.name.isBlank())
            throw new IllegalStateException("Nome do benefício não pode ser vazio.");
        if (this.amount == null)
            throw new IllegalStateException("Valor do benefício não pode ser nulo.");
        if (this.amount < 0)
            throw new IllegalStateException("Valor do benefício deve ser maior que 0.");
    }
}
