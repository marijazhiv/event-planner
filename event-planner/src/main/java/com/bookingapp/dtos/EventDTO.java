package com.bookingapp.dtos;


 import com.bookingapp.entities.Event;
 import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventDTO {
    private String name;
    private String description;
    private int maxParticipants;
    private String eventType;
    private String privacyType;
    private String location;
    private String eventDate;
    private List<Long> agendaItemIds;

    public EventDTO(Event event) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.maxParticipants = event.getMaxParticipants();
        this.eventType = event.getEventType();
        this.privacyType = event.getPrivacyType();
        this.location = event.getLocation();
        this.eventDate = event.getEventDate();
        this.agendaItemIds = event.getAgenda() != null ?
                event.getAgenda().stream().map(agendaItem -> agendaItem.getId()).toList() :
                null;
    }

    public EventDTO(String name, String description, int maxParticipants, String eventType,
                    String privacyType, String location, String eventDate, List<Long> agendaItemIds) {
        this.name = name;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.eventType = eventType;
        this.privacyType = privacyType;
        this.location = location;
        this.eventDate = eventDate;
        this.agendaItemIds = agendaItemIds;
    }

    public EventDTO() {
    }
}
