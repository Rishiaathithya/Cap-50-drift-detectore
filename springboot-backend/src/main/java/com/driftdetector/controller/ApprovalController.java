package com.driftdetector.controller;
import com.driftdetector.model.Approval;
import com.driftdetector.service.ApprovalService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/approvals") @CrossOrigin(origins="http://localhost:5173")
public class ApprovalController {
 private final ApprovalService service;
 public ApprovalController(ApprovalService s){service=s;}
 @PostMapping("/{driftId}") public Approval decide(@PathVariable Long driftId,@RequestBody Map<String,String> body){return service.decide(driftId,body);}
}
