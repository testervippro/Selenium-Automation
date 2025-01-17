pipeline {
    agent any

    tools {
        allure 'allure'  // Allure tool configured in Jenkins Global Tool Configuration
    }

    
        stage('Test Execution') {
            steps {
                script {
                    if (isUnix()) {
                        // Run tests on Unix-based systems using Maven Wrapper and specify the suite file
                        sh './mvnw test -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome -DsuiteXmlFile=Selenium-Automation/src/test/resources/suites/local.xml'
                    } else {
                        // Run tests on Windows using Maven Wrapper and specify the suite file
                        bat 'mvnw.cmd test -Dsuite=local -Dtarget=local -Dheadless=false -Dbrowser=chrome -DsuiteXmlFile=Selenium-Automation/src/test/resources/suites/local.xml'
                    }
                }
            }
        }
    }

    post {
        always {
            // Publish Allure test reports
            allure(
                includeProperties: false,
                jdk: '', 
                results: [[path: 'target/allure-results']]
            )
        }
    }
}
