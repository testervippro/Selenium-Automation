pipeline {
    agent any

    stages {
        stage('Prepare Environment') {
            steps {
                script {
                    // Ensure the permissions on .m2 directory
                    if (isUnix()) {
                        sh 'chmod -R 755 ~/.m2 || true'
                    } else {
                        bat 'icacls %USERPROFILE%\\.m2 /grant Everyone:F'
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Use Docker image to run tests
                    docker.image('cuxuanthoai/chrome-firefox-edge').inside {
                        if (isUnix()) {
                            sh "./mvnw clean test -Pweb-execution -Dsuite=selenium-grid -Dtarget=local-suite -Dheadless=true -Dbrowser=chrome"
                        } else {
                            bat "mvnw.cmd clean test -Pweb-execution -Dsuite=selenium-grid -Dtarget=selenium-grid -Dheadless=true -Dbrowser=chrome"
                        }
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
