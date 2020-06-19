library("tdr-jenkinslib")

def npmVersionBumpBranch = "npm-version-bump-${BUILD_NUMBER}"
def sbtVersionBumpBranch = "sbt-version-bump-${BUILD_NUMBER}"

pipeline {
  agent none
  parameters {
    choice(name: "STAGE", choices: ["intg", "staging", "prod"], description: "The stage you are deploying the schema to")
    text(name: "SCHEMA", defaultValue: "")
  }
  stages {
    stage("Deployment") {
      parallel {
        stage("Deploy to npm") {
          agent {
            ecs {
              inheritFrom "npm"
            }
          }
          stages {
            stage("Create npm version bump GitHub branch") {
              steps {
                createUpdateBranch(npmVersionBumpBranch)
              }
            }
            stage("Update npm version") {
              steps {
                sh "mkdir -p src/main/resources"
                sh "echo '${params.SCHEMA.trim()}' > src/main/resources/schema.graphql"
                dir("ts") {
                  sh 'npm ci'
                  sh 'npm run codegen'
                  sh 'npm run build'

                  //commits to local branch
                  sshagent(['github-jenkins']) {
                    sh "npm version patch"
                  }

                  withCredentials([string(credentialsId: 'npm-login', variable: 'LOGIN_TOKEN')]) {
                    sh "npm config set //registry.npmjs.org/:_authToken=$LOGIN_TOKEN"
                    sh 'npm publish --access public'
                  }
                }
              }
            }
            stage("Push npm version bump GitHub branch") {
              steps {
                script {
                  tdr.pushGitHubBranch(npmVersionBumpBranch)
                }
              }
            }
          }
        }
        stage("Deploy to s3") {
          agent {
            ecs {
              inheritFrom "base"
              taskDefinitionOverride "arn:aws:ecs:eu-west-2:${env.MANAGEMENT_ACCOUNT}:task-definition/s3publish-${params.STAGE}:2"
            }
          }
          stages {
            stage("Create sbt version bump GitHub branch") {
              steps {
                createUpdateBranch(sbtVersionBumpBranch)
                script {
                  tdr.pushGitHubBranch(sbtVersionBumpBranch)
                }
              }
            }
            stage("Update sbt release") {
              steps {
                sh "mkdir -p src/main/resources"
                sh "echo '${params.SCHEMA.trim()}' > src/main/resources/schema.graphql"

                //commits to origin branch
                  sshagent(['github-jenkins']) {
                    sh "sbt 'release with-defaults'"
                  }

                  slackSend color: "good", message: "*GraphQL schema* :arrow_up: The generated GraphQL schema has been published", channel: "#tdr-releases"
                }
              }
            }
          }
        }
      }
      stage("Create pull requests") {
        agent {
          label "master"
        }
        stages {
          stage("Create npm version bump pull request") {
            steps {
              createGitHubPullRequest("Npm", npmVersionBumpBranch)
            }
          }
          stage("Create sbt version bump pull request") {
            steps {
              createGitHubPullRequest("Sbt", sbtVersionBumpBranch)
            }
          }
        }
      }
    }
}

def createUpdateBranch(String branch) {
  script {
    tdr.configureJenkinsGitUser()
  }
  sh "git checkout -b ${branch}"
}

def createPullRequest(String buildType, String branch) {
  script {
    tdr.createGitHubPullRequest(
      pullRequestTitle: "${buildType} Version Bump from build number ${BUILD_NUMBER}",
      buildUrl: env.BUILD_URL,
      repo: "tdr-generated-graphql",
      branchToMergeTo: "master",
      branchToMerge: branch
    )
  }
}
