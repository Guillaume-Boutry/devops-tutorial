pipeline {
  agent {
    docker {
      image 'maven:3.6.3-jdk-11-slim'
      args '--network="host" -e DOCKER_HOST="tcp://docker:2376" -e DOCKER_CERT_PATH="/certs/client" -e DOCKER_TLS_VERIFY=1 -v "$DOCKER_CERT_PATH":"$DOCKER_CERT_PATH" -v "$WORKSPACE":/tmp/project -w /tmp/project -v $HOME/.m2:/root/.m2'
    }

  }
  stages {
    stage('Build project') {
      steps {
        sh 'mvn clean install -DskipTests'
      }
    }

    stage('Test') {
      environment {
        CI = 'true'
      }
      steps {
        sh 'mvn test'
      }
    }

    stage('Build docker') {
      agent {
        docker {
          image 'docker:dind'
          args '--network="host" -e DOCKER_HOST="tcp://docker:2376" -e DOCKER_CERT_PATH="/certs/client" -e DOCKER_TLS_VERIFY=1 -v "$DOCKER_CERT_PATH":"$DOCKER_CERT_PATH" -v "$WORKSPACE":/tmp/project -w /tmp/project'
        }

      }
      steps {
        sh 'docker build -f src/main/docker/Dockerfile.jvm -t registry.zouzland.com/boutry/devops-tutorial-jvm "$WORKSPACE"'
      }
    }

    stage('Push to docker image registry') {
      agent {
        docker {
          image 'docker:dind'
          args '--network="host" -e DOCKER_HOST="tcp://docker:2376" -e DOCKER_CERT_PATH="/certs/client" -e DOCKER_TLS_VERIFY=1 -v "$DOCKER_CERT_PATH":"$DOCKER_CERT_PATH"'
        }

      }
      steps {
        sh 'docker push registry.zouzland.com/boutry/devops-tutorial-jvm:latest'
      }
    }

  }
  environment {
    registry = 'registry.zouzland.com'
    registryCredential = 'registry'
  }
}