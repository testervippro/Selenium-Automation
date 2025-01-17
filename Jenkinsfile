pipeline {
    agent any

    stages {

        stage('Test Execution') {
            steps {
                script {
                    if (isUnix()) {
                        // For macOS and Linux, use Maven Wrapper (mvnw)
                        sh './mvnw test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome'
                    } else {
                        // For Windows, use Maven Wrapper (mvnw.cmd)
                        bat 'mvnw.cmd test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome'
                    }
                }
            }
        }
    }

    post {
        always {
            // Publish JUnit test results
            junit '**/target/surefire-reports/TEST-*.xml'

            pipeline {
    agent any

    stages {
        stage('Test Execution') {
            steps {
                bat 'mvn test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome'
            }
        }
    }

    post {
        always {

            junit '**/target/surefire-reports/TEST-*.xml'

            allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            
            publishHTML (target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/allure-report',
                reportFiles: 'index.html',
                reportName: "TestNG Report"
            ])
        }
    }
}


            // Publish HTML reports (TestNG report in this case)
            publishHTML (target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/surefire-reports', // Directory containing the HTML report
                reportFiles: 'index.html', // Entry point for the HTML report
                reportName: "TestNG Report"
            ])
        }
    }
}
