package com.driftdetector.service;
import com.driftdetector.model.*;
import com.driftdetector.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.*;
import java.time.LocalDateTime;
@Service
public class RemediationService {
 private final RestClient client; private final DriftRepository drifts; private final ApprovalRepository approvals; private final RemediationRepository records; private final JournalRepository journal;
 @org.springframework.beans.factory.annotation.Value("${python.service.url}") private String pythonUrl;
 public RemediationService(RestClient c, DriftRepository d, ApprovalRepository a, RemediationRepository r, JournalRepository j){client=c;drifts=d;approvals=a;records=r;journal=j;}
 public RemediationRecord createPlan(Long driftId){
   Drift d=drifts.findById(driftId).orElseThrow();
   Map<String,Object> payload=Map.of("resource_id",d.getResourceId(),"resource_type",d.getResourceType(),"attribute_name",d.getAttributeName());
   Map plan=client.post().uri(pythonUrl+"/remediation/plan").body(payload).retrieve().body(Map.class);
   RemediationRecord r=new RemediationRecord(); r.setDriftId(driftId); r.setProposedAction(String.valueOf(plan.get("action")));
   try{r.setPlanJson(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(plan));}catch(Exception e){throw new RuntimeException(e);}
   r.setStatus("PENDING_APPROVAL"); return records.save(r);
 }
 public RemediationRecord execute(Long remediationId, String requestedBy){
   RemediationRecord r=records.findById(remediationId).orElseThrow();
   Approval approval=approvals.findByDriftIdOrderByDecidedAtDesc(r.getDriftId()).stream()
      .filter(a->"APPROVED".equals(a.getDecision())).findFirst()
      .orElseThrow(()->new IllegalStateException("No approval exists for this remediation."));
   Map result=client.post().uri(pythonUrl+"/remediation/execute")
      .body(Map.of("plan",Map.of("action",r.getProposedAction()),"approved",true,"simulation",true))
      .retrieve().body(Map.class);
   r.setApprovalId(approval.getId()); r.setStatus(String.valueOf(result.get("status")));
   r.setResultMessage(String.valueOf(result.get("message"))); r.setExecutedAt(LocalDateTime.now()); records.save(r);
   Drift d=drifts.findById(r.getDriftId()).orElseThrow();
   ChangeJournal j=new ChangeJournal(); j.setDriftId(d.getId()); j.setResourceId(d.getResourceId()); j.setResourceType(d.getResourceType());
   j.setChangedBy(requestedBy); j.setChangeSource("REMEDIATION"); j.setEventDescription("Remediation execution: "+r.getStatus()); journal.save(j);
   return r;
 }
}
