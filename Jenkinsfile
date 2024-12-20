node {
    def mvnHome

    stage('Preparation') {
        git 'https://github.com/testervippro/Selenium-Automation.git'
        mvnHome = tool 'M3'
    }

    stage('Build') {
        sh "'${mvnHome}/bin/mvn' clean package -DskipTests=true"
    }

    stage('Test Execution') {
        try {
            sh "'${mvnHome}/bin/mvn' test -Pweb-execution -Dsuite=local -Dtarget=local -Dheadless=true -Dbrowser=chrome "
        } catch (Exception e) {
            currentBuild.result = 'FAILURE'
        } finally {
            junit '**/target/surefire-reports/TEST-*.xml'

            /*
             * Please read https://wiki.jenkins.io/display/JENKINS/Configuring+Content+Security+Policy
             * to allow Jenkins to load static files
             */
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
