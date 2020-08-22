#!/usr/bin/env groovy

def call() {
  echo "blehhhhhh!"

  node {

    stages {
      stage("Deploylol") {
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
}
