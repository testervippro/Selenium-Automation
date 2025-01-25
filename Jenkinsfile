pipeline {
    agent any

    environment {
        IMAGE_NAME = 'cuxuanthoai/chrome-firefox-edge'
        IMAGE_TAG = 'latest'
        CONTAINER_NAME = 'chrome-firefox-edge-container'
        REPORT_DIR = 'target/allure-results'
    }

    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile in the current directory
                    sh """
                        docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                    """
                }
            }
        }

        stage('Run Maven Tests') {
            steps {
                script {
                    // Run the container and mount a volume for test results
                    sh """
                        docker run --rm --name ${CONTAINER_NAME} \
                            --shm-size 2gb \
                            -v ${WORKSPACE}/target/allure-results:/app/target/allure-results \
                            ${IMAGE_NAME}:${IMAGE_TAG}
                    """
                }
            }
        }
    }

    post {
        always {
            // Publish Allure or other reports after test execution
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

        success {
            echo 'Tests completed successfully!'
        }

        failure {
            echo 'Tests failed!'
        }
    }
}
