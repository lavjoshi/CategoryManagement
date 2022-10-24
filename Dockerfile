FROM openjdk:17
LABEL maintainer="Lav Joshi"
ADD target/CategoryManagement-0.0.1-SNAPSHOT.jar category_mgmt.jar
ENTRYPOINT ["java", "-jar", "category_mgmt.jar"]