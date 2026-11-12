package com.driftdetector.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="change_journal")
public class ChangeJournal {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Long driftId; private String resourceId, resourceType, changedBy, changeSource;
 @Column(columnDefinition="json") private String oldValue;
 @Column(columnDefinition="json") private String newValue;
 @Column(length=500) private String eventDescription;
 private LocalDateTime createdAt=LocalDateTime.now();
 public Long getId(){return id;} public Long getDriftId(){return driftId;} public void setDriftId(Long v){driftId=v;}
 public String getResourceId(){return resourceId;} public void setResourceId(String v){resourceId=v;}
 public String getResourceType(){return resourceType;} public void setResourceType(String v){resourceType=v;}
 public String getChangedBy(){return changedBy;} public void setChangedBy(String v){changedBy=v;}
 public String getChangeSource(){return changeSource;} public void setChangeSource(String v){changeSource=v;}
 public String getOldValue(){return oldValue;} public void setOldValue(String v){oldValue=v;}
 public String getNewValue(){return newValue;} public void setNewValue(String v){newValue=v;}
 public String getEventDescription(){return eventDescription;} public void setEventDescription(String v){eventDescription=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
