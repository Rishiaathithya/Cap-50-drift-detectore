# CAP-50 – Deployment Drift Detector & Infrastructure Change Journal

A prototype that compares an approved infrastructure baseline with the observed AWS state,
classifies configuration drift, records an audit journal, and requires human approval before
any remediation is executed.

## Architecture
- `python-drift-engine`: FastAPI + Boto3 collection, normalization, comparison, classification,
  remediation planning and execution.
- `springboot-backend`: Spring Boot REST API, approval workflow and MySQL persistence.
- `react-frontend`: React/Vite dashboard.
- `baseline`: JSON baseline manifests.
- `database`: MySQL schema.
- `.github/workflows`: scheduled/manual drift scan workflow.

## Prerequisites
- Python 3.11+
- Java 17+
- Maven 3.9+
- Node.js 20+
- MySQL 8+
- Optional AWS credentials with read-only EC2/VPC permissions for scanning.

## 1. Database
Create a MySQL database and run:
```bash
mysql -u root -p < database/schema.sql
```
Set `DB_USERNAME` and `DB_PASSWORD` for the Spring Boot backend.

## 2. Python service
```bash
cd python-drift-engine
python -m venv .venv
# Windows: .venv\Scripts\activate
# macOS/Linux: source .venv/bin/activate
pip install -r requirements.txt
```
For a no-AWS demonstration:
```bash
SIMULATE_AWS=true uvicorn app:app --reload --port 8001
```
For AWS collection, configure credentials using an IAM role or AWS profile and set:
```bash
SIMULATE_AWS=false
AWS_REGION=ap-south-1
BASELINE_DIR=../baseline
```
Then start the service:
```bash
uvicorn app:app --reload --port 8001
```
Swagger docs: http://localhost:8001/docs

## 3. Spring Boot backend
```bash
cd springboot-backend
# Set DB_USERNAME and DB_PASSWORD in your environment
mvn spring-boot:run
```
Backend: http://localhost:8080

## 4. React frontend
```bash
cd react-frontend
npm install
npm run dev
```
Frontend: http://localhost:5173

## Demo workflow
1. Start Python, Spring Boot and React.
2. Click **Run scan** on the dashboard.
3. In simulation mode, the service returns sample resources and one unauthorized security-group rule.
4. Open the drift record and generate a remediation plan.
5. Approve the plan.
6. Execute the approved plan. In simulation mode, execution is simulated and does not change AWS.
7. Review the change journal.

## Important safety boundaries
- Detection uses read-only AWS API calls.
- The initial AWS remediation executor is deliberately disabled. It returns a safe simulation/disabled response
  instead of changing live AWS resources.
- Never grant broad write permissions to the scanner.
- A production remediation executor must re-fetch the resource, validate the exact reviewed plan,
  use least-privilege permissions, require a fresh approval and record before/after state.
- Unknown resources are reported for review; they are never deleted automatically.
- Baseline files are authoritative for this prototype. Keep them in version control and review changes
  through pull requests.

## Initial scope
The runnable prototype implements EC2 inventory and VPC security-group rule comparison. S3, IAM and
Elastic Beanstalk collectors are extension points and currently return an explicit not-implemented
status rather than pretending to scan those services.
