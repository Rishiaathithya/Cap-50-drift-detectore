package com.driftdetector.controller;
import com.driftdetector.model.ChangeJournal;
import com.driftdetector.repository.JournalRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/journal") @CrossOrigin(origins="http://localhost:5173")
public class JournalController {
 private final JournalRepository repository;
 public JournalController(JournalRepository r){repository=r;}
 @GetMapping public List<ChangeJournal> all(){return repository.findAllByOrderByCreatedAtDesc();}
}
