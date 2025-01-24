pipeline {
    agent none  // No agent specified, because we’ll be running on a Docker container

    stages {
        stage('Start Docker Compose') {
            steps {
                script {
                    // Run Docker Compose in the background
                    sh 'docker-compose -f docker-compose-chrome-firefox-edge.yml up -d'
                }
            }
        }

        stage('Run Tests') {
            agent {
                docker {
                    image 'cuxuanthoai/chrome-firefox-edge'
                }
            }
            steps {
                script {
                    // Run Maven tests with specific browser settings
                    sh """
                        mvn clean test \
                            -Pweb-execution \
                            -Dsuite=local-suite \
                            -Dtarget=local-suite \
                            -Dheadless=true \
                            -Dbrowser=firefox
                    """
                }
            }
        }

        stage('Stop Docker Compose') {
            steps {
                script {
                    // Stop and remove Docker containers
                    sh 'docker-compose -f docker-compose-chrome-firefox-edge.yml down'
                }
            }
        }
    }

    post {
        always {
            // Publish Allure report as an HTML report
            publishHTML target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/allure-results',
                reportFiles: 'index.html',
                reportName: 'Test Results',
                reportTitles: 'Execution Report'
            ]
        }
    }
}
