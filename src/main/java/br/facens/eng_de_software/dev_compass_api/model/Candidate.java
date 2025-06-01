package br.facens.eng_de_software.dev_compass_api.model;

import java.util.HashSet;
import java.util.Set;

import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Candidate extends BaseUser {
    @ElementCollection(targetClass = NotificationChannel.class)
    @Enumerated(EnumType.STRING)
    private Set<NotificationChannel> preferredNotificationChannels = new HashSet<>();
    
    @ManyToMany
    private Set<Technology> preferredTechnologies = new HashSet<>();
    
    @ManyToMany
    private Set<Category> preferredCategories = new HashSet<>();
    
    @ManyToOne
    private Region preferredRegion;

    public Candidate(String username, String password) {
        super(username, password, Role.CANDIDATE);
    }

    public Candidate() {
        this(null, null);
    }
}
