pipeline {
    agent any

    environment {
        // Define the path to the docker-compose YAML file
        COMPOSE_FILE = 'docker-compose-grid.yml'
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
                    // Clean the old target directory
                    echo "Cleaning old target directory"
                    if (isUnix()) {
                        sh "rm -rf target"
                    } else {
                        bat "rmdir /s /q target"
                    }
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
                    if (isUnix()) {
                        sh "./mvnw clean test -Pweb-execution -Dsuite=selenium-grid -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome"
                    } else {
                        bat "mvnw.cmd clean test -Pweb-execution -Dsuite=local-suite -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome"
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
            publishHTML (target : [allowMissing: false,
             alwaysLinkToLastBuild: true,
             keepAll: true,
             reportDir: 'target/allure-results',
             reportFiles: 'index.html',
             reportName: 'My Reports',
             reportTitles: 'The Report'])
        }
    }
}