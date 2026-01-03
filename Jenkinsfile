pipeline {
    agent any 

    tools {
        jdk 'java-17'
        maven 'Maven'
    }

    environment {
        IMAGE_NAME = "manojkrishnappa/dev-sec-ops:${GIT_COMMIT}"
        AWS_REGION = "us-west-2"
        CLUSTER_NAME = "itkannadigaru-cluster"
        NAMESPACE = "microdegree"
    }

    stages {

        stage('Git checkout') {
            steps {
                git url: 'https://github.com/Gotoman12/devsecops-1311-cal-app.git', branch: 'main'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test-Case') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
            post {
                always {
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        inclusionPattern: '**/*.class'
                    )
                }
            }
        }  
    } 
} 


