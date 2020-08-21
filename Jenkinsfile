@Library('groovy-test')_

pipeline {
  agent none

  stages {
    stage('Dev') {
      steps {
        script {
          deployApp
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
