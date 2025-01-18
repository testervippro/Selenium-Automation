pipeline {
    agent any

    environment {
        // Define the credentials ID for Jenkins to use
        CREDENTIALS_ID = 'd8d07f72-f09a-4d73-8b77-3b27ba612a24' // Update this with your actual credentials ID
    }

    stages {
        stage('Start Selenium Chrome') {
            steps {
                script {
                    // Start the Selenium Chrome standalone container using Docker Compose
                    sh 'docker-compose -f docker-compose-standalone-chrome.yml up -d'

                    // Wait for the Selenium Chrome standalone to be ready
                    sh '''
                        echo "Waiting for Selenium Chrome standalone to be ready..."
                        while ! curl -sSL "http://localhost:4444/wd/hub/status" | grep '"ready": true'; do
                            sleep 5
                        done
                        echo "Selenium Chrome standalone is ready!"
                    '''
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

        stage('Stop Selenium Chrome') {
            steps {
                script {
                    // Stop and remove the Selenium Chrome standalone container
                    sh 'docker-compose -f docker-compose-standalone-chrome.yml down'
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