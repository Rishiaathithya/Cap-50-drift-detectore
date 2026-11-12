package com.driftdetector.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="drift_records")
public class Drift {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String resourceId, resourceType, attributeName, classification, severity, status;
 @Column(columnDefinition="json") private String expectedValue;
 @Column(columnDefinition="json") private String actualValue;
 private LocalDateTime detectedAt = LocalDateTime.now();
 public Long getId(){return id;} public String getResourceId(){return resourceId;} public void setResourceId(String v){resourceId=v;}
 public String getResourceType(){return resourceType;} public void setResourceType(String v){resourceType=v;}
 public String getAttributeName(){return attributeName;} public void setAttributeName(String v){attributeName=v;}
 public String getClassification(){return classification;} public void setClassification(String v){classification=v;}
 public String getSeverity(){return severity;} public void setSeverity(String v){severity=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getExpectedValue(){return expectedValue;} public void setExpectedValue(String v){expectedValue=v;}
 public String getActualValue(){return actualValue;} public void setActualValue(String v){actualValue=v;}
 public LocalDateTime getDetectedAt(){return detectedAt;}
}
