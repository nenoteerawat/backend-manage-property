FROM openjdk:8-jdk-alpine as build
ARG WORKHOME=/workspace/app
WORKDIR ${WORKHOME}

COPY .m2 ${WORKHOME}/.m2
COPY mvnw .
COPY .mvn ${WORKHOME}/.mvn
COPY pom.xml .
COPY src ${WORKHOME}/src

RUN ${WORKHOME}/mvnw -s ${WORKHOME}/.mvn/settings.xml install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
RUN apk add ttf-dejavu
EXPOSE 8090
ENTRYPOINT ["java","-cp","app:app/lib/*","com.bayneno.backen_manage_property.ApiManagePropertyApplication"]
