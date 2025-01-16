pipeline {
    agent any

    stages {
        stage('Test Execution') {
            steps {
                script {
                    if (isUnix()) {
                        // For macOS and Linux, use the Maven Wrapper
                        sh './mvnw test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome -Dauto.report=false'
                    } else {
                        // For Windows, use the Maven Wrapper
                        bat 'mvnw.cmd test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome -Dauto.report=false'
                    }
                }
            }
        }
    }

    post {
        always {
            // Publish JUnit test results
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
