from copy import deepcopy

def normalize(value):
    # Recursively sort dictionaries and lists of rule-like objects to make comparisons stable.
    if isinstance(value, dict):
        return {k: normalize(v) for k, v in sorted(value.items())}
    if isinstance(value, list):
        normalized = [normalize(v) for v in value]
        return sorted(normalized, key=lambda x: str(x))
    return value

def remove_ignored_fields(config: dict, ignored_fields: list[str]) -> dict:
    result = deepcopy(config)
    for field in ignored_fields:
        result.pop(field, None)
    return normalize(result)
