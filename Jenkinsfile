pipeline {
    agent any 

    tools {
        jdk 'java-17'
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

        stage('Verify'){
            steps{
                sh 'mvn clean verify'
            }
        }

        stage('Code Coverage'){
            steps{
                sh 'mvn jacoco:report'
            }

            post{
                always{
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        inclusionPattern: '**/*.class'
                    )
                }
            }
        }  

        stage('Sonar-Qube-Analysis'){
            steps{
                sh '''
                    mvn sonar:sonar \
                        -Dsonar.projectKey=cal-app \
                        -Dsonar.host.url=http://44.247.38.123:9000 \
                        -Dsonar.login=9bc4cbb7d017932545e885e7449cbe99648a61cd
                '''
            }
        }      

    }
}
