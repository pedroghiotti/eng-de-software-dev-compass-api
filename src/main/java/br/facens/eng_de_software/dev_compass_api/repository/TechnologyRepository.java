package br.facens.eng_de_software.dev_compass_api.repository;

import br.facens.eng_de_software.dev_compass_api.model.Technology;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, UUID> {
    @Query("""
    SELECT t FROM JobListing jl
    JOIN jl.technologies t
    WHERE jl.region.id = :regionId
    GROUP BY t.id
    ORDER BY COUNT(jl) DESC,
        (SELECT COUNT(_jl) FROM JobListing _jl JOIN _jl.technologies _t WHERE _t.id = t.id) DESC
    """)
    public List<Technology> findTopTechnologiesByRegion(@Param("regionId") UUID regionId, Pageable pageable);

    public Set<Technology> findAllByIdIn(Set<UUID> preferredTechnologyIds);
}
