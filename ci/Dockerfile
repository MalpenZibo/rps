FROM buildo/scala-sbt-alpine:8u201_2.12.10_1.3.7 as build

WORKDIR /srv/api

ADD project project
ADD build.sbt .
RUN sbt update

ADD . .
RUN sbt assembly

FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY --from=build /srv/api/target/scala-2.12/rps.jar /srv/api.jar

CMD /usr/bin/java -cp /srv/api.jar rps.Main