#!/usr/bin/env bash

# Helper script to run the Spring Boot app with the JVM opens
# required by embedded Tomcat on newer Java versions.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_PATH="$SCRIPT_DIR/target/calculator-app-0.0.1-SNAPSHOT.jar"

if [ ! -f "$JAR_PATH" ]; then
  echo "JAR not found at: $JAR_PATH" >&2
  echo "Build it first with: mvn clean package" >&2
  exit 1
fi

java \
  --add-opens=java.base/java.io=ALL-UNNAMED \
  --add-opens=java.base/java.lang=ALL-UNNAMED \
  --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
  --add-opens=java.base/java.net=ALL-UNNAMED \
  --add-opens=java.base/java.nio=ALL-UNNAMED \
  --add-opens=java.base/java.util=ALL-UNNAMED \
  --add-opens=java.base/sun.nio.ch=ALL-UNNAMED \
  --add-opens=java.base/sun.nio.cs=ALL-UNNAMED \
  --add-opens=java.base/sun.security.action=ALL-UNNAMED \
  --add-opens=java.base/sun.util.calendar=ALL-UNNAMED \
  --add-opens=java.naming/javax.naming.spi=ALL-UNNAMED \
  --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED \
  -jar "$JAR_PATH"