pipeline {
    agent any

    environment {
        // Define the path to the docker-compose YAML file
        COMPOSE_FILE = 'docker-compose-standalone-chrome.yml'
    }

    stages {
        stage('Clean Disk Space') {
            steps {
                script {
                    echo "Cleaning up disk space"
                    sh '''
                        docker system prune -f || true
                        rm -rf /tmp/* || true
                        df -h
                    '''
                }
            }
        }

        stage('Clean Old Target') {
            steps {
                script {
                    // Clean the old target directory using Maven
                    echo "Cleaning old target directory using Maven"
                    sh "mvn clean"
                }
            }
        }

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
                    echo "Running tests with Maven"
                    sh """
                        mvn test -Pweb-execution \
                            -Dsuite=local-suite \
                            -Dtarget=selenium-grid \
                            -Dheadless=true \
                            -Dbrowser=chrome
                    """
                }
            }
        }
    }

    post {
        always {
            // Stop Selenium Grid using Docker Compose after tests
            echo "Stopping Selenium Grid using docker-compose"
            sh "docker-compose -f ${COMPOSE_FILE} down"

            // Generate Allure report using Maven
            echo "Generating Allure report"
            sh "mvn allure:report"

            // Publish Allure report as HTML
            publishHTML(
                target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site/allure-maven-plugin',
                    reportFiles: 'index.html',
                    reportName: 'Allure Report',
                    reportTitles: 'Allure Test Report'
                ]
            )
        }
    }
}