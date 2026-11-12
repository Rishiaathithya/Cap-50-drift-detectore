package com.driftdetector.service;
import com.driftdetector.model.*;
import com.driftdetector.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import java.util.*;
@Service
public class DriftService {
 private final RestClient client; private final DriftRepository drifts; private final JournalRepository journal;
 public DriftService(RestClient client, DriftRepository drifts, JournalRepository journal){this.client=client;this.drifts=drifts;this.journal=journal;}
 @Transactional
 public Map scan(String pythonUrl) {
   Map result=client.post().uri(pythonUrl+"/scan").retrieve().body(Map.class);
   List<Map> findings=(List<Map>)result.getOrDefault("findings", List.of());
   List<Drift> saved=new ArrayList<>();
   for(Map f:findings){
     Drift d=new Drift(); d.setResourceId(String.valueOf(f.get("resource_id"))); d.setResourceType(String.valueOf(f.get("resource_type")));
     d.setAttributeName(String.valueOf(f.get("attribute_name"))); d.setClassification(String.valueOf(f.get("classification")));
     d.setSeverity(String.valueOf(f.get("severity"))); d.setStatus("OPEN");
     d.setExpectedValue(toJson(f.get("expected_value"))); d.setActualValue(toJson(f.get("actual_value")));
     saved.add(drifts.save(d));
     ChangeJournal j=new ChangeJournal(); j.setDriftId(d.getId()); j.setResourceId(d.getResourceId()); j.setResourceType(d.getResourceType());
     j.setOldValue(d.getExpectedValue()); j.setNewValue(d.getActualValue()); j.setChangedBy("SYSTEM");
     j.setChangeSource("AWS_SCAN"); j.setEventDescription("Drift detected: "+d.getAttributeName()); journal.save(j);
   }
   result.put("savedFindings", saved); return result;
 }
 private String toJson(Object value){
   try{return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(value);}
   catch(Exception e){return value==null?"null":"\""+value.toString().replace("\"","\\\"")+"\"";}
 }
}
