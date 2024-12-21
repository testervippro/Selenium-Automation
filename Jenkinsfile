pipeline {
    agent any

    stages {
        stage('Test Execution') {
            steps {
                dir('selenium-java-automation') {
                    bat 'mvn test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome'
                    /*
                    bat 'mvn test -Pweb-execution -Dsuite=local-suite -Dtarget=local-suite -Dheadless=false -Dbrowser=chrome'
                    */
                }
            }
        }

    
    }

    post {
        always {
            dir('selenium-java-automation') {
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
}