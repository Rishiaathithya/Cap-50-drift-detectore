package com.driftdetector.controller;
import com.driftdetector.model.RemediationRecord;
import com.driftdetector.service.RemediationService;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/remediations") @CrossOrigin(origins="http://localhost:5173")
public class RemediationController {
 private final RemediationService service;
 public RemediationController(RemediationService s){service=s;}
 @PostMapping("/plan/{driftId}") public RemediationRecord plan(@PathVariable Long driftId){return service.createPlan(driftId);}
 @PostMapping("/{id}/execute") public RemediationRecord execute(@PathVariable Long id,@RequestParam(defaultValue="demo-admin") String requestedBy){return service.execute(id,requestedBy);}
}
