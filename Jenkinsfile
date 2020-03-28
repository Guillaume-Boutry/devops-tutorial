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
      steps {
        sh 'mvn test'
        stash includes: 'target/', name: 'target_built'
        stash includes: 'src/main/docker/Dockerfile.jvm', name: 'Dockerfile'
      }
    }

    stage('Build Docker') {
        agent {
            docker {
              image 'docker:dind'
              args '--network="host" -e DOCKER_HOST="tcp://docker:2376" -e DOCKER_CERT_PATH="/certs/client" -e DOCKER_TLS_VERIFY=1 -v "$DOCKER_CERT_PATH":"$DOCKER_CERT_PATH" -v "$WORKSPACE":/tmp/project -w /tmp/project -v $HOME/.m2:/root/.m2'
            }
        }
        environment {
            CI = 'true'
        }
        steps {
            unstash 'target_built'
            unstash 'Dockerfile'
            sh './jenkins/buildDocker.sh'
        }
    }

  }

  environment {
    registry = 'https://registry.zouzland.com'
    registryCredential = 'registry'
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