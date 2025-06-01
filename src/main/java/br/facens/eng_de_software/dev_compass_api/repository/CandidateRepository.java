package br.facens.eng_de_software.dev_compass_api.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.model.Category;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.security.repository.GenericUserRepository;

public interface CandidateRepository extends GenericUserRepository<Candidate>{
    @Query("""
        SELECT DISTINCT c FROM Candidate c
        LEFT JOIN c.preferredCategories pc
        LEFT JOIN c.preferredTechnologies pt
        WHERE pc IN :categories OR pt IN :technologies
    """)
    Set<Candidate> findByPreferredCategoriesOrTechnologies(
        @Param("category")  Set<Category> category,
        @Param("technologies") Set<Technology> technologies
    );
}
