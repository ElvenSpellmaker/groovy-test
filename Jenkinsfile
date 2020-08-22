@Library('groovy-test')_

pipeline {
  agent none

  stages {
    stage('Dev') {
      agent {
        docker {
          image "azul/zulu-openjdk-debian:11"
        }
      }

      steps {
        echo "before bleh!"

        script {
          deployApp()
        }

        echo "bleh!"
      }
    }
    stage('Regular run') {
      steps {
        echo "bla!"
      }
    }
  }
}
