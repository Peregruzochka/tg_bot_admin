FROM openjdk:17-alpine AS build_project
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY build.gradle.kts $APP_HOME
COPY settings.gradle.kts $APP_HOME
COPY gradlew $APP_HOME
COPY gradle $APP_HOME/gradle

RUN ./gradlew dependencies

COPY . .
RUN ./gradlew build -x test

FROM openjdk:17-jdk-slim

ENV TZ=Europe/Moscow
RUN apt-get update && apt-get install -y tzdata && \
    ln -fs /usr/share/zoneinfo/$TZ /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata

ENV ARTIFACT_NAME=tg_bot_admin-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY --from=build_project $APP_HOME/build/libs/$ARTIFACT_NAME app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]