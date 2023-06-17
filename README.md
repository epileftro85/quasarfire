## Quasar Fire Operation
In this repository you will find the code for Quasar Fire Operation, which receives distances from three alies ships and a message, with this we triangulate and find the secrets coordinates.

### Requirements

- Java >= 17
- Docker and docker-compose
- kafkaCat

### Instructions

- In a command line start the containers
  - > sh ./start.sh
- In another console window or in the same run gradlew
  -  > ./gradlew bootRun

### Running tests

- In a command line start the container
  - > sh ./start.sh
- In another console window or in the same run
  - > ./gradlew clean test

## Bonus

#### Sending messages
In order to send a message just run the next command
> kcat -P -b localhost:9092 -t topsecrettopic < topsecretmessage.json

#### Receiving messages
Listen to the topic **topsecrettopic**
> kcat -b localhost:9092 -t topsecrettopic

### Postman Collection
In order to test though postman, there is a collection in the root of the project called Postman-Collection.json.

### Infrastructure
Whiting the project exists the diagram of the built infrastructure, the file is called QuasarProject.jpg