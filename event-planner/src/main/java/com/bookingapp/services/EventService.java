package com.bookingapp.services;


import com.bookingapp.dtos.EventDTO;
import com.bookingapp.entities.Event;
import com.bookingapp.repositories.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;

    }

    @Transactional
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEventFromDTO(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setMaxParticipants(eventDTO.getMaxParticipants());
        event.setEventType(eventDTO.getEventType());
        event.setPrivacyType(eventDTO.getPrivacyType());
        event.setLocation(eventDTO.getLocation());
        event.setEventDate(eventDTO.getEventDate());


        event.setAgenda(null);

        return eventRepository.save(event);
    }

}
