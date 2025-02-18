package com.bookingapp.services;

 import com.bookingapp.entities.AgendaItem;
 import com.bookingapp.repositories.AgendaRepository;
 import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;

    }
    public AgendaItem createAgendaItem(AgendaItem agendaItem) {
        return agendaRepository.save(agendaItem);
    }

    public List<AgendaItem> getAllAgendaItems() {
        return agendaRepository.findAll();
    }
}
