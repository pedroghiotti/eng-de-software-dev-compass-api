package br.facens.eng_de_software.dev_compass_api.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.model.Category;
import br.facens.eng_de_software.dev_compass_api.model.CompensationPackage;
import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.model.Region;
import br.facens.eng_de_software.dev_compass_api.model.Salary;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.repository.CategoryRepository;
import br.facens.eng_de_software.dev_compass_api.repository.JobListingRepository;
import br.facens.eng_de_software.dev_compass_api.repository.RegionRepository;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import br.facens.eng_de_software.dev_compass_api.security.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
@DependsOn("userInitializer")
@Profile("dev")
public class DataInitializer {
    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private JobListingRepository jobListingRepository;
    
    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct()
    public void init() {
        var random = new Random();

        List<Region> regions = initializeRegionData();
        List<Technology> technologies = initializeTechnologyData();
        List<Category> categories = initializeCategoryData();

        Business business = (Business) userRepository.findByAuthenticationDataUsername("business").get();
        List<JobListing> newJobListings = new ArrayList<>();

        for (var region : regions) {
            for (int i = 0; i < 10; i++) {
                Set<Technology> newJobListingTechnologies = new HashSet<>();
                for (int j = 0; j < random.nextInt(1, 5); j++) {
                    newJobListingTechnologies.add(technologies.get(random.nextInt(0, technologies.size())));
                }

                Set<Category> newJobListingCategories = new HashSet<>();
                for (int j = 0; j < random.nextInt(1, 5); j++) {
                    newJobListingCategories.add(categories.get(random.nextInt(0, categories.size())));
                }

                CompensationPackage newJobListingCompensationPackage = new CompensationPackage(
                    new Salary(random.nextDouble(1500, 15000))
                );

                JobListing newJobListing = new JobListing(
                    UUID.randomUUID(),
                    String.join(" | ", newJobListingTechnologies.stream().map(Technology::getName).toList()),
                    String.join(" | ", newJobListingTechnologies.stream().map(Technology::getName).toList()),
                    region,
                    business,
                    newJobListingTechnologies,
                    newJobListingCategories,
                    newJobListingCompensationPackage
                );

                newJobListings.add(newJobListing);
            }
        }

        jobListingRepository.saveAll(newJobListings);
    }

    private List<Region> initializeRegionData() {
        List<String> regionNames = List.of("Acre", "Alagoas", "Amapa", "Amazonas", "Bahia", "Ceara", "Distrito Federal", "Espirito Santo", "Goias", "Maranhao", "Mato Grosso", "Mato Grosso do Sul", "Minas Gerais", "Para", "Paraiba", "Parana", "Pernambuco", "Piaui", "Rio de Janeiro", "Rio Grande do Norte", "Rio Grande do Sul", "Rondonia", "Roraima", "Santa Catarina", "Sao Paulo", "Sergipe", "Tocantins");
        List<Region> newRegions = regionNames.stream().map((regionName) -> new Region(UUID.randomUUID(), regionName)).toList();
        regionRepository.saveAll(newRegions);
        return newRegions;
    }

    private List<Technology> initializeTechnologyData() {
        List<String> technologyNames = List.of("Python", "JavaScript", "Java", "C#", "Ruby", "PHP", "Go", "Swift", "Kotlin", "Rust");
        List<Technology> newTechnologies = technologyNames.stream().map((technologyName) -> new Technology(UUID.randomUUID(), technologyName)).toList();
        technologyRepository.saveAll(newTechnologies);
        return newTechnologies;
    }

    private List<Category> initializeCategoryData() {
        List<String> categoryNames = List.of("FrontEnd", "BackEnd", "FullStack");
        List<Category> newCategories = categoryNames.stream().map((categoryName) -> new Category(UUID.randomUUID(), categoryName)).toList();
        categoryRepository.saveAll(newCategories);
        return newCategories;
    }
}
