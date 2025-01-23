pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    // Clone the repository from GitHub
                    git branch: 'main', url: 'https://github.com/testervippro/Selenium-Automation.git'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Run Maven tests against the Selenium Grid
                    if (isUnix()) {
                        sh """
                        ./mvnw clean test \
                            -Pweb-execution \
                            -Dsuite=selenium-grid \
                            -Dtarget=selenium-grid \
                            -Dheadless=true \
                            -Dbrowser=chrome
                        """
                    } else {
                        bat """
                        mvnw.cmd clean test ^
                            -Pweb-execution ^
                            -Dsuite=selenium-grid ^
                            -Dtarget=selenium-grid ^
                            -Dheadless=true ^
                            -Dbrowser=chrome
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            // Publish Allure report as HTML
            publishHTML(
                target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/allure-results',
                    reportFiles: 'index.html',
                    reportName: 'My Reports',
                    reportTitles: 'The Report'
                ]
            )
        }
    }
}
