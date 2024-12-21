pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                dir('selenium-java-automation') {
                    bat 'mvn clean'
                }
            }
        }
        stage('Deploy') {
            steps {
                dir('selenium-java-automation') {
                    bat 'mvn test'
                }
            }
        }
    }
}
