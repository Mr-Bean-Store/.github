"""Pulumi AWS resource provisioning"""
import pulumi
from pulumi_aws import ec2, get_availability_zones, rds, route53, route53domains
from helper_functions import label, ingress, everyone_cidr_blocks, egress

ec2_instance_type = "t2.micro"
rds_instance_type = "db.t3.micro"
all_zones = get_availability_zones()
config = pulumi.Config()
domain_name = "mr-bean-store.click"

# Setup VPC
vpc = ec2.Vpc(
    label("vpc"),
    cidr_block="10.0.0.0/16",
    enable_dns_hostnames=True,
    enable_dns_support=True,
)

# Setup Internet Gateway
internet_gateway = ec2.InternetGateway(label("internet-gateway"), vpc_id=vpc.id)

# Setup Route Table
route_table = ec2.RouteTable(
    label("route-table"),
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
for idx, zone in enumerate(zone_names):
    subnet = ec2.Subnet(
        label(f"subnet_{zone}"),
        assign_ipv6_address_on_creation=False,
        vpc_id=vpc.id,
        map_public_ip_on_launch=(idx == 0),
        cidr_block=f"10.0.{idx}.0/24",
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
    label("subnet-group"), subnet_ids=[subnet.id for subnet in subnets]
)


# Setup EC2 Security Group
ec2_security_group = ec2.SecurityGroup(
    label("ec2_security_group"),
    vpc_id=vpc.id,
    ingress=ingress(ports=[80, 22, 443, 8090]),
    egress=egress,
)


# Setup RDS Security Group
rds_security_group = ec2.SecurityGroup(
    label("rds-security-group"),
    vpc_id=vpc.id,
    ingress=ingress(ports=[5432]),
    egress=egress,
)

# Setup RDS Instance
rds_instance = rds.Instance(
    label("db"),
    db_name="mr_bean_store_db",
    allocated_storage=20,
    engine="postgres",
    engine_version="16.2",
    instance_class=rds_instance_type,
    storage_type="gp2",
    db_subnet_group_name=rds_subnet_group.name,
    username=config.get("db_user"),
    password=config.get("db_password"),
    vpc_security_group_ids=[rds_security_group.id],
    skip_final_snapshot=True,
    publicly_accessible=True,
)


# Get Amazon Machine Image (Amazon Linux)
machine_image = ec2.get_ami(
    most_recent=True,
    owners=["137112412989"],  # This owner ID refers to Amazon
    filters=[
        ec2.GetAmiFilterArgs(
            name="name",
            values=["al2023-ami-2023.3.20240219.0-kernel-6.1-x86_64"],
        ),
    ],
)

# Setup EC2 ssh key pair
ssh_key_pair = ec2.KeyPair(
    label("ssh_key"),
    key_name=config.get("ssh_key_name"),
    public_key="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQC3ZfsHy54GlzkrZ8BHW5CXKpnxSUCQviBH2GpUIAiJpg5ePucYMiRh0UhrXcMYSX9mcMfJpAgB3AlFjhW8DF8F2K/5BKSqUEtfE0UobuWfyy00mHRCETC5qmRkFF4/WgJn8ySbHL9pQPbgOD10DYg9SD9yJbaGEOEV7hqI9JcnfXMKNBCVGvogV+KR5jnDw1gV6TcUjYX9sTHR17lZVkOqGAkXBWXaJSF1I9ofhs/+du39PdOMznMTNpsz1B95eczxbNJxCDBwzj/t94pPy8rmyku2PGadJFO6X9sSyRmyplWH8re7dOfyccQFOwJq2X1zfddx3UNw+HgYnKV+LdBf/dZh/8QezYxvAuHwrXK1gf1OYG8qmhWBhnEA7sqTWpNWbQXM/9e+ztiLBBa01VnSI0aOHiYLG4/FQufwHojx/UpER1vOTwyculW+GL6YPAbcCONFDqKFsteK1XZXTKVPySbObMbdQWdpME3AkhQaNJswGBBqc40CiKxG+Ky+2kLiKIk4z7jlV3C1nvsg01wE6Jl4mKC5O9Ep1G1uLotg/oYz93sgQM6eiH6JMnLLNhHnann92+mDrhlYmyjDzZ48e1ynwoakhmSgs/4a7gh5/5G8TRLv27HIlFXFld0bDNn71vWo8kDYUVMtw11toZy8oQrd+2Tow5EI7p0a+lv7gw== devsm@wsl-atp",
)

# Setup EC2 instance
ec2_instance = ec2.Instance(
    label("api-server"),
    instance_type=ec2_instance_type,
    subnet_id=subnets[0].id,
    vpc_security_group_ids=[ec2_security_group.id],
    key_name=ssh_key_pair.key_name,
    ami=machine_image.id,
)

# Setup Elastic IP
elastic_ip = ec2.Eip(label("elastic_ip"))

# Setup Elastic IP association
elastic_ip_assoc = ec2.EipAssociation(
    "elastic_ip_assoc", instance_id=ec2_instance.id, allocation_id=elastic_ip.id
)
