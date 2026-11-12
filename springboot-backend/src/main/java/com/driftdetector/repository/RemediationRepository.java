package com.driftdetector.repository;
import com.driftdetector.model.RemediationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RemediationRepository extends JpaRepository<RemediationRecord, Long> {
}
