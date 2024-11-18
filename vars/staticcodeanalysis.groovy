
def call(Map params) {
    sh """
        sonar-scanner \
            -Dsonar.projectKey=${params.sonarProjectKey} \
            -Dsonar.sources=${params.projectDir} \
            -Dsonar.host.url=http://localhost:9000 \
            -Dsonar.login=${params.sonarCredentialsId}
    """
}