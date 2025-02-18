package com.bookingapp.controllers;

 import com.bookingapp.dtos.EventDTO;
 import com.bookingapp.entities.Event;
 import com.bookingapp.services.EventService;
 import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    public EventController(EventService events) {
        this.eventService = events;

    }


    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventDTO eventDTO) {
        Event event = eventService.createEventFromDTO(eventDTO);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.getEventById(id);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}