from pulumi_aws import ec2
from typing import List

everyone_cidr_blocks = ["0.0.0.0/0"]

egress = [
    ec2.SecurityGroupEgressArgs(
        protocol="-1",
        from_port=0,
        to_port=0,
        cidr_blocks=everyone_cidr_blocks,
    )
]


def label(value: str) -> str:
    return f"mr-bean-store-{value}"


def ingress(*, ports: List[int]) -> List[ec2.SecurityGroupIngressArgs]:
    ingress_ls = []
    for port in ports:
        ingress_ls.append(
            ec2.SecurityGroupIngressArgs(
                protocol="tcp",
                from_port=port,
                to_port=port,
                cidr_blocks=everyone_cidr_blocks,
            )
        )
    return ingress_ls
