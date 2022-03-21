import org.yaml.snakeyaml.Yaml

def parseJobList(def yamlText) {
    return new Yaml().load(yamlText)
}

node('master') {
    stage('Checkout') {
        cleanWs()
        checkout scm
    }

    stage('Seed') {
        def jobsList = parseJobList(readFile("aws/jobs.yaml"))

        credentialsId="deploy-key-shared-library"

        jobsList.jobs.each{ job ->
            createJobs("${job.repository}", "${job.jobDSLRevision}", "${credentialsId}",  "${job.jobDSLPath}", "${job.removedJobAction}")
        }
    }
}
