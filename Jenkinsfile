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
                     // Publish test results
                    junit testResults: '**/target/surefire-reports/*.xml',
                  allowEmptyResults: true
                   // Publish JaCoCo coverage in Jenkins
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        inclusionPattern: '**/*.class'
                    )
                    emailext(
                subject: "JaCoCo Coverage Report - Build #${BUILD_NUMBER}",
                mimeType: 'text/html',
                to: 'mallikarjunckm@gmail.com',
                body: """
                <h2>Build Status: ${currentBuild.currentResult}</h2>

                <p><b>Job:</b> ${JOB_NAME}</p>
                <p><b>Build:</b> #${BUILD_NUMBER}</p>

                <p>
                📊 <b>JaCoCo Coverage Report:</b><br>
                <a href="${BUILD_URL}jacoco/">View Coverage</a>
                </p>

                <p>
                🧪 <b>Test Results:</b><br>
                <a href="${BUILD_URL}testReport/">View Test Report</a>
                </p>
                """
            )
                }
            }
        }
       /*  stage("SonerQube"){
            steps{
                sh'''mvn sonar:sonar \
                    -Dsonar.projectKey=calculator \
                    -Dsonar.host.url=http://54.81.88.152:9000 \
                    -Dsonar.login=221ea1c81c4ce8d5fa83f6526bb55f48bad43ce6
                '''
            }
        } */
        stage('build && SonarQube analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
                    }
                }
            }
        stage("QualityGate-check"){
            steps{
                sh'''
                    timeout(time: 1, unit: 'HOURS') {
                    // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
                    // true = set pipeline to UNSTABLE, false = don't
                    waitForQualityGate abortPipeline: true
                }
                '''
            }
        }
    } 
} 


