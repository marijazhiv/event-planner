package com.bookingapp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(nullable=false)
    private String name;

    //@NotEmpty
    //@Column(nullable=false)
    private String description;

    //@Min(value = 1)
    //@Column(nullable=false)
    private int maxParticipants;

    //@Column(nullable=false)
    private String eventType;

    //@Column(nullable=false)
    private String privacyType; // "OPEN" or "CLOSED"

    //@Column(nullable=false)
    private String location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String eventDate; // ili LocalDate ako zelis bolji format

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<AgendaItem> agenda;

    public Event(String name, String description, int maxParticipants, String eventType,
                 String privacyType, String location, String eventDate, List<AgendaItem> agenda) {
        //this.id = id;
        this.name = name;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.eventType = eventType;
        this.privacyType = privacyType;
        this.location = location;
        this.eventDate = eventDate;
        this.agenda = agenda;
    }

    public Event() {

    }
}
