pipelineJob("jenkins/Jobs-Repository") {
    parameters {
        gitParam('revision') {
            type('BRANCH_TAG')
            sortMode('ASCENDING_SMART')
            defaultValue('origin/master')
        }
        stringParam('jobsFile', 'aws/jobs.yaml', 'The path of the jobDSL files.')
        stringParam('removedJobAction', 'IGNORE', 'Defines how to the jobDSL defined jobs with the existing once the=y are not part og the defined jobDSL. (IGNORE, DISABLE, DELETE)')
    }

    properties {
        pipelineTriggers {
            triggers {
                githubPush()
            }
        }
    }

    logRotator {
        numToKeep(50)
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        github("fr123k/jenkins-jobs", "ssh")
                        credentials("deploy-key-shared-library")
                    }

                    branch('$revision')
                }
            }
            scriptPath('jenkins/aws/jobs.groovy')
        }
    }
}
