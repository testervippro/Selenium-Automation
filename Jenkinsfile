pipeline {
    agent any

    stages {
        stage('Start Selenium Grid') {
            steps {
                script {
                    // Start the Selenium Grid using docker-compose
                    sh 'docker-compose -f docker-compose-v3-dynamic-grid.yml up -d'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Run Maven tests against the Selenium Grid
                    if (isUnix()) {
                        sh "./mvnw clean test -Pweb-execution -Dsuite=selenium-grid -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome"
                    } else {
                        bat "mvnw.cmd clean test -Pweb-execution -Dsuite=selenium-grid -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome"
                    }
                }
            }
        }
    }

    post {
        always {
            // Stop and remove the Selenium Grid containers
            script {
                sh 'docker-compose -f docker-compose-v3-dynamic-grid.yml down'
            }

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