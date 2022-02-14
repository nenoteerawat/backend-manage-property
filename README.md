# Spring Boot, Spring Security, MongoDB - JWT Authentication & Authorization example

For more detail, please visit:
> [Spring Boot, MongoDB: JWT Authentication with Spring Security](https://bezkoder.com/spring-boot-jwt-auth-mongodb/)

## Run Spring Boot application
```
mvn spring-boot:run
```

## Build image on local

Copy foder .m2 from home directory($HOME/.m2) to project directory(./)

```
docker build --platform linux/amd64 -t property123/api-manage-property:latest .
docker push property123/api-manage-property:latest 
```

With mulitple platform
```
docker buildx ls
docker buildx create --name mybuilder
docker buildx use mybuilder
docker buildx inspect --bootstrap
docker buildx build --tag [image-tag] -o type=image --platform=linux/arm64,linux/amd64 .
docker buildx build --push --tag [docker-hubid/image-tag] --platform=linux/arm64,linux/amd64 .
```