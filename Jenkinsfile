pipeline {
    agent any

    environment {
        // Define the path to the docker-compose YAML file
        COMPOSE_FILE = 'docker-compose-standalone-chrome.yml' // Path to your docker-compose YAML file
    }

    stages {
        stage('Setup Selenium Grid') {
            steps {
                script {
                    // Start Selenium Grid using Docker Compose
                    echo "Starting Selenium Grid with docker-compose"
                    sh "docker-compose -f ${COMPOSE_FILE} up -d"
                    echo "Ensure Selenium Grid is running at http://localhost:4444/wd/hub"
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
    }

    post {
        always {
            // Stop Selenium Grid using Docker Compose after tests
            echo "Stopping Selenium Grid using docker-compose"
            sh "docker-compose -f ${COMPOSE_FILE} down"

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
