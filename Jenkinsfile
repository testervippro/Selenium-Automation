pipeline {
    agent {
        dockerfile {
            dir '.'  // This specifies the directory with your Dockerfile (default is the root of the repository)
            filename 'Dockerfile'  // Use the Dockerfile to build the image
        }
    }

    stages {
        stage('Run Tests') {
            steps {
                script {
                    // Run Maven tests inside the Docker container
                    sh """
                        mvn --version
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
