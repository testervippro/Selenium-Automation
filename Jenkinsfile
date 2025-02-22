pipeline {
    agent any

    environment {
        // Define Maven home path sample use mailler
        //change  your token
        MAVEN_HOME = ''
        JENKINS_SERVER_URL = 'https://99999999.ngrok-free.app'
        EMAIL_RECIPIENT = '@gmail.com'
        TELEGRAM_TOKEN = ':' // Your Telegram bot token
        TELEGRAM_CHAT_ID = '' // Your Telegram chat ID
    }

    stages {
        
         stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/testervippro/Selenium-Automation.git'
            }
        }
        
        
        stage('Preparation') {
            steps {
                script {
                     echo "Current working directory: ${pwd()}"
                    MAVEN_HOME = tool name: 'maven', type: 'maven'
                    echo "Maven Home: ${MAVEN_HOME}"
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    sh "${MAVEN_HOME}/bin/mvn clean -DskipTests=true"
                }
            }
        }

        stage('Test Execution') {
            steps {
                script {
                    echo "Current working directory: ${pwd()}"
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

                // Collect environment details
                def gitBranch = env.GIT_BRANCH ?: 'Unknown'
                def gitCommit = env.GIT_COMMIT ?: 'Unknown'
                def gitCommitMessage = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                def buildExecutor = env.BUILD_USER ?: 'Unknown'

                archiveArtifacts artifacts: 'allure-report.zip', allowEmptyArchive: true
                // Send email report
                sendEmailReport(gitBranch, gitCommit, gitCommitMessage, buildExecutor)

                // Zip and send Allure report via Telegram
                sendTelegramReport()
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

// Function to send the email report with build.log attached
def sendEmailReport(gitBranch, gitCommit, gitCommitMessage, buildExecutor) {
    echo "Sending email report..."

    def emailSubject = "[Jenkins Build] ${env.JOB_NAME} - ${currentBuild.currentResult} - (#${env.BUILD_NUMBER})"
    def emailBody = """
        <html>
            <body>
                <p>Hello,</p>
                <p>The Jenkins build for <strong>${env.JOB_NAME}</strong> (#${env.BUILD_NUMBER}) has completed.</p>
                <ul>
                    <li><strong>Status:</strong> ${currentBuild.currentResult}</li>
                    <li><strong>Build Duration:</strong> ${currentBuild.durationString}</li>
                    <li><strong>Allure Report:</strong> <a href="${env.JENKINS_SERVER_URL}/job/${env.JOB_NAME}/${env.BUILD_NUMBER}/allure">View Allure Report</a></li>
                    <li><strong>Build URL:</strong> <a href="${env.JENKINS_SERVER_URL}/job/${env.JOB_NAME}/${env.BUILD_NUMBER}">Open Build Details</a></li>
                    <li><strong>Git Branch:</strong> ${gitBranch}</li>
                    <li><strong>Git Commit:</strong> ${gitCommit}</li>
                    <li><strong>Git Commit Message:</strong> ${gitCommitMessage}</li>
                    <li><strong>Build Executor:</strong> ${buildExecutor}</li>
                </ul>
                <p>Best regards,<br><strong>testervippro</strong></p>
            </body>
        </html>
    """

    emailext(
        to: EMAIL_RECIPIENT,
        subject: emailSubject,
        body: emailBody,
        mimeType: 'text/html',
        attachLog: true, // Attach the build log file
        //compressLog: true // Compress the build log before attaching zip 
        attachmentsPattern: '**/build.log'
        
    )

    echo "Email report sent successfully."
}

// Function to zip and send the Allure report via Telegram
def sendTelegramReport() {
    echo "Zipping Allure report..."

    // Compress the Allure report directory into a zip file
    sh """
        zip -r allure-report.zip ./allure-report
    """
    echo "Allure report zipped successfully."

    echo "Sending Telegram report..."

    def caption = "Allure Report for Jenkins Build #${env.BUILD_NUMBER}"

    // Use Telegram API to send the zip file
    sh """
        curl -X POST \
        -F chat_id=${env.TELEGRAM_CHAT_ID} \
        -F document=@allure-report.zip \
        -F caption="${caption}" \
        https://api.telegram.org/bot${env.TELEGRAM_TOKEN}/sendDocument
    """
    echo "Telegram report sent successfully."
}
