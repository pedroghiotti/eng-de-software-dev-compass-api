package br.facens.eng_de_software.dev_compass_api.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.model.Category;
import br.facens.eng_de_software.dev_compass_api.model.Region;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.security.repository.GenericUserRepository;

@Repository
public interface CandidateRepository extends GenericUserRepository<Candidate>{
    // @Query("""
    //     SELECT DISTINCT c FROM Candidate c
    //     LEFT JOIN c.preferredCategories pc
    //     LEFT JOIN c.preferredTechnologies pt
    //     WHERE c.preferredRegion = :region AND (pc IN :categories OR pt IN :technologies)
    // """)
    @Query("""
    SELECT DISTINCT c FROM Candidate c
        WHERE c.preferredRegion = :region AND (
            EXISTS (
                SELECT pc FROM c.preferredCategories pc WHERE pc IN :categories
            ) OR EXISTS (
                SELECT pt FROM c.preferredTechnologies pt WHERE pt IN :technologies
            )
        )
    """)
    Set<Candidate> findByRegionAndPreferredCategoriesOrTechnologies(
        @Param("region") Region region,
        @Param("categories")  Set<Category> categories,
        @Param("technologies") Set<Technology> technologies
    );
}
