package com.driftdetector.repository;
import com.driftdetector.model.ChangeJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface JournalRepository extends JpaRepository<ChangeJournal, Long> {
 List<ChangeJournal> findAllByOrderByCreatedAtDesc();
}
