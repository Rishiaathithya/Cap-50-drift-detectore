package com.driftdetector.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="remediation_records")
public class RemediationRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Long driftId; private String proposedAction, status;
 @Column(columnDefinition="json") private String planJson;
 private Long approvalId;
 @Column(length=1000) private String resultMessage;
 private LocalDateTime createdAt=LocalDateTime.now(), executedAt;
 public Long getId(){return id;} public Long getDriftId(){return driftId;} public void setDriftId(Long v){driftId=v;}
 public String getProposedAction(){return proposedAction;} public void setProposedAction(String v){proposedAction=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getPlanJson(){return planJson;} public void setPlanJson(String v){planJson=v;}
 public Long getApprovalId(){return approvalId;} public void setApprovalId(Long v){approvalId=v;}
 public String getResultMessage(){return resultMessage;} public void setResultMessage(String v){resultMessage=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getExecutedAt(){return executedAt;} public void setExecutedAt(LocalDateTime v){executedAt=v;}
}
