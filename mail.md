Here's a full Markdown guide for configuring email notifications in Jenkins with Gmail, including the **System Admin E-mail Address** configuration:

---

# **Configuring Email Notifications in Jenkins with Gmail**

Follow these steps to set up and use Gmail's SMTP server for sending emails from Jenkins.

---

## **Step 1: Set Up Your Google Account for SMTP Access**

1. **Enable 2-Step Verification**:
    - Go to [Google Account Security](https://myaccount.google.com/security).
    - Ensure **2-Step Verification** is set to **ON**.

2. **Generate an App Password**:
    - Navigate to [App Passwords](https://myaccount.google.com/apppasswords).
    - Select **Mail** as the app and **Other** for the device name (e.g., Jenkins).
    - Generate an app-specific password.
    - **Copy the generated password** (you'll need it in later steps).

---

## **Step 2: Configure Jenkins Location**

1. Navigate to **Manage Jenkins > Configure System**.
2. Scroll to the **Jenkins Location** section.
3. In the **System Admin e-mail address** field, enter a valid email address:
    - Example: `Jenkins Daemon <youremail@gmail.com>`.
4. Click **Save**.

---

## **Step 3: Configure E-mail Notification in Jenkins**

1. Navigate to **Manage Jenkins > Configure System**.
2. Scroll to the **E-mail Notification** section and configure the following settings:
    - **SMTP Server**: `smtp.gmail.com`.
    - **Default user e-mail suffix**: Leave blank or set to `@gmail.com`.
    - Check **Use SMTP Authentication**.
    - **User Name**: Your Gmail address (e.g., `youremail@gmail.com`).
    - **Password**: Enter the **App Password** generated in Step 1.
    - Check **Use SSL**.
    - **SMTP Port**: `465`.
    - **Charset**: Default (`UTF-8`).

3. (Optional) Configure the **Reply-To Address** to set a preferred reply-to email.

4. Click **Save**.

---

## **Step 4: Test E-mail Configuration**

1. In the **E-mail Notification** section, click **Test configuration by sending test e-mail**.
2. Enter a recipient email address (e.g., your Gmail address).
3. Click **Send Test E-mail**.
4. Check your inbox to verify that you receive the test email.

---

## **Step 5: Use Email Notifications in Jenkins Pipelines**

Add the following email notification script to your Jenkins pipeline:

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
            mail(
                to: 'recipient@example.com',
                subject: "Jenkins Build - ${currentBuild.currentResult}",
                body: """
                    The Jenkins build ${env.JOB_NAME} (#${env.BUILD_NUMBER}) has completed.
                    Status: ${currentBuild.currentResult}
                    Build URL: ${env.BUILD_URL}
                """
            )
        }
    }
}
```

---

## **Step 6: Troubleshooting Common Issues**

| **Problem**                                   | **Solution**                                                                 |
|-----------------------------------------------|-------------------------------------------------------------------------------|
| **"Address not configured yet" error**        | Ensure **System Admin e-mail address** is configured in **Jenkins Location**. |
| **Test email not sent**                       | Verify SMTP server settings and App Password in the E-mail Notification section. |
| **Using Gmail without 2-Step Verification**   | Not supported; enable 2-Step Verification and use an App Password.            |

---

By following these steps, you can successfully configure email notifications in Jenkins using Gmail.