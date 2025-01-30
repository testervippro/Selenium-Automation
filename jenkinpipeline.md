
---

### **Step 1: Set Up Ngrok with Docker ( to share local port to internet)**



2. **Run Ngrok to Expose Port 8080 (it will auto pull image):**

   ```bash
   docker run --net=host -it -e NGROK_AUTHTOKEN=your_ngrok_authtoken -p 8080:8080 ngrok/ngrok http 8080
   ```

   Replace `your_ngrok_authtoken` with your authtoken.

3. **Check Ngrok Status:**  
   After running, Ngrok will give you a public URL to access your local app (e.g., `http://<random_subdomain>.ngrok.io`).

---

### **Step 2: Send File to Telegram via Jenkins Pipeline**

1. **Get Telegram Bot Token & Chat ID:**
    - Create a bot via [BotFather](https://core.telegram.org/bots#botfather) for your token.
    - Get your chat ID using `https://api.telegram.org/bot<TELEGRAM_TOKEN>/getUpdates`.

2. **Jenkins Pipeline Script:**

   ```groovy
   pipeline {
       agent any

       environment {
           TELEGRAM_TOKEN = 'your_telegram_token'
           TELEGRAM_CHAT_ID = 'your_chat_id'
       }

       stages {
           stage('Send File to Telegram') {
               steps {
                   script {
                       def fileToSend = 'path_to_your_file/example.txt'
                       
                       // Send file via Telegram Bot API
                       sh """
                           curl -X POST https://api.telegram.org/bot\$TELEGRAM_TOKEN/sendDocument \
                           -F chat_id=\$TELEGRAM_CHAT_ID \
                           -F document=@\$fileToSend
                       """
                   }
               }
           }
       }
   }
   ```

   Replace placeholders with your actual values.

---

### **References:**
- [Ngrok Documentation](https://ngrok.com/docs)
- [Telegram Bot API](https://core.telegram.org/bots/api)

---