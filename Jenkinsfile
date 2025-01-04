pipeline {
    agent any

    stages {
        stage('Test Execution') {
            steps {
                script {
                    if (isUnix()) {
                        // For macOS and Linux
                        sh 'mvn test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome -Dauto.report=false'
                    } else {
                        // For Windows
                        bat 'mvn test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome -Dauto.report=false'
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
