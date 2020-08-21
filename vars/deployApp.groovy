#!/usr/bin/env groovy

def call() {
  stages {
    stage("Deploylol") {
      agent {
        docker {
          image "azul/zulu-openjdk-debian:11"
        }
      }

      steps {
        echo "Deploy App Here"

        sh(
          script: """#!/bin/bash
            echo "lolololol"
          """,
          label: "Bash lol"
        )
      }
    }
    stage("PostDeploylol") {
      steps {
        echo "Run Post Deploy Tests Here"
      }
    }
  }
}
