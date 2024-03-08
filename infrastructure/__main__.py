"""Pulumi AWS resource provisioning"""
import pulumi
from pulumi_aws import ec2, get_availability_zones, rds
from helper_functions import label, ingress, everyone_cidr_blocks, egress

ec2_instance_type = "t2.micro"
rds_instance_type = "db.t2.micro"
all_zones = get_availability_zones()
config = pulumi.Config()

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

# Setup EC2 Subnets
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

# Setup RDS subnet group
rds_subnet_group = rds.SubnetGroup(
    label("subnet_group"), subnet_ids=[subnet.id for subnet in subnets]
)


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

# Setup RDS Instance
rds_instance = rds.Instance(
    label("rds_instance"),
    name="mr_bean_store_db",
    allocated_storage=20,
    engine="postgres",
    engine_version="16.2",
    instance_class=rds_instance_type,
    storage_type="gp2",
    db_subnet_group_name=rds_subnet_group.id,
    username=config.get("db_user"),
    password=config.get("db_password"),
    vpc_security_group_ids=[rds_security_group.id],
    skip_final_snapshot=True,
    publicly_accessible=False,
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
    instance_type=ec2_instance_type,
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
pulumi.export("rdsEndpoint", rds_instance.endpoint)
pulumi.export("rdsPort", rds_instance.port)
