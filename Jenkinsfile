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
                        // For macOS/Linux, run the Maven Wrapper using the shell script
                        sh '''#!/bin/bash
                        ./mvnw clean test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome -Dauto.report=false
                        '''
                    } else {
                        // For Windows, run the Maven Wrapper using the batch script
                        bat '''mvnw.cmd clean test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome -Dauto.report=false'''
                    }
                }
            }
        }
    }

    post {
        always {
             // Publish HTML report (TestNG report in this case)
                        publishHTML (target: [
                            allowMissing: false,
                            alwaysLinkToLastBuild: true,
                            keepAll: true,
                            reportDir: 'target/surefire-reports',
                            reportFiles: 'index.html',
                            reportName: " Report"
                        ])
        }
    }
}
