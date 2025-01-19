pipeline {
    agent any

    environment {
        // Define the credentials ID for Jenkins to use
        CREDENTIALS_ID = 'd8d07f72-f09a-4d73-8b77-3b27ba612a24' // Update this with your actual credentials ID
    }

    stages {
        stage('Setup Selenium Grid') {
            steps {
                script {
                    // Assuming Selenium Grid is already running on a remote machine or locally
                    echo "Ensure Selenium Grid is running at http://localhost:4444/wd/hub"
                }
            }
        }

        stage('Run Tests') {
            steps {
                // Inject the username and password credentials securely
                withCredentials([usernamePassword(credentialsId: CREDENTIALS_ID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    script {
                        // Output the username for debugging (this will not display the password)
                        echo "Using credentials for username: $USERNAME"

                        // Run tests using Maven
                        if (isUnix()) {
                            sh "./mvnw test -Pweb-execution -Dsuite=local-suite -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome"
                        } else {
                            bat "mvnw.cmd test -Pweb-execution -Dsuite=local-suite -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            // Publish Allure report as HTML
            publishHTML(target: [
                reportDir: 'target/allure-report', // Path to Allure report folder
                reportFiles: 'index.html', // Main file in the Allure report
                reportName: 'Allure Report', // Name of the report in Jenkins UI
                alwaysLinkToLastBuild: true
            ])
        }
    }
}