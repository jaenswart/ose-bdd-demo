def bddJobs = [
    [ name: 'coolstore-app-pipeline', gitUrl: 'http://gogs:3000/gogs/coolstore-app', gitBranch: 'master', openShiftHost: 'kubernetes.default.svc.cluster.local', openShiftPort: "443", openShiftSourceProject: 'coolstore-bdd-dev', openShiftSourceApplication: 'coolstore-app', openShiftDestinationProject: 'coolstore-bdd-prod', openShiftDestinationApplication: 'coolstore-app'],
    [ name: 'coolstore-test-harness-pipeline', gitUrl: 'http://gogs:3000/gogs/coolstore-test-harness.git', gitBranch: 'master', openShiftHost: 'kubernetes.default.svc.cluster.local', openShiftPort: "443", openShiftSourceProject: 'coolstore-bdd-dev', openShiftSourceApplication: 'coolstore-rules', openShiftDestinationProject: 'coolstore-bdd-prod', openShiftDestinationApplication: 'coolstore-rules', kieServer: 'http://coolstore-rules.coolstore-bdd-dev.svc.cluster.local:8080/kie-server/services/rest/server'],
    [ name: 'coolstore-rules-pipeline', gitUrl: 'http://gogs:3000/gogs/coolstore-rules.git', gitBranch: 'deployments', openShiftHost: 'kubernetes.default.svc.cluster.local', openShiftPort: "443", openShiftSourceProject: 'coolstore-bdd-dev', openShiftSourceApplication: 'coolstore-rules', openShiftDestinationProject: 'coolstore-bdd-prod', openShiftDestinationApplication: 'coolstore-rules', kieServer: 'http://coolstore-rules.coolstore-bdd-dev.svc.cluster.local:8080/kie-server/services/rest/server']
]

bddJobs.each { job ->

    workflowJob(job.name) {
        parameters {
            stringParam "OPENSHIFT_HOST",job.openShiftHost,"OpenShift Host"
            stringParam "OPENSHIFT_PORT",job.openShiftPort, "OpenShift Port"
            stringParam "OPENSHIFT_SOURCE_PROJECT",job.openShiftSourceProject, "OpenShift Source Project"
            stringParam "OPENSHIFT_SOURCE_APPLICATION",job.openShiftSourceApplication, "OpenShift Source Application"
            stringParam "OPENSHIFT_DESTINATION_PROJECT",job.openShiftDestinationProject, "OpenShift Destination Project"
            stringParam "OPENSHIFT_DESTINATION_APPLICATION",job.openShiftDestinationApplication, "OpenShift Destination Application"

            if(job.kieServer) {
                stringParam "KIE_SERVER_URL",job.kieServer, "KIE Server URL"
            }

        }

      definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(job.gitUrl)
                    }

                    branch(job.gitBranch) 

                }
            }
            scriptPath "Jenkinsfile"
        }    
      }
    }
}


listView('Coolstore') {

    jobs {
        bddJobs.each { job ->
           name(job.name)
        }
    }

    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

listView("Ops") {

    jobs {
        name("bdd-coolstore-dsl")
    }

    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}