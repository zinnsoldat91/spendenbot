FROM adoptopenjdk/openjdk11:alpine

COPY target/donationbot.lib/ donationbot.lib/
COPY target/donationbot.jar donationbot.jar

CMD ["java", "-jar", "donationbot.jar"]