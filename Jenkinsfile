pipeline {
  agent any
  parameters {
          booleanParam (
              defaultValue: false,
              description: '',
              name : 'FORCE_PUSH')
      }
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
          unstash 'target_built'
          sh './jenkins/buildDocker.sh'
        }
    }

    stage('Push Docker build to registry') {
        agent any
        when {
            expression {
                GIT_BRANCH = 'origin/' + sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                return GIT_BRANCH == 'origin/master' || params.FORCE_PUSH
            }
        }
        steps {
          withCredentials([usernamePassword(credentialsId: 'registry', passwordVariable: 'registryPassword', usernameVariable: 'registryUser')]) {
            sh "docker login -u ${env.registryUser} -p ${env.registryPassword} registry.zouzland.com"
            sh 'docker push registry.zouzland.com/boutry/devops-tutorial-jvm:latest'
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