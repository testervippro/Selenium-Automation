pipeline {
    agent any

    environment {
        // Define the Selenium Grid URL
        SELENIUM_HUB_URL = "http://localhost:4444/wd/hub"
    }

    stages {
        stage('Start Selenium Chrome') {
            steps {
                script {
                    // Use Docker plugin to start the Selenium Chrome standalone container
                    dockerCompose(
                        composeFilePath: 'docker-compose-standalone-chrome.yml',
                        useCustomDockerComposeFile: true,
                        upOptions: '--detach'
                    )

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
                script {
                    // Run tests using Maven
                    if (isUnix()) {
                        sh "./mvnw test -Pweb-execution -Dsuite=local-suite -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome"
                    } else {
                        bat "mvnw.cmd test -Pweb-execution -Dsuite=local-suite -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome"
                    }
                }
            }
        }

        stage('Stop Selenium Chrome') {
            steps {
                script {
                    // Use Docker plugin to stop and remove the Selenium Chrome standalone container
                    dockerCompose(
                        composeFilePath: 'docker-compose-standalone-chrome.yml',
                        useCustomDockerComposeFile: true,
                        downOptions: '--volumes --remove-orphans'
                    )
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