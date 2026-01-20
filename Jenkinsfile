pipeline{
    agent any
    tools{
        jdk "java-17"
        maven "maven"
    }

    environment{
        IMAGE_NAME= "arjunckm/cloth-app:${BUILD_NUMBER}"
    }
    stages{
        stage("Git-Checkout"){
            steps{
                git url:"https://github.com/Gotoman12/Calulator-springboot.git", branch:"main"
            }
        }
        stage("Build"){
            steps{
                sh 'mvn clean compile'
            }
        }
        stage("Test"){
            steps{
                sh 'mvn test'
            }
        }
        stage("Package"){
            steps{
                sh 'mvn clean package'
            }
        }
        stage("Unit Testing"){
            steps{
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
        stage("SonarQube-testing"){
            steps{
                sh '''
                         mvn sonar:sonar \
                        -Dsonar.projectKey=cal-app \
                        -Dsonar.host.url=http://20.197.49.99:9000 \
                        -Dsonar.login=0d45f5d5bfc41d5ded81a699d68bd3da97226ce5
                '''
            }
        }
        // stage("Sonar-Qube"){
        //             parallel{
        //                 stage("SonarQube-Analysis"){
        //                 steps{
        //                     script{
        //                     withSonarQubeEnv(credentialsId: 'sonarqube'){
        //                         sh 'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
        //                     }
        //                 }
        //             }
        //         }
        //        stage("Quality Gate"){
        //             steps{
        //                 timeout(time: 1, unit: 'HOURS') {
        //                     // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
        //                     // true = set pipeline to UNSTABLE, false = don't
        //                     waitForQualityGate abortPipeline: true
        //                 }
        //             }
        //         } 
        //     }
        // }
    //     stage("SonarQube-Analysis"){
    //         steps{
    //             script{
    //                 withSonarQubeEnv(credentialsId: 'SonarQube'){
    //                     sh 'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
    //                 }
    //             }
    //         }
    //     }
    //     stage("Quality Gate"){
    //         steps{
    //             timeout(time: 1, unit: 'HOURS') {
    //                 // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
    //                 // true = set pipeline to UNSTABLE, false = don't
    //                 waitForQualityGate abortPipeline: true
    //         }
    //     }
    // }
    // stage("OWASP-Dependency Check"){
    //     steps{
    //         sh 'mvn org.owasp:dependency-check-maven:check -Dformat=ALL'
    //     }
    // }

    // Scanning the the base image used in the docker file
    stage("Docker Image Scan"){
        parallel{
          stage("Trivy Scan for Docker base image"){
            steps{
                sh '''
                    chmod +x trivy-docker-image-scan.sh
                    bash trivy-docker-image-scan.sh
                '''
                }
            }
            stage('OPA confest'){
               steps{
                      sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest:latest test --policy dockerfile-security.rego Dockerfile'  
                    }
                }
        }
    }
    stage("Docker Build"){
        steps{
          sh 'docker build -t ${IMAGE_NAME} .'
         }
      }
    }
}