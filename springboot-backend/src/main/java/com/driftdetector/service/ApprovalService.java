package com.driftdetector.service;
import com.driftdetector.model.*;
import com.driftdetector.repository.*;
import org.springframework.stereotype.Service;
import java.util.Map;
@Service
public class ApprovalService {
 private final ApprovalRepository approvals; private final DriftRepository drifts; private final JournalRepository journal;
 public ApprovalService(ApprovalRepository a, DriftRepository d, JournalRepository j){approvals=a;drifts=d;journal=j;}
 public Approval decide(Long driftId, Map<String,String> body){
   Drift drift=drifts.findById(driftId).orElseThrow();
   String decision=body.getOrDefault("decision","").toUpperCase();
   if(!decision.equals("APPROVED") && !decision.equals("REJECTED")) throw new IllegalArgumentException("decision must be APPROVED or REJECTED");
   Approval a=new Approval(); a.setDriftId(driftId); a.setDecision(decision);
   a.setApprovedBy(body.getOrDefault("approvedBy","demo-admin")); a.setComments(body.getOrDefault("comments",""));
   approvals.save(a); drift.setStatus(decision); drifts.save(drift);
   ChangeJournal j=new ChangeJournal(); j.setDriftId(driftId); j.setResourceId(drift.getResourceId()); j.setResourceType(drift.getResourceType());
   j.setChangedBy(a.getApprovedBy()); j.setChangeSource("APPROVAL_WORKFLOW"); j.setEventDescription("Remediation review decision: "+decision); journal.save(j);
   return a;
 }
}
