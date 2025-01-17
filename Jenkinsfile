pipeline {
    agent any

    tools {
       
        allure 'allure'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Perform a Git checkout of the repository
                    checkout scm
                }
            }
        }
        
        stage('Test Execution') {
            steps {
                script {
                    if (isUnix()) {
                        // Run tests on Unix-based systems using Maven Wrapper
                        sh './mvnw test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome'
                    } else {
                        // Run tests on Windows using Maven Wrapper
                        bat 'mvnw.cmd test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome'
                    }
                }
            }
        }
    }

    post {
        always {
          
            // Publish Allure test reports
            allure(
                includeProperties: false,
                jdk: '', 
                results: [[path: 'target/allure-results']]
            )

           
        }
    }
}
