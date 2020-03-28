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
        sh 'mvn clean package -DskipTests'
        stash includes: 'target/', name: 'target_built'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }

    stage('Build Docker') {
        agent {
            label "docker-host"
        }
        environment {
            CI = 'true'
        }
        steps {
          script {
              unstash 'target_built'
              def imageName = "registry.zouzland.com/boutry/devops-tutorial-jvm:latest"
              def image = docker.build(imageName, "-f src/main/docker/Dockerfile.jvm")
              image.push()
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