pipeline {
    agent any

    stages {
        stage('Test Execution') {
            steps {
                script {
                    // Clean the target folder and run tests depending on the operating system
                    if (isUnix()) {
                        // For Unix-based systems, use Maven Wrapper
                        sh './mvnw test -Pweb-execution -Dsuite=local-suite -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome'
                    } else {
                        // For Windows, use Maven Wrapper with .cmd
                        bat 'mvnw.cmd test -Pweb-execution -Dsuite=local-suite -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome'
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