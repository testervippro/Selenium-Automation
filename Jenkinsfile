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
            publishHTML (target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/surefire-reports',
                reportFiles: 'index.html',
                reportName: "TestNG Report"
            ])
        }
    }
}
