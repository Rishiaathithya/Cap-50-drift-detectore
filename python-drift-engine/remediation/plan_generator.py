def generate_plan(drift: dict) -> dict:
    if drift.get("resource_type") == "SECURITY_GROUP" and drift.get("attribute_name") == "inbound_rules":
        return {
            "action": "REVIEW_AND_RESTORE_INBOUND_RULES",
            "description": "Review the unexpected inbound rule and restore the approved inbound rule set.",
            "safety_note": "This plan does not execute automatically. Confirm business impact before approval."
        }
    return {
        "action": "MANUAL_REVIEW",
        "description": "Inspect the resource and decide on a corrective action.",
        "safety_note": "No infrastructure change will be made by generating this plan."
    }
