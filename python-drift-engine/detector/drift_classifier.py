def classify_drift(resource_type: str, differences: list[dict]) -> dict:
    if not differences:
        return {"classification": "NO_DRIFT", "severity": "INFO"}
    names = {item["attribute_name"] for item in differences}
    if resource_type == "SECURITY_GROUP" and "inbound_rules" in names:
        # Any unapproved inbound-rule difference is treated conservatively as high risk.
        return {"classification": "UNAUTHORIZED", "severity": "HIGH"}
    if resource_type == "EC2" and names <= {"instance_type", "state"}:
        return {"classification": "REVIEW_REQUIRED", "severity": "MEDIUM"}
    return {"classification": "REVIEW_REQUIRED", "severity": "MEDIUM"}
