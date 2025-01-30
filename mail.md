H

## **Set Up Google SMTP Access for Jenkins**

1. **Enable 2-Step Verification**:
   - Go to [Google Account Security](https://myaccount.google.com/security).
   - Turn **2-Step Verification** **ON**.

2. **Generate an App Password**:
   - Go to [App Passwords](https://myaccount.google.com/apppasswords).
   - Choose **Mail** as the app and **Other** for the device (e.g., "Jenkins").
   - Click **Generate** and copy the generated password.

---

## **Configure Jenkins Email Notification**

1. Go to **Manage Jenkins > Configure System**.
2. In the **Extended E-mail Notification** section, configure:
   - **SMTP Server**: `smtp.gmail.com`
   - **SMTP Port**: `465` (Use SSL)
   - **Use SSL**: Checked
   - **SMTP Authentication**: Checked
   - **User Name**: Your Gmail (e.g., `youremail@gmail.com`)
   - **Password**: The App Password you generated.
3. In the **E-mail Notification** section, set the **System Admin e-mail address** (e.g., `youremail@gmail.com`).
4. Click **Save**.

---

## **Example Jenkins Pipeline for Email Notification**

```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building...'
            }
        }
    }
    post {
        always {
            emailext(
                to: 'recipient@example.com',
                subject: "Build Status - ${currentBuild.currentResult}",
                body: "Build ${env.JOB_NAME} #${env.BUILD_NUMBER} finished with status: ${currentBuild.currentResult}."
            )
        }
    }
}
```

---

By following these steps, you’ll configure email notifications in Jenkins with Gmail and set up a pipeline to send build updates.