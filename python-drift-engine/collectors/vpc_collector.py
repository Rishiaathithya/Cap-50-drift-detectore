from config.aws_config import get_ec2_client

def collect_security_groups():
    client = get_ec2_client()
    response = client.describe_security_groups()
    resources = []
    for group in response.get("SecurityGroups", []):
        rules = []
        for permission in group.get("IpPermissions", []):
            protocol = permission.get("IpProtocol", "-1")
            for item in permission.get("IpRanges", []):
                rules.append({
                    "protocol": protocol,
                    "from_port": permission.get("FromPort"),
                    "to_port": permission.get("ToPort"),
                    "cidr": item.get("CidrIp")
                })
            for item in permission.get("Ipv6Ranges", []):
                rules.append({
                    "protocol": protocol,
                    "from_port": permission.get("FromPort"),
                    "to_port": permission.get("ToPort"),
                    "cidr": item.get("CidrIpv6")
                })
        resources.append({
            "resource_id": group["GroupId"],
            "vpc_id": group.get("VpcId"),
            "inbound_rules": sorted(rules, key=lambda r: str(r))
        })
    return resources
