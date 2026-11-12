CREATE DATABASE IF NOT EXISTS cap50_drift;
USE cap50_drift;

CREATE TABLE IF NOT EXISTS baselines (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  resource_id VARCHAR(128) NOT NULL,
  resource_type VARCHAR(64) NOT NULL,
  expected_config JSON NOT NULL,
  version VARCHAR(64) NOT NULL DEFAULT '1',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_baseline_resource (resource_id, resource_type)
);

CREATE TABLE IF NOT EXISTS infrastructure_inventory (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  resource_id VARCHAR(128) NOT NULL,
  resource_type VARCHAR(64) NOT NULL,
  actual_config JSON NOT NULL,
  last_scanned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_inventory_resource (resource_id, resource_type)
);

CREATE TABLE IF NOT EXISTS drift_records (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  resource_id VARCHAR(128) NOT NULL,
  resource_type VARCHAR(64) NOT NULL,
  attribute_name VARCHAR(128) NOT NULL,
  expected_value JSON,
  actual_value JSON,
  classification VARCHAR(40) NOT NULL,
  severity VARCHAR(20) NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
  detected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_drift_status (status),
  INDEX idx_drift_resource (resource_id)
);

CREATE TABLE IF NOT EXISTS change_journal (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  drift_id BIGINT NULL,
  resource_id VARCHAR(128) NOT NULL,
  resource_type VARCHAR(64) NOT NULL,
  old_value JSON,
  new_value JSON,
  changed_by VARCHAR(128) NOT NULL DEFAULT 'SYSTEM',
  change_source VARCHAR(64) NOT NULL DEFAULT 'AWS_SCAN',
  event_description VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_journal_drift FOREIGN KEY (drift_id) REFERENCES drift_records(id)
);

CREATE TABLE IF NOT EXISTS approvals (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  drift_id BIGINT NOT NULL,
  decision VARCHAR(20) NOT NULL,
  approved_by VARCHAR(128) NOT NULL,
  comments VARCHAR(1000),
  decided_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_approval_drift FOREIGN KEY (drift_id) REFERENCES drift_records(id)
);

CREATE TABLE IF NOT EXISTS remediation_records (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  drift_id BIGINT NOT NULL,
  proposed_action VARCHAR(128) NOT NULL,
  plan_json JSON NOT NULL,
  approval_id BIGINT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
  result_message VARCHAR(1000),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  executed_at TIMESTAMP NULL,
  CONSTRAINT fk_remediation_drift FOREIGN KEY (drift_id) REFERENCES drift_records(id),
  CONSTRAINT fk_remediation_approval FOREIGN KEY (approval_id) REFERENCES approvals(id)
);
