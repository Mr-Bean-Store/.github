from pulumi_aws import ec2
from typing import List
everyone_cidr_blocks = ["0.0.0.0/0"]

def label(value: str) -> str:
    return f"mr_bean_store_{value}"

def ingress(*,ports: List[int]) ->List[ec2.SecurityGroupIngressArgs]:
    ingress_ls = []
    for port in ports:
        ingress_ls.append(ec2.SecurityGroupIngressArgs(
            protocol="tcp",
            from_port=port,
            to_port=port,
            cidr_blocks=everyone_cidr_blocks
        ))
    return ingress_ls
