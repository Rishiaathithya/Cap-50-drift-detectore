package com.driftdetector.controller;
import com.driftdetector.model.Baseline;
import com.driftdetector.repository.BaselineRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/baselines") @CrossOrigin(origins="http://localhost:5173")
public class BaselineController {
 private final BaselineRepository repository;
 public BaselineController(BaselineRepository r){repository=r;}
 @GetMapping public List<Baseline> all(){return repository.findAll();}
 @PostMapping public Baseline create(@RequestBody Baseline baseline){return repository.save(baseline);}
}
