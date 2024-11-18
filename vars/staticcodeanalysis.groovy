def call(String credentialsId, String projectDir, String sonarProjectKey) {
    withSonarQubeEnv(credentialsId: credentialsId) {
        script {
            try {
                // Check if the virtual environment exists and activate it (if applicable)
                if (fileExists("${projectDir}/venv/bin/activate")) {
                    echo "Activating virtual environment"
                    // Activate virtual environment (using . for shell compatibility)
                    sh """
                        . ${projectDir}/venv/bin/activate
                        pylint ${projectDir} --output-format=html > pylint-report.html
                    """
                } else {
                    echo "Virtual environment not found. Skipping pylint execution."
                    sh "pylint ${projectDir} --output-format=html > pylint-report.html"
                }

                // Run SonarQube analysis with the Pylint report
                sh """
                    sonar-scanner \
                    -Dsonar.projectKey=${sonarProjectKey} \
                    -Dsonar.sources=${projectDir} \
                    -Dsonar.python.pylint.reportPath=pylint-report.html \
                    -Dsonar.language=py
                """
            } catch (Exception e) {
                echo "An error occurred during linting or SonarQube analysis: ${e.message}"
                currentBuild.result = 'FAILURE' // Mark the build as failed if an error occurs
                throw e // Rethrow exception to fail the stage
            } finally {
                // Clean up the pylint report if not needed for future stages
                sh 'rm -f pylint-report.html'
            }
        }
    }
}
