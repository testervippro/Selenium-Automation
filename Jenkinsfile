pipeline {
    agent any  // Define no global agent to ensure stages have their own agent

    stages {
        stage('Run Tests') {
            agent {
                docker {
                    image 'cuxuanthoai/chrome-firefox-edge'  // Use the specified Docker image for the container
                   
                }
            }
            steps {
                script {
                    // Run Maven tests inside the Docker container
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
