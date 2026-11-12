package com.driftdetector.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="baselines")
public class Baseline {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String resourceId, resourceType, version="1";
 @Column(columnDefinition="json") private String expectedConfig;
 private LocalDateTime createdAt=LocalDateTime.now();
 public Long getId(){return id;} public String getResourceId(){return resourceId;} public void setResourceId(String v){resourceId=v;}
 public String getResourceType(){return resourceType;} public void setResourceType(String v){resourceType=v;}
 public String getVersion(){return version;} public void setVersion(String v){version=v;}
 public String getExpectedConfig(){return expectedConfig;} public void setExpectedConfig(String v){expectedConfig=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
