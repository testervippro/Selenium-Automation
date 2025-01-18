pipeline {
    agent any
    environment {
        // Define the credentials ID for Jenkins to use
        CREDENTIALS_ID = 'd8d07f72-f09a-4d73-8b77-3b27ba612a24' // Update this with your actual credentials ID
    }
    stages {
        stage('Test Execution') {
            steps {
                // Inject the username and password credentials securely
                withCredentials([usernamePassword(credentialsId: CREDENTIALS_ID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    script {
                        // Output the username for debugging (this will not display the password)
                        echo "Using credentials for username: $USERNAME"

                        // Clean the target folder and run tests depending on the operating system
                        if (isUnix()) {
                            // For Unix-based systems, use Maven Wrapper
                            sh './mvnw test -Pweb-execution -Dsuite=local-suite -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome -Dusername=$USERNAME -Dpassword=$PASSWORD'
                        } else {
                            // For Windows, use Maven Wrapper with .cmd
                            bat "mvnw.cmd test -Pweb-execution -Dsuite=local-suite -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome -Dusername=$USERNAME -Dpassword=$PASSWORD"
                        }
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
