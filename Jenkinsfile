@Library('groovy-test')_

pipeline {
  agent none

  stages {
    stage('Dev') {
      steps {
        script {
          groovy-test(
            environment_name: "dev"
          )
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
