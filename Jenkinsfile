pipeline {   
	agent {     
	    label 'Slave_Induccion'   
	} 
	
	options {   
		buildDiscarder(logRotator(numToKeepStr: '3'))
		disableConcurrentBuilds()   
	} 
  
	tools {     
		jdk 'JDK8_Centos'
		gradle 'Gradle4.5_Centos' 
	} 
  
	stages{     
		stage('Checkout') {       
			steps{         
				echo "------------>Checkout<------------"       
			}     
		}       
		
		stage('Unit Tests') {       
			steps{         
				echo "------------>Unit Tests<------------" 
			}     
		} 
		
		stage('Integration Tests') {
			steps {
				echo "------------>Integration Tests<------------"
			} 
		} 
		
		stage('Static Code Analysis') {       
			steps{
				echo '------------>Análisis de código estático<------------'
				withSonarQubeEnv('Sonar') { 
					sh "${tool name: 'SonarScanner', type:'hudson.plugins.sonar.SonarRunnerInstallation'}/bin/sonar-scanner -Dproject.settings=sonar-project.properties" 
				}       
			}     
		}
		
		stage('Build') {
			steps {
				echo "------------>Build<------------"
			}
		} 
	}
	
	
	post {
		always {
			echo 'This will always run'
		}
		
		success {
			echo 'This will run only if successful'
		}
		
		failure {
			echo 'This will run only if failed'
		}
		
		unstable {
			echo 'This will run only if the run was marked as unstable'
		}   

		changed {
			echo 'This will run only if the state of the Pipeline has changed'
			echo 'For example, if the Pipeline was previously failing but is now successful'
		}
	}
}
