FROM maven:3.8.4-amazoncorretto-17
RUN mkdir -p /usr/src/app
EXPOSE 8081
WORKDIR /usr/src/app
ADD . /usr/src/app
RUN mvn clean test