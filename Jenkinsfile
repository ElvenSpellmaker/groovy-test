@Library('groovy-test')_

pipeline {
  agent none

  stages {
    stage('Dev') {
      steps {
        script {
          groovy-test "dev"
        }
      }
    }
    stage('Regular run') {
      steps {
        echo "bla!"
      }
    }
  }
}
