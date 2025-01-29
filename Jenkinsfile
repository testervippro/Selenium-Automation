pipeline {
    agent any
    environment {
        //sample 
        MAVEN_HOME = tool name: 'maven', type: 'maven'
        JENKINS_SERVER_URL = 'https://d1f-116-96-46-98.ngrok-free.app'
        EMAIL_RECIPIENT = '@gmail.com'
        TELEGRAM_TOKEN = '891845500:AAEAJ5GDawMGUC4Ofv9SvD3YBn5UGmVii7Q'
        TELEGRAM_CHAT_ID = '321745388'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/testervippro/Selenium-Automation.git'
            }
        }
        stage('Build') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn clean -DskipTests=true"
            }
        }
        stage('Test') {
            steps {
                sh "${MAVEN_HOME}/bin/mvn test -Pweb-execution -Dsuite=local-suite -Dtarget=local-suite -Dheadless=true -Dbrowser=chrome"
            }
        }
    }
    post {
        always {
            script {
                allure results: [[path: 'target/allure-results']], reportBuildPolicy: 'ALWAYS'
                
                // Get environment details
                def gitBranch = env.GIT_BRANCH ?: 'Unknown'
                def gitCommit = env.GIT_COMMIT ?: 'Unknown'
                def gitCommitMessage = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                
                // Send email and telegram reports
                sendEmailReport(gitBranch, gitCommit, gitCommitMessage)
                sendTelegramReport()
            }
        }
        success {
            echo 'Tests passed!'
        }
        failure {
            echo 'Tests failed!'
        }
    }
}

def sendEmailReport(gitBranch, gitCommit, gitCommitMessage) {
    emailext(
        to: EMAIL_RECIPIENT,
        subject: "[Jenkins Build] ${env.JOB_NAME} - ${currentBuild.currentResult} (#${env.BUILD_NUMBER})",
        body: """
            <html><body>
                <p>Build details:</p>
                <ul>
                    <li>Status: ${currentBuild.currentResult}</li>
                    <li>Duration: ${currentBuild.durationString}</li>
                    <li>Allure Report: <a href="${env.JENKINS_SERVER_URL}/job/${env.JOB_NAME}/${env.BUILD_NUMBER}/allure">View</a></li>
                    <li>Git Branch: ${gitBranch}</li>
                    <li>Git Commit: ${gitCommit}</li>
                    <li>Commit Message: ${gitCommitMessage}</li>
                </ul>
            </body></html>
        """,
        mimeType: 'text/html',
        attachLog: true, 
        attachmentsPattern: '**/build.log'
    )
}

def sendTelegramReport() {
    sh "zip -r allure-report.zip ./allure-report"
    sh """
        curl -X POST \
        -F chat_id=${TELEGRAM_CHAT_ID} \
        -F document=@allure-report.zip \
        -F caption="Allure Report for Build #${env.BUILD_NUMBER}" \
        https://api.telegram.org/bot${TELEGRAM_TOKEN}/sendDocument
    """
}
