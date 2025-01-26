pipeline {
    agent any

    environment {
        // Define Maven home path
        MAVEN_HOME = ''
    }

    stages {
        stage('Checkout Code') {
            steps {
                retry(3) { // Retry the checkout up to 3 times in case of transient errors
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: '*/main']], // Replace with your branch name if different
                        extensions: [[$class: 'CloneOption', depth: 1, shallow: true]], // Shallow clone
                        userRemoteConfigs: [[
                            url: 'https://github.com/testervippro/Selenium-Automation.git', // Your repository URL
                            credentialsId: '72a36d73-7b27-4213-aa99-c560b1f83b90' // Your Jenkins credentials ID
                        ]]
                    ])
                }
            }
        }

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
                    sh "${MAVEN_HOME}/bin/mvn test -Pweb-execution -Dsuite=local-suite -Dtarget=local-suite -Dheadless=false -Dbrowser=chrome"
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
