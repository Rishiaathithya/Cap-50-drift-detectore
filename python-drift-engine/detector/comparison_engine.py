from .normalizer import normalize, remove_ignored_fields

def compare_resource(expected: dict, actual: dict, ignored_fields=None) -> list[dict]:
    ignored_fields = ignored_fields or []
    expected_clean = remove_ignored_fields(expected, ignored_fields)
    actual_clean = remove_ignored_fields(actual, ignored_fields)
    differences = []
    for key in sorted(set(expected_clean) | set(actual_clean)):
        old = expected_clean.get(key)
        new = actual_clean.get(key)
        if normalize(old) != normalize(new):
            differences.append({
                "attribute_name": key,
                "expected_value": old,
                "actual_value": new
            })
    return differences
