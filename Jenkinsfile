pipeline {
  agent any
  parameters {
      booleanParam (
          defaultValue: false,
          description: 'Push docker image to registry',
          name : 'FORCE_PUSH'
        )
      booleanParam (
          defaultValue: false,
          description: 'Tag current build ?',
          name: 'TAG_BUILD'
        )
        string (
            description: 'TAG number',
            name: 'TAG'
          )
      }

  stages {
    stage('Build project') {
    agent {
        docker {
          image 'maven:3.6.3-jdk-11-slim'
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
            args '--network="host" -e DOCKER_HOST="tcp://docker:2376" -e DOCKER_CERT_PATH="/certs/client" -e DOCKER_TLS_VERIFY=1 -v "$DOCKER_CERT_PATH":"$DOCKER_CERT_PATH"'
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
          sh 'docker build -f src/main/docker/Dockerfile.jvm -t registry.zouzland.com/boutry/devops-tutorial-jvm .'
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
            script {
                if(TAG_BUILD) {
                    sh 'docker push registry.zouzland.com/boutry/devops-tutorial-jvm:latest'
                    sh 'docker tag registry.zouzland.com/boutry/devops-tutorial-jvm:latest registry.zouzland.com/boutry/devops-tutorial-jvm:' + TAG
                    sh 'docker push registry.zouzland.com/boutry/devops-tutorial-jvm:' + TAG
                } else {
                    sh 'docker tag registry.zouzland.com/boutry/devops-tutorial-jvm:latest registry.zouzland.com/boutry/devops-tutorial-jvm:snapshot'
                    sh 'docker push registry.zouzland.com/boutry/devops-tutorial-jvm:snapshot'
                }
            }
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