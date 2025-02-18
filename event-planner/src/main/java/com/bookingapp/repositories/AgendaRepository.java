package com.bookingapp.repositories;

 import com.bookingapp.entities.AgendaItem;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<AgendaItem, Long> {
}