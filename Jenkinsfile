pipeline {
  agent {
    docker {
      image 'maven:3.6.3-jdk-11-slim'
    }

  }
  stages {
    stage('') {
      steps {
        sh 'mvn clean install -DskipTests'
      }
    }

  }
}