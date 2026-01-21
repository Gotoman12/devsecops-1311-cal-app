pipeline{
    agent any
    tools{
        jdk "java-17"
        maven "maven"
    }

    environment{
        IMAGE_NAME= "arjunckm/cloth-app:${BUILD_NUMBER}"
        AWS_REGION = "us-east-1"
        CLUSTER_NAME = "itkannadigaru-cluster"
        NAMESPACE = "itkannadigaru"
    }
    stages{
        stage("Git-Checkout"){
            steps{
                git url:"https://github.com/Gotoman12/devsecops-1311-cal-app.git", branch:"main"
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
        //  Unit testing 
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
        // stage("SonarQube-testing"){
        //     steps{
        //         sh '''
        //                 mvn sonar:sonar \
        //                 -Dsonar.projectKey=cal-app-test \
        //                 -Dsonar.host.url=http://44.211.30.29:9000 \
        //                 -Dsonar.login=d7d1ac88a9e82dbaa1e5e881d1b8b610fb7d2a36
        //         '''
        //     }
        // }

      //  SAST: SonarQube-Analysis, Quality Gate 
        stage("SonarQube-Analysis"){
            steps{
                script{
                    withSonarQubeEnv(credentialsId:'SonarQube'){
                       sh 'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
                    }
                }
            }
        }
        stage("Quality Gate"){
            steps{
                timeout(time: 1, unit: 'HOURS') {
                    // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
                    // true = set pipeline to UNSTABLE, false = don't
                    waitForQualityGate abortPipeline: true
            }
        }
    }
    // SCA stage : OWASP Dependency Check
    // stage("OWASP-Dependency Check"){
    //     steps{
    //         sh 'mvn org.owasp:dependency-check-maven:check -Dformat=ALL'
    //     }
    // }
    
    //Trivy Scan : Docker Image Scan
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
            // Container Policy : OPA Conftest
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
    stage("docker-login"){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CRED', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                       sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                  }
                }
            }
        }
        stage("Dokcer Push"){
            steps{
                sh 'docker push ${IMAGE_NAME}'
            }
        }
          stage('Updating the K8 clsuter'){
            steps{
                sh '''
                    aws eks update-kubeconfig --region ${AWS_REGION} --name ${CLUSTER_NAME}
                '''
            }
        }
        // K8s Policy Validation : OPA-kubernetes
        stage('OPA-kubernetes'){
            steps{
                sh 'docker run --rm -v $(pwd):/project openpolicyagent/conftest test --policy opa-k8s-security.rego deployment.yml'
            }
        }
        stage('Deploying to EKS'){
            steps{
                withKubeConfig(caCertificate: '', clusterName: 'itkannadigaru-cluster', contextName: '', credentialsId: 'kube', namespace: '${NAMESPACE}', restrictKubeConfigAccess: false, serverUrl: 'https://7B0A7F823BEDE80E3A4CECA5203267DF.gr7.us-east-1.eks.amazonaws.com') {
                    sh " sed -i 's|replace|${IMAGE_NAME}|g' deployment.yml "
                    sh " kubectl apply -f deployment.yml -n ${NAMESPACE}"
                }
            }
        }
    }
}