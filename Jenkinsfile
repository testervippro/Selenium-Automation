pipeline {
    agent any

    stages {
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