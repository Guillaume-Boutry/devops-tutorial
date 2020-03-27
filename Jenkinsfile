pipeline {
  agent {
    docker {
      image 'maven:3.6.3-jdk-11-slim'
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
        DOCKER_HOST = '$DOCKER_HOST'
        DOCKER_CERT_PATH = '/certs/client'
        DOCKER_TLS_VERIFY = '1'
      }
      steps {
        sh 'mvn test'
      }
    }

  }
}