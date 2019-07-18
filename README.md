# pccw-assessment
pccw-assessment is a Spring Boot Application

## Requirments
- Java8+
- maven
- pcc-assessment-notification (another application)
- docker

### Usage ( docker-compose.yml)
```Using a docker-compose yml to start this application, the file sample is like this:

version: "2"
services:
  user-service:
    image: gonglongmin/pccw-assessment:Rel.1.0.0
    ports:
      - "8080:8080"
    depends_on:
      - notification-service
    networks:
     - back-tier
    volumes:
      - ./logs/user-service:/logs

  notification-service:
    image: gonglongmin/pccw-assessment-notification:Rel.1.0.0
    ports:
      - "8081:8081"
    networks:
      - back-tier
    volumes:
      - ./logs/notification-service:/logs
      - ./mail:/received-emails

networks:
  back-tier:
    driver: bridge
```
## Install Executable jar
```git clone https://github.com/charles-gong/pccw-assessment.git
   cd pcc-assessment
   mvn clean install
   cd pccw-assessment-service
   mvn docker:build
```

### User schema
|Varaiable| Type|
|name|String|
|id|String|
|email|String|
|isDeleted|Boolean|
|age|Integer|
|nation|String|
 
 ### API Doc
 - User-Service [http://localhost:8080/s-pccw-assessment/swagger-ui.html] 
 - Notification-servie [http://localhost:8081/s-pccw-assessment-notification/swagger-ui.html]
 
 ### Log output/ Email output
 - location: ./logs
    - User-Service : ./logs/user-service/
    - Notification-Service: ./logs/notification-service/
    - Email: ./mail/
