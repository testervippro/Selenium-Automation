pipeline {
    agent any

    environment {
        // Define Maven home path
        MAVEN_HOME = ''
    }

    stages {
        stage('Preparation') {
            steps {
                script {
                    // Set MAVEN_HOME using the Maven tool configured in Jenkins
                    MAVEN_HOME = tool name: 'maven', type: 'maven'
                    echo "Maven Home: ${MAVEN_HOME}"
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // Run Maven clean and package (skip tests)
                    sh "${MAVEN_HOME}/bin/mvn clean package -DskipTests=true"
                }
            }
        }

        stage('Test Execution') {
            steps {
                script {
                    // Run Maven tests with specific profiles and parameters
                    sh "${MAVEN_HOME}/bin/mvn test -Pweb-execution -Dsuite=local-suite -Dtarget=local-suite -Dheadless=true -Dbrowser=chrome"
                }
            }
        }
    }

    post {
        always {
            script {
                // Generate Allure report
                allure([
                    results: [[path: 'target/allure-results']],
                    reportBuildPolicy: 'ALWAYS',
                    includeProperties: false
                ])
            }
        }

        success {
            echo 'Tests completed successfully!'
        }

        failure {
            echo 'Tests failed!'
        }
    }
}
