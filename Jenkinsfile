pipeline {
    agent any

    tools {
        allure 'allure' // Ensure Allure is configured in Jenkins Global Tool Configuration
    }

    stages {
        stage('Clean and Test Execution') {
            steps {
                script {
                    // Clean the target folder and run tests depending on the operating system
                    if (isUnix()) {
                        // For Unix-based systems, use Maven Wrapper
                        sh './mvnw clean test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome'
                    } else {
                        // For Windows, use Maven Wrapper with .cmd
                        bat 'mvnw.cmd clean test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome'
                    }
                }
            }
        }
    }

    post {
        always {
            // Publish Allure test results after the test execution, regardless of success or failure
            allure(
                includeProperties: false,
                jdk: '', // Optionally, specify JDK if required
                results: [[path: 'target/allure-results']] // Path to Allure results folder
            )
        }
    }
}
