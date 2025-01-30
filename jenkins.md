

## **Running Jenkins with Docker Compose Using Pre-configured Files**

### **Step 1: Install Required Tools and Run the Jenkins Container**

Before running Jenkins, ensure you have **Docker** installed on your system. You can install it from the [Docker website](https://www.docker.com/products/docker-desktop).

Once Docker is ready, run the Jenkins container using Docker Compose:

```bash
docker-compose -f docker-compose-jenkins-as-code.yml up -d
```

This command does the following:

1. **Installs Jenkins and Plugins**: Installs the required plugins listed in `plugins.txt` (e.g., **Matrix Auth**, **Job DSL**, **Email Extension(Mailler)**).
2. **Configures Users**: Creates the `admin`, `viewer`, and `developer` users from `config.yml`, with  passwords.
3. **Installs Tools**: Installs tools like **Node.js**, **Maven**, **Git**, and **Allure** inside the Jenkins container.

---

### **Step 2: Log in to Jenkins**

- **Admin**, **Viewer**, and **Developer** users are created with passwords from the `config.yml` file.
- Tools like **Node.js**, **Maven**, **Git**, and **Allure** are automatically installed during the setup.

Now, you can log in to Jenkins with the configured users and start using Jenkins.

---
