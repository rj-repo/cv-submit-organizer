# Final image
FROM azul/zulu-openjdk:21

WORKDIR /app

# Copy built jar from build stage
COPY ./target/*.jar /app/target/*.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app/target/*.jar"]