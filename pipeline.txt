pipeline {
    agent any

    stages {
        stage('Checkout Repository') {
            steps {
                // Checkout the code from the repository using the Git plugin
                git(
                    url: 'https://github.com/testervippro/Selenium-Automation.git', // Repository URL
                    branch: 'main', // Branch name
                    credentialsId: '3e97af83-a30d-4b78-ad61-e2619023491a' // Jenkins Credential ID
                )
            }
        }

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
