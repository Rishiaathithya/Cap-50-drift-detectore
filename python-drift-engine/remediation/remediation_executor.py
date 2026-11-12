def execute_plan(plan: dict, approved: bool, simulation: bool = True) -> dict:
    if not approved:
        raise PermissionError("Remediation cannot run without an approval.")
    if simulation:
        return {
            "status": "SIMULATED",
            "message": "Approval verified. No live AWS resource was modified."
        }
    # Deliberately disabled: live AWS remediation needs resource-specific implementation,
    # least-privilege IAM, fresh state validation, exact-plan approval and audit logging.
    raise NotImplementedError(
        "Live remediation is disabled in this prototype. Implement and security-review "
        "a resource-specific executor before enabling AWS write operations."
    )
