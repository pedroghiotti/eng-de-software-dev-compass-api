package br.facens.eng_de_software.dev_compass_api.dto.request;

import java.util.Set;
import java.util.UUID;

import br.facens.eng_de_software.dev_compass_api.notifications.model.NotificationChannel;
import br.facens.eng_de_software.dev_compass_api.security.dto.BaseUserEditorDto;
import lombok.Getter;

@Getter
public class CandidateEditorDto extends BaseUserEditorDto {
    private final Set<NotificationChannel> preferredNotificationChannels;
    private final Set<UUID> preferredTechnologyIds;
    private final Set<UUID> preferredCategoryIds;
    private final UUID preferredRegionId;

    public CandidateEditorDto(
        String username,
        String password,
        Set<NotificationChannel> preferredNotificationChannels,
        Set<UUID> preferredTechnologyIds,
        Set<UUID> preferredCategoryIds,
        UUID preferredRegionId
    ) {
        super(username, password);
        this.preferredNotificationChannels = preferredNotificationChannels;
        this.preferredTechnologyIds = preferredTechnologyIds;
        this.preferredCategoryIds = preferredCategoryIds;
        this.preferredRegionId = preferredRegionId;
    }    
}
