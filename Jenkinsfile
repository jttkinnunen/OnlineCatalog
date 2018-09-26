

node {

    stage('Clone sources') {
        echo 'Cloning: ' + env.BRANCH_NAME
        checkout scm
        git url: 'https://github.com/Project2018FCG/OnlineCatalog.git',
            credentialsId: '85005edc-27f1-4eb4-aaf7-54f6ba45ff02',
            branch: env.BRANCH_NAME
    }

    stage('Unit test') {
        // Make gradlew executable
        sh 'chmod +x gradlew'
        sh "./gradlew ktlintCheck test jacocoTestReport"
        junit 'build/test-results/test/*.xml'
        step( [ $class: 'JacocoPublisher' ] )
        step([$class: 'hudson.plugins.checkstyle.CheckStylePublisher',
              checkstyle: 'build/reports/ktlint/ktlintMainCheck.xml'])
    }


    stage('Build') {
        sh "./gradlew build"
    }
}