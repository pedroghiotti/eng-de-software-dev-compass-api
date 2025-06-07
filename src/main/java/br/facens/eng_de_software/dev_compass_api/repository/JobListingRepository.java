package br.facens.eng_de_software.dev_compass_api.repository;

import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, UUID> {
    public List<JobListing> findAllByRegionId(UUID regionId);
}
