package br.facens.eng_de_software.dev_compass_api.dto.response;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.facens.eng_de_software.dev_compass_api.model.Candidate;
import br.facens.eng_de_software.dev_compass_api.notifications.model.NotificationChannel;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserResponseDto;
import br.facens.eng_de_software.dev_compass_api.security.model.Role;

public class CandidateResponseDto extends BaseUserResponseDto {
    private final Set<NotificationChannel> preferredNotificationChannels;
    private final Set<TechnologyResponseDto> preferredTechnologies;
    private final Set<CategoryResponseDto> preferredCategories;
    private final RegionResponseDto preferredRegion;

    public CandidateResponseDto(
        UUID id,
        String username,
        Role role,
        Set<NotificationChannel> preferredNotificationChannels,
        Set<TechnologyResponseDto> preferredTechnologies,
        Set<CategoryResponseDto> preferredCategories,
        RegionResponseDto preferredRegion
    ) {
        super(id, username, role);
        this.preferredNotificationChannels = preferredNotificationChannels;
        this.preferredTechnologies = preferredTechnologies;
        this.preferredCategories = preferredCategories;
        this.preferredRegion = preferredRegion;
    }

    public Set<NotificationChannel> getPreferredNotificationChannels() {
        return preferredNotificationChannels;
    }

    public Set<TechnologyResponseDto> getPreferredTechnologies() {
        return preferredTechnologies;
    }

    public Set<CategoryResponseDto> getPreferredCategories() {
        return preferredCategories;
    }

    public RegionResponseDto getPreferredRegion() {
        return preferredRegion;
    }

    public static CandidateResponseDto fromUser(Candidate candidate) {
        return new CandidateResponseDto(
            candidate.getId(),
            candidate.getUsername(),
            candidate.getRole(),
            candidate.getPreferredNotificationChannels(),
            candidate.getPreferredTechnologies().stream()
                .map(TechnologyResponseDto::fromTechnology)
                .collect(Collectors.toCollection(HashSet::new)),
            candidate.getPreferredCategories().stream()
                .map(CategoryResponseDto::fromCategory)
                .collect(Collectors.toCollection(HashSet::new)),
            RegionResponseDto.fromRegion(candidate.getPreferredRegion())
        );
    }
}
