module "dockercomposehost" {
  source  = "buildo/dockercomposehost/aws"
  version = "1.6.1"

  instance_type = "t3a.nano"

  zone_id      = "${data.aws_route53_zone.buildo-io.zone_id}"
  ssh_key_name = "${aws_key_pair.access.key_name}"

  project_name        = "simonerps"
  host_name           = "simonerps.our.buildo.io"
  ssh_private_key     = "${var.ssh_private_key}"
  quay_password       = "${var.quay_password}"
  in_open_ports       = ["80"]
  in_cidr_blocks      = ["${local.buildo_ip}"]
  bellosguardo_target = "buildo"
}

data "aws_route53_zone" "buildo-io" {
  name = "buildo.io"
}

variable quay_password {}

variable ssh_private_key {
  default = "~/.ssh/rps"
}

resource "aws_key_pair" "access" {
  key_name   = "simonerps"
  public_key = "${file("config/rps.pub")}"
}

locals {
  buildo_ip = "151.0.219.218/32"
}
