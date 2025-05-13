package br.facens.eng_de_software.dev_compass_api.model;

import java.util.HashSet;
import java.util.Set;

import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Business extends BaseUser {
    @OneToMany(mappedBy = "business")
    private final Set<JobListing> managedJobListings;

    public Business(String username, String password) {
        super(username, password, Role.BUSINESS);
        managedJobListings = new HashSet<>();
    }

    public Business() {
        this(null, null);
    }

    public Set<JobListing> getManagedJobListings() {
        return this.managedJobListings;
    }

    public void addManagedJobListing(JobListing jobListing) {
        this.managedJobListings.add(jobListing);
    }
}
