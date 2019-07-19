# pccw-assessment
pccw-assessment is a Spring Boot Application

## Requirments
- Java8+
- maven
- pcc-assessment-notification (another application)
- docker

### Usage (docker-compose.yml)
- How to run
  -  docker-compose up -d
  -  Open http://localhost:8080/s-pccw-assessment/swagger-ui.html with your browser, you can see the  register/edit/delete/read APIs.
  -  You can invoke  the Rest APIs in the browser
- Using a docker-compose yml to start this application, the file sample is like this:
- <b>Haproxy is used as a load balancer for user-service's partial handling</b>
- <b>Redis is a database for storing user information.</b>

### Log output/ Email output
 - location: ./logs
    - User-Service : ./logs/user-service/
    - Notification-Service: ./logs/notification-service/
    - Email: ./mail/

### docker-compose.yml [Keep at same folder with haproxy ]
```
version: "2"
services:
  user-service1:
    image: gonglongmin/pccw-assessment:Rel.1.0.0
    ports:
      - 8080
    depends_on:
      - notification-service
    networks:
     - back-tier
    volumes:
      - ./logs/user-service1:/logs
  user-service2:
    image: gonglongmin/pccw-assessment:Rel.1.0.0
    ports:
      - 8080
    depends_on:
      - notification-service
    networks:
     - back-tier
    volumes:
      - ./logs/user-service2:/logs

  proxy:
    image: haproxy:2.0.2-alpine
    networks:
      - back-tier
    links:
      - user-service1
      - user-service2
    ports:
      - "80:80"
      - "8080:8080"
    volumes:
      - ./haproxy/haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg

  notification-service:
    image: gonglongmin/pccw-assessment-notification:Rel.1.0.0
    ports:
      - "8081:8081"
    networks:
      - back-tier
    volumes:
      - ./logs/notification-service:/logs
      - ./mail:/received-emails
  redis:
    image: redis:5.0.5-alpine
    networks:
      - back-tier
networks:
  back-tier:
    driver: bridge
```
## Build Image
```
git clone https://github.com/charles-gong/pccw-assessment.git
cd pcc-assessment
mvn clean install
cd pccw-assessment-service
mvn docker:build
```
 
 ### API Doc
 - User-Service [http://localhost:8080/s-pccw-assessment/swagger-ui.html] 
 - Notification-servie [http://localhost:8081/s-pccw-assessment-notification/swagger-ui.html]
