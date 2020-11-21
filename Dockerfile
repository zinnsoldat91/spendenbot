FROM adoptopenjdk/openjdk11:alpine

COPY target/donationbot.jar donationbot.jar

CMD ["java", "-jar", "donationbot.jar"]