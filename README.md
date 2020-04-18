# devops-tutorial project
###### Author: Guillaume Boutry

## Link to Jenkins Instance
[https://jenkins.zouzland.com/blue/organizations/jenkins/devops-project-api/branches/] <-- from this page you can create a new release. Check tag build and set a tag version like 0.3.0 and it will be built and deployed on heroku.
You can snoop around in the Jenkinsfile to see the all the pipeline steps.
Jenkins instance hosted on my server, same for my docker registry.
Your credentials are:
login: adaltas
password: adaltas

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `devops-tutorial-0.3.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/devops-tutorial-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative`.

You can then execute your binary: `./target/devops-tutorial-0.3.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .

Native built with docker image quay.io/quarkus/centos-quarkus-maven:20.0.0-java11

## Building docker image

To build the jvm docker image run this command from the project root: 
`docker build -f src/main/docker/Dockerfile.jvm -t registry.zouzland.com/boutry/devops-tutorial-jvm:latest .`

To build this image, you need to connect to my private registry.
`docker login registry.zouzland.com`
login: adaltas
password: adaltas

I repackaged the a build image from Red Hat, that's why you need to login to my registry.

To build the native docker image run this command from the project root:
`docker build -f src/main/docker/Dockerfile.native -t registry.zouzland.com/boutry/devops-tutorial-native:latest .`

The native build should last around 6 minutes, it compiles java bytecode to native code.

In the registry, tags meaning:
- 0.3.0 -- version number
- latest -> points to the latest release. Right now it's 0.3.0
- snapshot -> latest build from jenkins

## Docker-compose
Run `docker-compose up` and you will be served.

Connect to [http://127.0.0.1:8080/api/user]

## K8S

Just apply every yamls in k8s folder.
Postgres-volume and configmap should be applied first.

## Istio

Don't forget to remove all k8s yamls from the step before

Just apply every yamls in istio folder
Postgres-volume and configmap sould be applied first.

## Ansible

Run `vagrant up` from ansible folder.
It will install every package you need and run docker-compose for you.
Connect to [http://30.30.30.3:8080/api/user]