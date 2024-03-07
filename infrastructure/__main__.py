"""Pulumi AWS resource provisioning"""
import pulumi
from pulumi_aws import ec2
from helper_functions import label, ingress

size = "t2.micro"

vpc = ec2.Vpc(label("vpc"), cidr_block="10.0.0.0/16")

ec2_security_group = ec2.SecurityGroup(
    label("ec2_security_group"), vpc_id=vpc.id, ingress=ingress(ports=[80, 22, 443])
)

ami = ec2.get_ami(
    most_recent=True,
    owners=["137112412989"],  # This owner ID refers to Amazon
    filters=[
        ec2.GetAmiFilterArgs(
            name="name",
            values=["amzn-ami-hvm-*-x86_64-ebs"],
        ),
    ],
)

ec2_instance = ec2.Instance(
    label("api_server"),
    instance_type=size,
    vpc_security_group_ids=[ec2_security_group.id],
    ami=ami.id,
)

pulumi.export("publicIp", ec2_instance.public_ip)
pulumi.export("publicDns", ec2_instance.public_dns)
