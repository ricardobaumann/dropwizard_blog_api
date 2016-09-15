# dropwizard_blog_api
A blog API written in java dropwizard framework

## Installation
* Clone it
* On the root folder, run 

`
mvn clean package &&
java -jar target/dropwizard_blog_api-1.0-SNAPSHOT.jar db migrate src/main/resources/blog_api.yaml &&
java -jar target/dropwizard_blog_api-1.0-SNAPSHOT.jar server src/main/resources/blog_api.yaml
`

## Usage
* Check endpoint resources on code. Posts creation endpoint require authentication!!
