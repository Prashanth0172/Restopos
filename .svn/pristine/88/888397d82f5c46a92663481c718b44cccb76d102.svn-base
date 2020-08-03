package com.hyva.restopos.rest.repository;


import com.hyva.restopos.rest.entities.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    JournalEntry findByJeNo(String orNo);
    List<JournalEntry> findAllByStatus(String status);
    List<JournalEntry> findAllByJeNoContaining(String status);

}
