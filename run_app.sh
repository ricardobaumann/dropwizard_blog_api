#!/bin/sh
java -version
java -jar target/dropwizard_blog_api-1.0-SNAPSHOT.jar db migrate src/main/resources/blog_api_docker.yaml
java -jar target/dropwizard_blog_api-1.0-SNAPSHOT.jar server src/main/resources/blog_api_docker.yaml