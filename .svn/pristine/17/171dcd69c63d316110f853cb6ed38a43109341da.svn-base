package com.hyva.restopos.rest.repository;


import com.hyva.restopos.rest.entities.JournalEntry;
import com.hyva.restopos.rest.entities.JournalEntryDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalEntryDetailsRepository extends JpaRepository<JournalEntryDetails, Long> {
List<JournalEntryDetails> findAllByJeId(JournalEntry journalEntry);
}
