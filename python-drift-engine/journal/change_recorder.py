from datetime import datetime, timezone

def make_journal_entry(resource_id, resource_type, attribute_name, expected, actual, classification):
    return {
        "resource_id": resource_id,
        "resource_type": resource_type,
        "attribute_name": attribute_name,
        "old_value": expected,
        "new_value": actual,
        "classification": classification,
        "change_source": "AWS_SCAN",
        "recorded_at": datetime.now(timezone.utc).isoformat()
    }
