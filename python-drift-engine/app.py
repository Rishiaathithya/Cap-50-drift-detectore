from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from main import run_scan
from remediation.plan_generator import generate_plan
from remediation.remediation_executor import execute_plan

app = FastAPI(title="CAP-50 Drift Detection Service", version="1.0.0")
class ExecuteRequest(BaseModel):
    plan: dict
    approved: bool = False
    simulation: bool = True
@app.get("/health")
def health():
    return {"status": "UP", "service": "python-drift-engine"}
@app.post("/scan")
def scan():
    try:
        return run_scan()
    except Exception as exc:
        raise HTTPException(status_code=500, detail=str(exc))
@app.post("/remediation/plan")
def remediation_plan(drift: dict):
    return generate_plan(drift)
@app.post("/remediation/execute")
def remediation_execute(request: ExecuteRequest):
    try:
        return execute_plan(request.plan, request.approved, request.simulation)
    except (PermissionError, NotImplementedError) as exc:
        raise HTTPException(status_code=403, detail=str(exc))
