pipeline {
    agent any 

    tools {
        jdk 'java-17'
        maven 'maven'
    }

    stages {
        stage('Git checkout') {
            steps {
                git url: 'https://github.com/ManojKRISHNAPPA/devsecops-1311-cal-app.git', branch: 'Test'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test & Coverage') {
            steps {
                sh 'mvn test jacoco:report'
            }
            post {
                always {
                    // Publish JaCoCo coverage report in Jenkins
                    jacoco(execPattern: 'target/jacoco.exec', classPattern: 'target/classes', sourcePattern: 'src/main/java', exclusionPattern: '')
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}
