
from config.aws_config import get_ec2_client
import json


def collect_ec2():
    """
    Collect live EC2 instance information from AWS.
    """

    ec2 = get_ec2_client()

    instances = []

    paginator = ec2.get_paginator("describe_instances")

    for page in paginator.paginate():

        for reservation in page.get("Reservations", []):

            for instance in reservation.get("Instances", []):

                instances.append({
                    "resource_id": instance.get("InstanceId"),
                    "instance_id": instance.get("InstanceId"),
                    "instance_type": instance.get("InstanceType"),
                    "state": instance.get("State", {}).get("Name"),
                    "availability_zone": instance.get(
                        "Placement", {}
                    ).get("AvailabilityZone"),
                    "private_ip": instance.get("PrivateIpAddress"),
                    "public_ip": instance.get("PublicIpAddress"),
                    "vpc_id": instance.get("VpcId"),
                    "subnet_id": instance.get("SubnetId"),
                    "security_groups": [
                        group.get("GroupId")
                        for group in instance.get("SecurityGroups", [])
                    ],
                    "tags": {
                        tag["Key"]: tag["Value"]
                        for tag in instance.get("Tags", [])
                    }
                })

    return instances


# Backward-compatible function name
def collect_ec2_instances():
    return collect_ec2()


if __name__ == "__main__":
    result = collect_ec2()

    print(json.dumps(result, indent=4, default=str))
