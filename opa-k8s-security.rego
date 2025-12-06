package main

# Service should use LoadBalancer type
deny contains msg if {
  input.kind == "Service"
# Service should use LoadBalancer type
  input.spec.type != "LoadBalancer"
  msg = "Service type should be LoadBalancer"
}

# Containers must not run as root
deny contains msg if {
  input.kind == "Deployment"
  container := input.spec.template.spec.containers[_]
  not container.securityContext.runAsNonRoot == true
  msg = sprintf("Container '%s' must not run as root - use runAsNonRoot within container security context", [container.name])
}