package com.driftdetector.repository;
import com.driftdetector.model.Drift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DriftRepository extends JpaRepository<Drift, Long> {
 List<Drift> findByStatusOrderByDetectedAtDesc(String status);
}
