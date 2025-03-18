# ---- Stage 1: Cache Dependencies ----
FROM maven:3.8.7-amazoncorretto-17 AS dependencies

# Define build-time arguments
ARG MAVEN_OPTS="-Xmx1024m"
ARG APP_VERSION="1.0.0"

# Metadata labels
LABEL maintainer="Your Name <your.email@example.com>" \
      description="Multi-stage build for optimized Spring Boot containerization" \
      version="${APP_VERSION}"

# Set working directory
WORKDIR /app

# Copy only the Maven descriptor to leverage caching
COPY pom.xml ./

# Download dependencies and store them in cache
RUN mvn dependency:go-offline -B

# ---- Stage 2: Build and Extract Layers ----
FROM maven:3.8.7-amazoncorretto-17 AS builder

# Set working directory
WORKDIR /app

# Copy cached dependencies from the first stage
COPY --from=dependencies /root/.m2 /root/.m2

COPY pom.xml ./
# Copy the application source code
COPY src/ ./src/

# Compile and build the Spring Boot application
RUN mvn clean package -DskipTests

# Extract the JAR using Spring Boot Layertools
RUN mkdir -p /app/extracted && \
    java -Djarmode=layertools -jar target/java-web-service-0.0.1-SNAPSHOT.jar extract --destination /app/extracted

# ---- Stage 3: Optimized Runtime Image ----
FROM amazoncorretto:17-alpine

# Metadata labels for runtime image
LABEL maintainer="Your Name <your.email@example.com>" \
      description="Optimized Spring Boot runtime container" \
      version="${APP_VERSION}"

# Set a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory inside the container
WORKDIR /app

# Copy extracted layers separately for better caching
COPY --from=builder /app/extracted/dependencies/ ./
COPY --from=builder /app/extracted/spring-boot-loader/ ./
COPY --from=builder /app/extracted/snapshot-dependencies/ ./
COPY --from=builder /app/extracted/application/ ./

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=dev
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Define a build-time argument (optional)
ARG BUILD_VERSION="1.0.0"

# Expose application port
EXPOSE 8080

# Healthcheck to ensure the application is running
# HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
#   CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set user permissions (Non-root user)
USER nobody

# Set the default shell
SHELL ["/bin/sh", "-c"]

# Add files from remote source (if required)
# ADD https://example.com/config.yaml /app/config.yaml

# Define default stop signal
STOPSIGNAL SIGTERM

# VOLUME
VOLUME [ "/data" ]

# Set an ONBUILD instruction for derived images (if needed)
ONBUILD COPY additional-config/ /app/config/

# Set default entrypoint for the application
ENTRYPOINT ["java", "-XX:TieredStopAtLevel=1", "-XX:+UseContainerSupport", "org.springframework.boot.loader.launch.JarLauncher"]

# Default command (can be overridden)
CMD []