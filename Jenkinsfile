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
      }
    }

    stage('Build Docker') {

        environment {
            CI = 'true'
        }
        steps {
          script {
            withDockerServer([uri: "tcp://docker:2376"]) {
              // we give the image the same version as the .war package
              withDockerRegistry([credentialsId: '${env.registryCredential}', url: "${env.registry}"]) {
                  def imageName = "registry.zouzland.com/boutry/devops-tutorial-jvm:latest"
                  def image = docker.build("${imageName}", "-f src/main/docker/Dockerfile.jvm")
                  image.push()
              }
            }
          }
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