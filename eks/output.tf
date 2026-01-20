output "vpc_id" {
  value = aws_vpc.project_java.id
}

output "subnet_id" {
  value = aws_subnet.project_java-subnet-public1.id
}