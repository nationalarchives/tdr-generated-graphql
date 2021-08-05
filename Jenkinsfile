library("tdr-jenkinslib")

def versionBumpBranch = "version-bump-${BUILD_NUMBER}"
def repo = "tdr-generated-graphql"
def resourcePath = "src/main/resources"
def remoteSchemaUrl = "https://raw.githubusercontent.com/nationalarchives/tdr-consignment-api/master/schema.graphql"

pipeline {
  agent {
    label "master"
  }

  parameters {
    choice(name: "STAGE", choices: ["intg", "staging", "prod"], description: "The stage you are deploying the schema to")
  }
  stages {
    stage("Run git secrets") {
      steps {
        script {
          tdr.runGitSecrets(repo)
        }
      }
    }
    stage("Create version bump branch") {
      steps {
        script {
          tdr.configureJenkinsGitUser()
        }
        sh "git checkout -b ${versionBumpBranch}"
        script {
          tdr.pushGitHubBranch(versionBumpBranch)
        }
      }
    }
    stage("Deployment") {
      stages {
        stage("Deploy to npm") {
          agent {
            ecs {
              inheritFrom "npm"
            }
          }
          stages {
            stage ("Checkout and track version bump branch") {
              steps {
                script {
                  tdr.configureJenkinsGitUser()
                }
                sshagent(['github-jenkins']) {
                  sh "git checkout -b ${versionBumpBranch} --track origin/${versionBumpBranch}"
                }
              }
            }
            stage("Update npm version") {
              steps {
                sh "mkdir -p ${resourcePath}"
                sh "wget -O ${resourcePath}/schema.graphql ${remoteSchemaUrl}"
                dir("ts") {
                  sh 'npm ci'
                  sh 'npm run codegen'
                  sh 'npm run build'

                  sshagent(['github-jenkins']) {
                    sh "npm version patch"
                    //npm version will not commit version updates if the package.json is in a different directory to .git directory
                    //Open PR to add this feature to npm version: https://github.com/npm/cli/pull/1557
                    //In the interim include git commands to commit the changes to the branch
                    sh "git add package.json package-lock.json"
                    sh "git commit -m 'Update npm version'"
                  }

                  withCredentials([string(credentialsId: 'npm-login', variable: 'LOGIN_TOKEN')]) {
                    sh "npm config set //registry.npmjs.org/:_authToken=$LOGIN_TOKEN"
                    sh 'npm publish --access public'
                  }
                }
              }
            }
            stage("Commit npm version bump changes to origin branch") {
              steps {
                script {
                  tdr.configureJenkinsGitUser()
                  tdr.pushGitHubBranch(versionBumpBranch)
                }
              }
            }
          }
        }
        stage("Deploy to s3") {
          agent {
            ecs {
              inheritFrom "base"
              taskDefinitionOverride "arn:aws:ecs:eu-west-2:${env.MANAGEMENT_ACCOUNT}:task-definition/sbtwithpostgres"
            }
          }
          stages {
            stage("Checkout and track branch version bump branch") {
              steps {
                script {
                  tdr.configureJenkinsGitUser()
                }
                sshagent(['github-jenkins']) {
                  sh "git checkout -b ${versionBumpBranch} --track origin/${versionBumpBranch}"
                }
              }
            }
            stage("Update sbt release") {
              steps {
                sh "mkdir -p ${resourcePath}"
                sh "wget -O ${resourcePath}/schema.graphql ${remoteSchemaUrl}"

                //commits to origin branch
                sshagent(['github-jenkins']) {
                  sh "sbt 'release with-defaults'"
                }

                script {
                  tdr.postToDaTdrSlackChannel(colour: "good", message: "*GraphQL schema* :arrow_up: The generated GraphQL schema has been published")
                }
              }
            }
          }
        }
      }
    }
    stage("Create version bump pull request") {
      steps {
        script {
          tdr.createGitHubPullRequest(
            pullRequestTitle: "Version Bump from build number ${BUILD_NUMBER}",
            buildUrl: env.BUILD_URL,
            repo: "tdr-generated-graphql",
            branchToMergeTo: "master",
            branchToMerge: versionBumpBranch
          )
        }
      }
    }
  }
}
