package br.facens.eng_de_software.dev_compass_api.model;

import java.util.HashSet;
import java.util.Set;

import br.facens.eng_de_software.dev_compass_api.notifications.model.NotificationChannel;
import br.facens.eng_de_software.dev_compass_api.security.model.BaseUser;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Candidate extends BaseUser {
    @ElementCollection(targetClass = NotificationChannel.class)
    @Enumerated(EnumType.STRING)
    private Set<NotificationChannel> preferredNotificationChannels;
    
    @ManyToMany
    private Set<Technology> preferredTechnologies;
    
    @ManyToMany
    private Set<Category> preferredCategories;
    
    @ManyToOne
    private Region preferredRegion;

    public Candidate(
        String username,
        String password,
        Set<NotificationChannel> preferredNotificationChannels,
        Set<Technology> preferredTechnologies,
        Set<Category> preferredCategories,
        Region preferredRegion
    ) {
        super(username, password, Role.CANDIDATE);
        this.preferredNotificationChannels = preferredNotificationChannels;
        this.preferredTechnologies = preferredTechnologies;
        this.preferredCategories = preferredCategories;
        this.preferredRegion = preferredRegion;
    }

    public Candidate() {
        this(null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), null);
    }
}
