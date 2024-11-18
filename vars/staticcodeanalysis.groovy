def call(String credentialsId, String projectDir, String sonarProjectKey) {
    withSonarQubeEnv(credentialsId: credentialsId) {
        sh """
            pylint ${projectDir} --output-format=html > pylint-report.html
            sonar-scanner \
            -Dsonar.projectKey=${sonarProjectKey} \
            -Dsonar.sources=${projectDir} \
            -Dsonar.python.pylint.reportPath=pylint-report.html \
            -Dsonar.language=py
        """
    }
}
