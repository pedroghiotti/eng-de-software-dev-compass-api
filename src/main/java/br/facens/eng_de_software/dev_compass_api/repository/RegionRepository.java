package br.facens.eng_de_software.dev_compass_api.repository;

import br.facens.eng_de_software.dev_compass_api.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
}
