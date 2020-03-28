pipeline {
  agent any
  stages {
    stage('Build project') {
    agent {
        docker {
          image 'maven:3.6.3-jdk-11-slim'
          args '--network="host" -e DOCKER_HOST="tcp://docker:2376" -e DOCKER_CERT_PATH="/certs/client" -e DOCKER_TLS_VERIFY=1 -v "$DOCKER_CERT_PATH":"$DOCKER_CERT_PATH" -v "$WORKSPACE":/tmp/project -w /tmp/project -v $HOME/.m2:/root/.m2'
        }
      }
      steps {
        sh 'mvn clean package -DskipTests'
        stash includes: 'target/', name: 'target_built'
      }
    }

    stage('Test') {
      agent {
          docker {
            image 'maven:3.6.3-jdk-11-slim'
            args '--network="host" -e DOCKER_HOST="tcp://docker:2376" -e DOCKER_CERT_PATH="/certs/client" -e DOCKER_TLS_VERIFY=1 -v "$DOCKER_CERT_PATH":"$DOCKER_CERT_PATH" -v "$WORKSPACE":/tmp/project -w /tmp/project -v $HOME/.m2:/root/.m2'
          }
        }
      steps {
        unstash 'target_built'
        sh 'mvn test'
      }
    }

    stage('Build Docker') {
        agent any
        steps {
          script {
            checkout scm
            unstash 'target_built'
            sh './jenkins/buildDocker.sh'
          }
        }
    }

  }

  post {
          always {
              deleteDir()
          }
          success {
              echo 'SUCCESS'
          }
          unstable {
              echo 'UNSTABLE'
          }
          failure {
              echo 'FAILURE'
          }
          changed {
              echo 'State changed.'
          }
      }
}