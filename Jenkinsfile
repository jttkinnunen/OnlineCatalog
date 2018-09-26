

node {

    stage('Clone sources') {
        echo 'Cloning: ' + env.BRANCH_NAME
        checkout scm
        git url: 'https://github.com/Project2018FCG/OnlineCatalog.git', //url: 'git@github.com:Project2018FCG/OnlineCatalog.git',
            credentialsId: '85005edc-27f1-4eb4-aaf7-54f6ba45ff02',
            branch: env.BRANCH_NAME
    }

    stage('Unit test') {
        sh "./OnlineCatalog/gradlew test"
    }

    stage('Build') {
        sh "./OnlineCatalog/gradlew test"
    }
}