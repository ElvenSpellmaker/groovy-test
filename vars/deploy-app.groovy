#!/usr/bin/env groovy

def call(String environment_name) {
    stages {
        stage("Deploy${environment_name}") {
            steps {
                echo "Deploy App Here"
            }
        }
        stage("PostDeploy${environment_name}") {
            steps {
                echo "Run Post Deploy Tests Here"
            }
        }
    }
}
