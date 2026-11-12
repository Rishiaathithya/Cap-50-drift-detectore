import boto3

# AWS configuration for CAP-50
AWS_PROFILE = "cap50-dev"
AWS_REGION = "eu-north-1"


def get_session():
    return boto3.Session(
        profile_name=AWS_PROFILE,
        region_name=AWS_REGION
    )


def get_ec2_client():
    return get_session().client("ec2")


def get_vpc_client():
    # VPC operations in boto3 are handled through the EC2 service client
    return get_session().client("ec2")


def get_s3_client():
    return get_session().client("s3")


def get_iam_client():
    # IAM is a global service, so it will route to us-east-1 automatically
    return get_session().client("iam")


def get_eb_client():
    return get_session().client("elasticbeanstalk")
