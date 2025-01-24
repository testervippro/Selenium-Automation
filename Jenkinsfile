pipeline {
    agent {
        docker {
            image 'cuxuanthoai/chrome-firefox-edge'
        }
    }

    stages {


        stage('Run Tests') {
            steps {
                script {
                    // Run Maven tests based on the operating system
                    if (isUnix()) {
                        sh """
                            mvn clean test \
                                -Pweb-execution \
                                -Dsuite=local-suite \
                                -Dtarget=local-suite \
                                -Dheadless=true \
                                -Dbrowser=firefox
                        """
                    } else {
                        bat """
                            mvnw.cmd clean test ^
                                -Pweb-execution ^
                                -Dsuite=local-suite ^
                                -Dtarget=local-suite ^
                                -Dheadless=true ^
                                -Dbrowser=firefox
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            // Publish Allure report as HTML
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
