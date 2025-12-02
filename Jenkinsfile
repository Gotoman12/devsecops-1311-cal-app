pipeline {
    agent any 

    tools {
        jdk 'java-21'
        maven 'maven'
    }

    stages{
        stage('Git checkout'){
            steps{
                git url: 'https://github.com/ManojKRISHNAPPA/devsecops-1311-cal-app.git', branch: 'main'
            }
        }

        stage('Compile'){
            steps{
                sh 'mvn clean compile'
            }
        }
        stage('Test-Case'){
            steps{
                sh 'mvn clean test'
            }
        }  
        stage('Package'){
            steps{
                sh 'mvn clean package'
            }
        }          

    }

}