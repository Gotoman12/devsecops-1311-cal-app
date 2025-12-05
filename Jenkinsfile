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

        stage('Build & SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
                }
            }
        }

        stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }   

        // stage('OWASP FS Scan') {
        //     steps {
        //         dependencyCheck additionalArguments: '''
        //             --scan ./ 
        //             --disableYarnAudit 
        //             --disableNodeAudit 
        //             --nvdApiKey 0ad9f72c-7dcd-4a1d-af36-83d8cc7f3526 
        //             --noupdate
        //         ''', odcInstallation: 'DC'

        //         archiveArtifacts(
        //             allowEmptyArchive: true, 
        //             artifacts: '**/dependency-check-report.xml'
        //         )
        //         dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
        //     }
        // }
        
        stage('Vulnarabilties scan-OWASP'){
            parallel {
                stage('Dependency Check') {
                    steps {
                        sh 'mvn org.owasp:dependency-check-maven:check -Dformat=ALL'
                    }
                }

                stage('Trivy base image scan') {
                    steps {
                        sh 'bash trivy-docker-image-scan.sh'
                    }
                }
        }

        stage('Docker-Image-creation'){
            steps{
                sh '''
                    docker build -t cal-app:1 .
                '''
            }
        }
    }
}
