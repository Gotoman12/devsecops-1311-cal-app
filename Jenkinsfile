pipeline {
    agent any 

    tools {
        jdk 'java-17'
        maven 'maven'
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
                git url: 'https://github.com/ManojKRISHNAPPA/devsecops-1311-cal-app.git', branch: 'main'
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

        stage('Build & SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
                }
            }
        }

        // stage("Quality Gate") {
        //     steps {
        //         timeout(time: 1, unit: 'HOURS') {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }

        // stage('Vulnerabilities Scan - OWASP & Trivy') {
        //     parallel {

        //         stage('OWASP Dependency Check') {
        //             steps {
        //                 sh 'mvn org.owasp:dependency-check-maven:check -Dformat=ALL'
        //             }
        //         }

        //         stage('Trivy Base Image Scan') {
        //             steps {
        //                 sh 'bash trivy-docker-image-scan.sh'
        //             }
        //         }

        //     }
        // }


        stage('Vulnerabilities Scan - OWASP & Trivy') {
            parallel {

                stage('Trivy Base Image Scan') {
                    steps {
                        sh 'bash trivy-docker-image-scan.sh'
                    }
                }

                stage('OPA confest'){
                    steps{
                      sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy dockerfile-security.rego Dockerfile'  
                    }
                }

            }
        }

        stage('Docker Image Creation') {
            steps {
                sh '''
                    printenv
                    docker build -t ${IMAGE_NAME} .
                '''
            }
        }

        stage('Docker-Login'){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                            sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                        }
                }
            }
        }

        stage('Docker-push'){
            steps{
                sh '''
                    docker push ${IMAGE_NAME}
                '''
            }
        }

        stage('Updating the K8 clsuter'){
            steps{
                sh '''
                    aws eks update-kubeconfig --region ${AWS_REGION} --name ${CLUSTER_NAME}
                '''
            }
        }

        stage('OPA-kubernetes'){
            steps{
                sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy opa-k8s-security.rego deployment.yml'
            }
        }

        stage('Deploying to EKS'){
            steps{
                withKubeConfig(caCertificate: '', clusterName: 'itkannadigaru-cluster', contextName: '', credentialsId: 'kube', namespace: 'microdegree', restrictKubeConfigAccess: false, serverUrl: 'https://AC0929E28609D9C6B318DA622D40A5FE.sk1.us-west-2.eks.amazonaws.com') {
                    sh " sed -i 's|replace|${IMAGE_NAME}|g' deployment.yml "
                    sh " kubectl apply -f deployment.yml -n ${NAMESPACE}"
                }
            }
        }        

    } 
} 


