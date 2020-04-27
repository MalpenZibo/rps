locals {
  buildo_ip = "151.0.219.218/32"
}

variable "access_key_value" {}

data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-*-18.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["099720109477"] # Canonical
}

resource "aws_key_pair" "access" {
  key_name   = "access-key"
  public_key = "${var.access_key_value}"
}

resource "aws_instance" "rps" {
  ami           = "${data.aws_ami.ubuntu.id}"
  instance_type = "t3a.nano"

  vpc_security_group_ids = ["${aws_security_group.sg.id}"]

  root_block_device {
    volume_type = "gp2"
    volume_size = 10
  }

  key_name = "${aws_key_pair.access.key_name}"
}

resource "aws_security_group" "sg" {
  name = "simonerps"
}

resource "aws_security_group_rule" "ssh_in" {
  type              = "ingress"
  protocol          = "tcp"
  security_group_id = "${aws_security_group.sg.id}"
  from_port         = 22
  to_port           = 22
  cidr_blocks       = ["${local.buildo_ip}"]
}

resource "aws_security_group_rule" "web_in" {
  type              = "ingress"
  protocol          = "tcp"
  security_group_id = "${aws_security_group.sg.id}"
  from_port         = 80
  to_port           = 80
  cidr_blocks       = ["${local.buildo_ip}"]
}


resource "aws_security_group_rule" "all_out" {
  type              = "egress"
  protocol          = -1
  security_group_id = "${aws_security_group.sg.id}"
  from_port         = 0
  to_port           = 0
  cidr_blocks       = ["0.0.0.0/0"]
}

data "aws_route53_zone" "buildo-io" {
  name = "buildo.io"
}

resource "aws_route53_record" "simonerps-our-buildo-io-A" {
  zone_id = "${data.aws_route53_zone.buildo-io.id}"
  name    = "simonerps.our.buildo.io"
  type    = "A"
  records = ["${aws_instance.rps.public_ip}"]
  ttl     = "1800"
}
