# import json
# import os
# from pathlib import Path

# BASELINE_DIR = Path(os.getenv("BASELINE_DIR", "../baseline")).resolve()

# def load_baseline(filename: str) -> dict:
#     path = BASELINE_DIR / filename
#     if not path.exists():
#         raise FileNotFoundError(f"Baseline file not found: {path}")
#     with path.open("r", encoding="utf-8") as handle:
#         return json.load(handle)
import json
import os
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parents[2]

BASELINE_DIR = Path(
    os.getenv("BASELINE_DIR", str(PROJECT_ROOT / "baseline"))
).resolve()


def load_baseline(filename: str) -> dict:
    path = BASELINE_DIR / filename

    if not path.exists():
        raise FileNotFoundError(
            f"Baseline file not found: {path}"
        )

    with path.open("r", encoding="utf-8") as handle:
        return json.load(handle)