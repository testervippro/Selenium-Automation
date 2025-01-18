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
            // Publish Allure report as HTML using the HTML Publisher plugin
            publishHTML(target: [
                reportDir: 'target/allure-report', // Path to Allure report folder
                reportFiles: 'index.html', // Main file in the Allure report
                reportName: 'Report', // Name of the report in Jenkins UI
                alwaysLinkToLastBuild: true
            ])
        }
    }
}
