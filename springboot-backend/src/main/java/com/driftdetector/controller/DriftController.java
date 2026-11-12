package com.driftdetector.controller;
import com.driftdetector.model.Drift;
import com.driftdetector.repository.DriftRepository;
import com.driftdetector.service.DriftService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/drifts") @CrossOrigin(origins="http://localhost:5173")
public class DriftController {
 private final DriftService service; private final DriftRepository repository;
 @Value("${python.service.url}") private String pythonUrl;
 public DriftController(DriftService s, DriftRepository r){service=s;repository=r;}
 @PostMapping("/scan") public Map scan(){return service.scan(pythonUrl);}
 @GetMapping public List<Drift> all(){return repository.findAll();}
 @GetMapping("/{id}") public Drift one(@PathVariable Long id){return repository.findById(id).orElseThrow();}
}
