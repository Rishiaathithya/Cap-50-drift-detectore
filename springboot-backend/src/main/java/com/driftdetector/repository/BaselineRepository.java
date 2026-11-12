package com.driftdetector.repository;
import com.driftdetector.model.Baseline;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BaselineRepository extends JpaRepository<Baseline, Long> {
}
