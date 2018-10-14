pipeline {
    agent any
    stages {
        stage('Clone sources') {
            steps {
                echo 'Cloning: ' + env.BRANCH_NAME
                checkout scm
            }
        }

        stage('Unit test') {
            steps {
                // Make gradlew executable
                sh 'chmod +x gradlew'
                sh "./gradlew ktlintMainCheck testUnit"

            }
        }

        stage('Integration test') {
            steps {
                sh "./gradlew testIntegration"
            }
        }


        stage('Build') {
            steps {
                sh "./gradlew build"
            }
        }
    }
    post {
        always {
            step([$class: 'JacocoPublisher'])
            step([$class : 'hudson.plugins.checkstyle.CheckStylePublisher',
                  pattern: 'build/reports/ktlint/ktlintMainCheck.xml'])

            archiveArtifacts artifacts: 'build/libs/**/*.jar, build/reports/**', fingerprint: true
            junit 'build/test-results/test/*.xml'
        }
    }
}