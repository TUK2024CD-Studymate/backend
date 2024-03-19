FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} studymate.jar
ENTRYPOINT ["java","-jar","/studymate.jar"]