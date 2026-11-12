package com.driftdetector.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="approvals")
public class Approval {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Long driftId; private String decision, approvedBy;
 @Column(length=1000) private String comments;
 private LocalDateTime decidedAt=LocalDateTime.now();
 public Long getId(){return id;} public Long getDriftId(){return driftId;} public void setDriftId(Long v){driftId=v;}
 public String getDecision(){return decision;} public void setDecision(String v){decision=v;}
 public String getApprovedBy(){return approvedBy;} public void setApprovedBy(String v){approvedBy=v;}
 public String getComments(){return comments;} public void setComments(String v){comments=v;}
 public LocalDateTime getDecidedAt(){return decidedAt;}
}
