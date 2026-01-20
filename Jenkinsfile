pipeline{
    agent any

    parameters {
        choice(name:"TerraformAction",choices:["apply","destroy"],description:"Terraform actions")
    }

    environment{
        AWS_ACCESS_KEY = credentials('AWS_ACCESS_KEY')
        AWS_SECRET_KEY = credentials('AWS_SECRET_KEY')
        SNYK_TOKEN = credentials('SNYK_TOKEN')
    }

    stages{
        stage("checkout"){
            steps{
                git url: 'https://github.com/Gotoman12/devsecops-1311-cal-app.git',branch: 'IAC-Scan'
            }
               
        }
        stage('Snyk Security Scan'){
            steps{
                sh '''
                eks/; chmod 777 iac-scan-synk.sh
                cd eks/;./iac-scan-synk.sh
                '''
            }
        }
        stage("terraform-plan"){
            steps{
                sh 'cd eks/; terraform init'
                sh 'cd eks/; terraform validate'
                sh 'cd eks/; terraform plan -out=tfplan'
            }   
        }
        stage('Approval'){
            steps{
                script{
                    input message: "Do you want to proceed with Terraform ${params.TerraformAction}?"
                }
            }
        }
        stage('Terraform Apply / Destroy'){
            steps{
                sh '''
                cd eks
                if [ "${TerraformAction}" = "apply" ]; then
                    terraform apply tfplan
                else
                    terraform destroy -auto-approve
                fi
                '''
            }
        }
    }
}