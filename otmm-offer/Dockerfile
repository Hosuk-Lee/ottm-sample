# 1st stage, build the app
FROM maven:3.8.3-openjdk-17 as build

WORKDIR /offer-app

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom

COPY pom.xml .


# Do the Maven build!
# Incremental docker builds will resume here when you change sources
ADD src src
RUN mvn package -DskipTests
RUN echo "done!"
RUN ls
RUN ls target

# 2nd stage, build the runtime image
FROM openjdk:17.0.1-jdk-slim
WORKDIR /offer-app

# Copy the binary built in the 1st stage
COPY --from=build  /offer-app/target/otmm-offer.jar ./

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "otmm-offer.jar"]

EXPOSE 8081