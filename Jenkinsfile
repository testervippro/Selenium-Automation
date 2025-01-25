pipeline {
    agent any

    environment {
        IMAGE_NAME = 'cuxuanthoai/chrome-firefox-edge'
        IMAGE_TAG = 'latest'
        CONTAINER_NAME = 'chrome-firefox-edge-container'
        REPORT_DIR = 'target/allure-results'
    }

    stages {
        stage('Prepare Report Directory') {
            steps {
                script {
                    // Ensure the report directory exists on the Jenkins host machine
                    sh "mkdir -p ${WORKSPACE}/${REPORT_DIR}"
                    
                    // Fix permissions for the report directory to ensure Jenkins can access it
                    sh "chmod -R 777 ${WORKSPACE}/${REPORT_DIR}"  // Make it readable, writable, and executable by anyone
                }
            }
        }

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

        stage('Run Tests in Docker') {
            steps {
                script {
                    // Run the container from the built image, set permissions for the report directory, and run the tests
                    sh """
                        docker run --rm --name ${CONTAINER_NAME} \
                            --shm-size 2gb \
                            -v ${WORKSPACE}/${REPORT_DIR}:${WORKSPACE}/${REPORT_DIR} \
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
                reportDir: "${WORKSPACE}/${REPORT_DIR}",
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
