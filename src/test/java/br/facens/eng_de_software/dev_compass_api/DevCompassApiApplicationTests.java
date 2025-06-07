package br.facens.eng_de_software.dev_compass_api;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.facens.eng_de_software.dev_compass_api.model.Business;
import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.model.Category;
import br.facens.eng_de_software.dev_compass_api.model.CompensationPackage;
import br.facens.eng_de_software.dev_compass_api.model.JobListing;
import br.facens.eng_de_software.dev_compass_api.model.Region;
import br.facens.eng_de_software.dev_compass_api.model.Salary;
import br.facens.eng_de_software.dev_compass_api.model.Technology;
import br.facens.eng_de_software.dev_compass_api.notifications.model.NotificationChannel;
import br.facens.eng_de_software.dev_compass_api.repository.CandidateRepository;
import br.facens.eng_de_software.dev_compass_api.repository.CategoryRepository;
import br.facens.eng_de_software.dev_compass_api.repository.JobListingRepository;
import br.facens.eng_de_software.dev_compass_api.repository.RegionRepository;
import br.facens.eng_de_software.dev_compass_api.repository.TechnologyRepository;
import br.facens.eng_de_software.dev_compass_api.security.repository.UserRepository;

@SpringBootTest
class DevCompassApiApplicationTests {

	@Autowired private UserRepository userRepository;
	@Autowired private JobListingRepository jobListingRepository;
	@Autowired private CandidateRepository candidateRepository;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private TechnologyRepository technologyRepository;
	@Autowired private RegionRepository regionRepository;

