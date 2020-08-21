#!/usr/bin/env groovy

def call(Map config) {
    stages {
        stage("Deploy${config.environment_name}") {
            steps {
                echo "Deploy App Here"
            }
        }
        stage("PostDeploy${config.environment_name}") {
            steps {
                echo "Run Post Deploy Tests Here"
            }
        }
    }
}
