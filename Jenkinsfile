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
                    -Dsonar.host.url=http://34.220.193.218:9000 \
                    -Dsonar.login=b9f45956612ec722b9471af172f8f74cec8ac6bd
                '''
            }
        }

        stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
                    // true = set pipeline to UNSTABLE, false = don't
                    waitForQualityGate abortPipeline: true
                }
            }
        }     

    }
}
