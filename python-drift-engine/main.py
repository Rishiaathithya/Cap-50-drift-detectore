import os
from collectors.ec2_collector import collect_ec2
from collectors.vpc_collector import collect_security_groups
from detector.baseline_loader import load_baseline
from detector.comparison_engine import compare_resource
from detector.drift_classifier import classify_drift

def simulated_inventory():
    # Stable demo IDs make it possible to compare against the sample baseline.
    return {
        "ec2": [{
            "resource_id": "i-demo-001",
            "instance_type": "t2.micro",
            "state": "running",
            "launch_time": "2026-01-01T00:00:00+00:00",
            "private_ip_address": "10.0.1.10",
            "public_ip_address": None,
            "required_tags": {"Environment": "Development", "ManagedBy": "CAP-50"}
        }],
        "security_groups": [{
            "resource_id": "sg-demo-001",
            "vpc_id": "vpc-demo-001",
            "inbound_rules": [
                {"protocol": "tcp", "from_port": 80, "to_port": 80, "cidr": "0.0.0.0/0"},
                {"protocol": "tcp", "from_port": 22, "to_port": 22, "cidr": "0.0.0.0/0"}
            ]
        }]
    }

def run_scan():
    # simulate = os.getenv("SIMULATE_AWS", "true").lower() == "true"
    simulate = False
    inventory = simulated_inventory() if simulate else {
        "ec2": collect_ec2(),
        "security_groups": collect_security_groups()
    }
    findings = []
    ec2_baseline = load_baseline("ec2-baseline.json")
    vpc_baseline = load_baseline("vpc-baseline.json")

    for expected in ec2_baseline.get("resources", []):
        actual = next((x for x in inventory["ec2"] if x["resource_id"] == expected["resource_id"]), None)
        if actual is None:
            findings.append({
                "resource_id": expected["resource_id"], "resource_type": "EC2",
                "attribute_name": "resource_presence", "expected_value": "present",
                "actual_value": "missing", "classification": "REVIEW_REQUIRED",
                "severity": "HIGH"
            })
            continue
        ignored = expected.get("comparison", {}).get("ignore_fields", [])
        diffs = compare_resource(
            {k: v for k, v in expected.items() if k not in ("resource_id", "logical_name", "comparison")},
            {k: v for k, v in actual.items() if k != "resource_id"},
            ignored
        )
        classification = classify_drift("EC2", diffs)
        for diff in diffs:
            findings.append({
                "resource_id": expected["resource_id"], "resource_type": "EC2",
                **diff, **classification
            })

    for expected in vpc_baseline.get("resources", []):
        actual = next((x for x in inventory["security_groups"] if x["resource_id"] == expected["resource_id"]), None)
        if actual is None:
            findings.append({
                "resource_id": expected["resource_id"], "resource_type": "SECURITY_GROUP",
                "attribute_name": "resource_presence", "expected_value": "present",
                "actual_value": "missing", "classification": "REVIEW_REQUIRED",
                "severity": "HIGH"
            })
            continue
        diffs = compare_resource(
            {k: v for k, v in expected.items() if k not in ("resource_id", "logical_name", "comparison")},
            {k: v for k, v in actual.items() if k != "resource_id"},
            expected.get("comparison", {}).get("ignore_fields", [])
        )
        classification = classify_drift("SECURITY_GROUP", diffs)
        for diff in diffs:
            findings.append({
                "resource_id": expected["resource_id"], "resource_type": "SECURITY_GROUP",
                **diff, **classification
            })

    # Report resources absent from the baseline for human investigation.
    # Unknown resources are never deleted or remediated automatically.
    expected_ec2_ids = {x["resource_id"] for x in ec2_baseline.get("resources", [])}
    for actual in inventory["ec2"]:
        if actual["resource_id"] not in expected_ec2_ids:
            findings.append({
                "resource_id": actual["resource_id"], "resource_type": "EC2",
                "attribute_name": "unknown_resource", "expected_value": None,
                "actual_value": actual, "classification": "UNKNOWN_RESOURCE",
                "severity": "MEDIUM"
            })

    expected_sg_ids = {x["resource_id"] for x in vpc_baseline.get("resources", [])}
    for actual in inventory["security_groups"]:
        if actual["resource_id"] not in expected_sg_ids:
            findings.append({
                "resource_id": actual["resource_id"], "resource_type": "SECURITY_GROUP",
                "attribute_name": "unknown_resource", "expected_value": None,
                "actual_value": actual, "classification": "UNKNOWN_RESOURCE",
                "severity": "MEDIUM"
            })

    return {
        "mode": "SIMULATION" if simulate else "AWS",
        "resources_scanned": len(inventory["ec2"]) + len(inventory["security_groups"]),
        "findings": findings
    }