	@BeforeEach
	void setUp() {
		clearDatabase();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void usersShouldBeNotifiedWhenJobListingPublishedWithMatchingRegionAndCategory() {
		// Arrange
		Region region = regionRepository.save(new Region(UUID.randomUUID(), "South America"));
		Category category = categoryRepository.save(new Category(UUID.randomUUID(), "Backend"));
		Technology tech = technologyRepository.save(new Technology(UUID.randomUUID(), "Java"));

		Business business = new Business(
			"TechCorp",
			"tc@123"
		);
		userRepository.save(business);

		Candidate matchingCandidate = new Candidate(
			"Candidate",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(),
			Set.of(category),
			region
		);
		candidateRepository.save(matchingCandidate);

		JobListing job = new JobListing(
			UUID.randomUUID(),
			"Software Engineer",
			"Develop and maintain software applications.",
			region,
			business,
			Set.of(),
			Set.of(category),
			new CompensationPackage(new Salary(5000D))
		);
		jobListingRepository.save(job);

		// Act
		Set<Candidate> notified = candidateRepository
			.findByRegionAndPreferredCategoriesOrTechnologies(region, job.getCategories(), job.getTechnologies());

		// Assert
		Assertions.assertTrue(notified.stream().anyMatch(c -> c.getId().equals(matchingCandidate.getId())));
	}

	@Test
	void usersShouldBeNotifiedWhenJobListingPublishedWithMatchingRegionAndTechnology() {
		// Arrange
		Region region = regionRepository.save(new Region(UUID.randomUUID(), "Europe"));
		Technology tech = technologyRepository.save(new Technology(UUID.randomUUID(), "Python"));
		Category category = categoryRepository.save(new Category(UUID.randomUUID(), "AI"));

		Candidate candidate = new Candidate(
			"Candidate",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(tech),
			Set.of(),
			region
		);
		candidateRepository.save(candidate);

		
		Business business = new Business(
			"TechCorp",
			"tc@123"
		);
		userRepository.save(business);

		JobListing job = new JobListing(
			UUID.randomUUID(),
			"Data Scientist",
			"Analyze data and build models.",
			region,
			business,
			Set.of(tech),
			Set.of(category),
			new CompensationPackage(new Salary(5000D))
		);
		jobListingRepository.save(job);

		// Act
		Set<Candidate> notified = candidateRepository
			.findByRegionAndPreferredCategoriesOrTechnologies(region, job.getCategories(), job.getTechnologies());

		// Assert
		Assertions.assertTrue(notified.stream().anyMatch(c -> c.getId().equals(candidate.getId())));
	}

	@Test
	void usersShouldBeNotifiedWhenJobListingPublishedWithMatchingRegionAndCategoryAndTechnology() {
		// Arrange
		Region region = regionRepository.save(new Region(UUID.randomUUID(), "North America"));
		Technology tech = technologyRepository.save(new Technology(UUID.randomUUID(), "React"));
		Category category = categoryRepository.save(new Category(UUID.randomUUID(), "Frontend"));

		Candidate candidate = new Candidate(
			"Candidate",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(tech),
			Set.of(category),
			region
		);
		candidateRepository.save(candidate);

		Business business = new Business(
			"TechCorp",
			"tc@123"
		);
		userRepository.save(business);

		JobListing job = new JobListing(
			UUID.randomUUID(),
			"Frontend Developer",
			"Develop user-facing applications.",
			region,
			business,
			Set.of(tech),
			Set.of(category),
			new CompensationPackage(new Salary(6000D))
		);
		jobListingRepository.save(job);

		// Act
		Set<Candidate> notified = candidateRepository
			.findByRegionAndPreferredCategoriesOrTechnologies(region, job.getCategories(), job.getTechnologies());

		// Assert
		Assertions.assertTrue(notified.stream().anyMatch(c -> c.getId().equals(candidate.getId())));
	}

	@Test
	void usersShouldNotBeNotifiedWhenJobListingPublishedWithMismatchingRegionAndMatchingCategory() {
		// Arrange
		Region candidateRegion = regionRepository.save(new Region(UUID.randomUUID(), "Asia"));
		Region jobRegion = regionRepository.save(new Region(UUID.randomUUID(), "Africa"));
		Category category = categoryRepository.save(new Category(UUID.randomUUID(), "DevOps"));

		Candidate candidate = new Candidate(
			"Candidate",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(),
			Set.of(category),
			candidateRegion
		);
		candidateRepository.save(candidate);

		Business business = new Business(
			"TechCorp",
			"tc@123"
		);
		userRepository.save(business);

		JobListing job = new JobListing(
			UUID.randomUUID(),
			"DevOps Engineer",
			"Implement and manage CI/CD pipelines.",
			jobRegion,
			business,
			Set.of(),
			Set.of(category),
			new CompensationPackage(new Salary(7000D))
		);
		jobListingRepository.save(job);

		// Act
		Set<Candidate> notified = candidateRepository
			.findByRegionAndPreferredCategoriesOrTechnologies(jobRegion, job.getCategories(), job.getTechnologies());

		// Assert
		Assertions.assertFalse(notified.contains(candidate));
	}

	@Test
	void usersShouldNotBeNotifiedWhenJobListingPublishedWithMismatchingRegionAndMatchingTechnology() {
		// Arrange
		Region candidateRegion = regionRepository.save(new Region(UUID.randomUUID(), "Asia"));
		Region jobRegion = regionRepository.save(new Region(UUID.randomUUID(), "Europe"));
		Technology tech = technologyRepository.save(new Technology(UUID.randomUUID(), "Kotlin"));

		Candidate candidate = new Candidate();
		candidate.setPreferredRegion(candidateRegion);
		candidate.setPreferredTechnologies(Set.of(tech));
		candidate.setPreferredCategories(Set.of());
		candidateRepository.save(candidate);

		Business business = new Business(
			"TechCorp",
			"tc@123"
		);
		userRepository.save(business);

		JobListing job = new JobListing(
			UUID.randomUUID(),
			"Mobile Developer",
			"Develop Android applications.",
			jobRegion,
			business,
			Set.of(tech),
			Set.of(),
			new CompensationPackage(new Salary(6000D))
		);
		jobListingRepository.save(job);

		// Act
		Set<Candidate> notified = candidateRepository
			.findByRegionAndPreferredCategoriesOrTechnologies(jobRegion, job.getCategories(), job.getTechnologies());

		// Assert
		Assertions.assertFalse(notified.stream().anyMatch(c -> c.getId().equals(candidate.getId())));
	}

	@Test
	void usersShouldNotBeNotifiedWhenJobListingPublishedWithMismatchingRegionAndMatchingCategoryAndTechnology() {
		// Arrange
		Region candidateRegion = regionRepository.save(new Region(UUID.randomUUID(), "Oceania"));
		Region jobRegion = regionRepository.save(new Region(UUID.randomUUID(), "Middle East"));
		Category category = categoryRepository.save(new Category(UUID.randomUUID(), "Data Science"));
		Technology tech = technologyRepository.save(new Technology(UUID.randomUUID(), "R"));

		Candidate candidate = new Candidate(
			"Candidate",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(tech),
			Set.of(category),
			candidateRegion
		);
		candidateRepository.save(candidate);
		
		Business business = new Business(
			"TechCorp",
			"tc@123"
		);
		userRepository.save(business);

		JobListing job = new JobListing(
			UUID.randomUUID(),
			"Data Scientist",
			"Analyze and interpret complex data.",
			jobRegion,
			business,
			Set.of(tech),
			Set.of(category),
			new CompensationPackage(new Salary(8000D))
		);
		jobListingRepository.save(job);

		// Act
		Set<Candidate> notified = candidateRepository
			.findByRegionAndPreferredCategoriesOrTechnologies(jobRegion, job.getCategories(), job.getTechnologies());

		// Assert
		Assertions.assertFalse(notified.stream().anyMatch(c -> c.getId().equals(candidate.getId())));
	}

	@Test
	void usersShouldNotBeNotifiedWhenJobListingHasNoCategoryOrTechnology() {
		// Arrange
		Region region = regionRepository.save(new Region(UUID.randomUUID(), "Antarctica"));
		Category category = categoryRepository.save(new Category(UUID.randomUUID(), "AI"));
		Technology tech = technologyRepository.save(new Technology(UUID.randomUUID(), "Go"));

		Candidate candidate = new Candidate(
			"Candidate",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(tech),
			Set.of(category),
			region
		);
		candidateRepository.save(candidate);

		Business business = new Business(
			"TechCorp",
			"tc@123"
		);
		userRepository.save(business);

		JobListing job = new JobListing(
			UUID.randomUUID(),
			"AI Researcher",
			"Conduct research in artificial intelligence.",
			region,
			business,
			Set.of(),
			Set.of(),
			new CompensationPackage(new Salary(9000D))
		);
		jobListingRepository.save(job);

		// Act
		Set<Candidate> notified = candidateRepository
			.findByRegionAndPreferredCategoriesOrTechnologies(region, job.getCategories(), job.getTechnologies());

		// Assert
		Assertions.assertFalse(notified.stream().anyMatch(c -> c.getId().equals(candidate.getId())));
	}

	@Test
	void usersShouldNotBeNotifiedWhenCandidateHasNoPreferences() {
		// Arrange
		Region region = regionRepository.save(new Region(UUID.randomUUID(), "South Pole"));
		Category category = categoryRepository.save(new Category(UUID.randomUUID(), "Cloud"));
		Technology tech = technologyRepository.save(new Technology(UUID.randomUUID(), "Azure"));

		Candidate candidate = new Candidate(
			"Candidate",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(),
			Set.of(),
			region
		);
		candidateRepository.save(candidate);

		Business business = new Business(
			"TechCorp",
			"tc@123"
		);
		userRepository.save(business);

		JobListing job = new JobListing(
			UUID.randomUUID(),
			"Cloud Engineer",
			"Design and implement cloud solutions.",
			region,
			business,
			Set.of(tech),
			Set.of(category),
			new CompensationPackage(new Salary(9500D))
		);
		jobListingRepository.save(job);

		// Act
		Set<Candidate> notified = candidateRepository
			.findByRegionAndPreferredCategoriesOrTechnologies(region, job.getCategories(), job.getTechnologies());

		// Assert
		Assertions.assertFalse(notified.stream().anyMatch(c -> c.getId().equals(candidate.getId())));
	}

	@Test
	void onlyMatchingCandidatesShouldBeNotified() {
		// Arrange
		Region region = regionRepository.save(new Region(UUID.randomUUID(), "Global"));
		Category category = categoryRepository.save(new Category(UUID.randomUUID(), "Security"));
		Technology tech = technologyRepository.save(new Technology(UUID.randomUUID(), "Rust"));

		Candidate matchingByCategory = new Candidate(
			"Candidate1",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(),
			Set.of(category),
			region
		);
		candidateRepository.save(matchingByCategory);

		Candidate matchingByTechnology = new Candidate(
			"Candidate2",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(tech),
			Set.of(),
			region
		);
		candidateRepository.save(matchingByTechnology);

		Candidate nonMatchingRegion = new Candidate(
			"Candidate",
			"candidate@123",
			Set.of(NotificationChannel.EMAIL),
			Set.of(tech),
			Set.of(category),
			regionRepository.save(new Region(UUID.randomUUID(), "Mars"))
		);
		candidateRepository.save(nonMatchingRegion);

		Business business = new Business(
			"TechCorp",
			"tc@123"
		);
		userRepository.save(business);

		JobListing job = new JobListing(
			UUID.randomUUID(),
			"Cloud Engineer",
			"Design and implement cloud solutions.",
			region,
			business,
			Set.of(tech),
			Set.of(category),
			new CompensationPackage(new Salary(9500D))
		);
		jobListingRepository.save(job);

		// Act
		Set<Candidate> notified = candidateRepository
			.findByRegionAndPreferredCategoriesOrTechnologies(region, job.getCategories(), job.getTechnologies());

		// Assert
		Assertions.assertTrue(notified.stream().anyMatch(c -> c.getId().equals(matchingByCategory.getId())));
		Assertions.assertTrue(notified.stream().anyMatch(c -> c.getId().equals(matchingByTechnology.getId())));
		Assertions.assertFalse(notified.stream().anyMatch(c -> c.getId().equals(nonMatchingRegion.getId())));
	}

	public void clearDatabase() {
		jobListingRepository.deleteAll();
		candidateRepository.deleteAll();
		categoryRepository.deleteAll();
		technologyRepository.deleteAll();
		regionRepository.deleteAll();
		userRepository.deleteAll();
	}
}
