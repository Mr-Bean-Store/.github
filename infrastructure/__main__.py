"""Pulumi AWS resource provisioning"""
import pulumi
from pulumi_aws import ec2, get_availability_zones
from helper_functions import label, ingress, everyone_cidr_blocks, egress

size = "t2.micro"
all_zones = get_availability_zones()

# Setup VPC
vpc = ec2.Vpc(label("vpc"), cidr_block="10.0.0.0/16")

# Setup Internet Gateway
internet_gateway = ec2.InternetGateway(label("internet_gateway"), vpc_id=vpc.id)

# Setup Route Table
route_table = ec2.RouteTable(
    label("route_table"),
    vpc_id=vpc.id,
    routes=[
        ec2.RouteTableRouteArgs(
            cidr_block=everyone_cidr_blocks[0],
            gateway_id=internet_gateway.id,
        )
    ],
)
# Setup Subnets
# limiting to 2 zones for speed and to meet minimal requirements.
zone_names = [all_zones.names[0], all_zones.names[1]]
subnets = []
route_table_associations = []
for zone in zone_names:
    subnet = ec2.Subnet(
        label(f"subnet_{zone}"),
        assign_ipv6_address_on_creation=False,
        vpc_id=vpc.id,
        map_public_ip_on_launch=True,
        cidr_block=f"10.100.{len(subnets)}.0/24",
        availability_zone=zone,
    )
    route_table_association = ec2.RouteTableAssociation(
        label(f"vpc_route_table_assoc_{zone}"),
        route_table_id=route_table.id,
        subnet_id=subnet.id,
    )
    subnets.append(subnet)
    route_table_associations.append(route_table_association)


# Setup EC2 Security Group
ec2_security_group = ec2.SecurityGroup(
    label("ec2_security_group"), vpc_id=vpc.id, ingress=ingress(ports=[80, 22, 443])
)

# Setup RDS Security Group
rds_security_group = ec2.SecurityGroup(
    label("rds_security_group"),
    vpc_id=vpc.id,
    ingress=ingress(ports=[3306]),
    egress=egress,
)

# Get Amazon Machine Image (Amazon Linux)
machine_image = ec2.get_ami(
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
    ami=machine_image.id,
)

pulumi.export("vpcId", vpc.id)
pulumi.export("internetGateWayId", internet_gateway.id)
for idx in range(len(zone_names)):
    pulumi.export(f"subnetId_{zone_names[idx]}", subnets[idx].id)
    pulumi.export(
        f"routeTableAssocId_{zone_names[idx]}", route_table_associations[idx].id
    )
pulumi.export("ec2SecurityGroupId", ec2_security_group.id)
# pulumi.export("rdsSecurityGroupId", rds_security_group.id)
pulumi.export("publicIp", ec2_instance.public_ip)
pulumi.export("publicDns", ec2_instance.public_dns)
