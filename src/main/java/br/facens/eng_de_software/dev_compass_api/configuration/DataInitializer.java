package br.facens.eng_de_software.dev_compass_api.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.model.JobListingState;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.repository.JobListingRepository;
import br.facens.eng_de_software.dev_compass_api.repository.RegionRepository;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import br.facens.eng_de_software.dev_compass_api.security.repository.BaseUserRepository;
import jakarta.annotation.PostConstruct;

@Component
@DependsOn("userInitializer")
public class DataInitializer {
    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @PostConstruct()
    public void init() {
        var random = new Random();
        var regions = regionRepository.findAll();
        var technologies = technologyRepository.findAll();
        Business business = (Business) baseUserRepository.findByAuthenticationDataUsername("business").get();
        List<JobListing> newJobListings = new ArrayList<>();

        for (var region : regions) {
            for (int i = 0; i < 10; i++) {
                Set<Technology> newJobListingTechnologies = new HashSet<>();

                for (int j = 0; j < random.nextInt(1, 5); j++) {
                    newJobListingTechnologies.add(technologies.get(random.nextInt(0, technologies.size())));
                }

                JobListing newJobListing = new JobListing(
                    UUID.randomUUID(),
                    String.join(" | ", newJobListingTechnologies.stream().map(Technology::getName).toList()),
                    String.join(" | ", newJobListingTechnologies.stream().map(Technology::getName).toList()),
                    JobListingState.PUBLISHED,
                    region,
                    business,
                    newJobListingTechnologies.stream().toList()
                );

                newJobListings.add(newJobListing);
            }
        }

        jobListingRepository.saveAll(newJobListings);
    }
}
