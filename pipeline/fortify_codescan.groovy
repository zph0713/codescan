
def linuxRepos = LinuxRepos.isEmpty() ? [] : LinuxRepos.split(",")
def windowsRepos = WindowsRepos.isEmpty() ? [] : WindowsRepos.split(",")


def subProduct = SubProduct
def branchName = Branch
def jobName = JOB_NAME

def allLinux = AllLinux.toBoolean()
def allWin = AllWindows.toBoolean()


def osReposMaps = [
        "dev": [
                "Linux": ["cas-gmail", "cas-storage", "cas-ui", "cas-samlsso", "cas-auth",
                                             "cas-urlt0query", "cas-jaguar-delegator", "cas-phishingdetect", "cas-config",
                                             "cas-hybridsmex", "cas-emailsender", "cas-crypto", "cas-o365graph", "cas-policy",
                                             "cas-policymatch", "cas-controller", "cas-sglogcollector", "cas-license",
                                             "cas-integrationapi", "cas-account", "cas-node-log-client", "cas-client-sdk-js",
                                             "cas-prophet", "cas-admin", "cas-asmsrvapi", "cas-ops-toolkit","cas-stosubscription",
                ],
                "Win": ["cas-gmscanner","cas-delegator","cas-scanner","cas-chat"]
        ]

]

repos = [

        "cas-chat": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-chat",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-chat.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-gmail": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-gmail",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-gmail.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-L10n": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-L10n",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-L10n.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-3rd-party": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-3rd-party",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-3rd-party.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-config": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-config",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-config.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-ops-toolkit": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-ops-toolkit",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-ops-toolkit.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-gmscanner": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-gmscanner",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-gmscanner.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "base": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-codescan",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-codescan.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-storage": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-storage",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-storage.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-ui": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-ui",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-ui.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-samlsso": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-samlsso",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-samlsso.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-auth": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-auth",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-auth.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-phishingdetect": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-phishingdetect",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-phishingdetect.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-urlt0query": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-urlt0query",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-urlt0query.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-jaguar-delegator": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-jaguar-delegator",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-jaguar-delegator.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-hybridsmex": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-hybridsmex",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-hybridsmex.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-emailsender": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-emailsender",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-emailsender.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-crypto": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-crypto",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-crypto.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-o365graph": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-o365graph",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-o365graph.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-policy": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-policy",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-policy.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-policymatch": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-policymatch",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-policymatch.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-controller": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-controller",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-controller.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-sglogcollector": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-sglogcollector",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-sglogcollector.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-license": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-license",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-license.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-scanner": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-scanner",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-scanner.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-delegator": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-delegator",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-delegator.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-prophet": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-prophet",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-prophet.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-integrationapi": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-integrationapi",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-integrationapi.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-account": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-account",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-account.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-admin": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-admin",
                "subModules": [],
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-admin.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-client-sdk-js": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-client-sdk-js",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-client-sdk-js.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-node-log-client": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-node-log-client",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-node-log-client.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-asmsrvapi": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-asmsrvapi",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-asmsrvapi.git",
                "credential": "tmcas-selfci-ssh-key"
        ],

        "cas-stosubscription": [
                "type": "GIT",
                "branch": "${branchName}",
                "subDir": "${branchName}/cas-stosubscription",
                "url": "git@adc.github.trendmicro.com:Foundation-CAS/cas-stosubscription.git",
                "credential": "tmcas-selfci-ssh-key"
        ]
]



echo(linuxRepos.toString())
echo(windowsRepos.toString())

def nodeName(osType){
	if(osType == 'Linux'){
		return 'CDC-CAS-CodeScan-Linux-02'
	}
	if(osType == 'Win'){
		return 'CDC-CAS-CodeScan-Win-01'
	}

}


def targetOsReposMaps = [:] as Map

timestamps {
    stage("node-repos assignment") {
        if (currentBuild.currentResult != "SUCCESS") {
            echo("The previous stage failed. Skip the current stage.")
            return
        }
        if(linuxRepos.size() > 0){
            targetOsReposMaps[nodeName('Linux')] = ['base']
            linuxRepos.each {
            targetOsReposMaps[nodeName('Linux')].add(it)
		    }
        }
        if(windowsRepos.size() > 0){
            targetOsReposMaps[nodeName('Win')] = ['base']
            windowsRepos.each {
            targetOsReposMaps[nodeName('Win')].add(it)
            }
        }

		echo(targetOsReposMaps.toString())
    }
}


def generateTask(nodeName,targetOsReposMaps,branchName){
    return {
        node(nodeName){
            stage("checkout on node ${nodeName}") {
                if (currentBuild.currentResult != "SUCCESS") {
                    echo("The previous stage failed. Skip the current stage.")
                    return
                }
                catchError {
                    targetOsReposMaps.get(nodeName).each{
                        String repoName = it
                        Map repo = repos.get(repoName)
                        if (repo['type'] == "GIT") {
                            echo("the repo is GIT , the name is ${repoName}")
                            checkout([$class: 'GitSCM', branches: [[name: repo['branch']]],
                                      doGenerateSubmoduleConfigurations: false,
                                      extensions: [[$class: 'CloneOption', depth: 0, noTags: true, shallow: true],
                                                   [$class: 'RelativeTargetDirectory', relativeTargetDir: repo['subDir']]],
                                      submoduleCfg: [],
                                      userRemoteConfigs: [[credentialsId: repo['credential'], url: repo['url']]]])
                        }
                    }
                }
            }
        }
    }
}

timestamps {
    stage("sync code") {
        echo("processing build in parallel..")
        if (currentBuild.currentResult != "SUCCESS") {
            echo("The previous stage failed. Skip the current stage.")
            return
        }
        pipelineTasks = [:]
        targetOsReposMaps.each {
            pipelineTasks[it.key] = generateTask(it.key as String, targetOsReposMaps,branchName)
        }
        parallel pipelineTasks
    }
}



def ostypeCMD(nodeName){
	if(nodeName == 'CDC-CAS-CodeScan-Linux-02'){
		sh(script: "python /home/jenkinsbuild/fortify_codescan/workspace/TMCAS/CAS-codescan/cas-fortify/dev/cas-codescan/fortify_codescan/bin/fortify_codescan.py -d ${linuxRepos}")
	}
	if(nodeName == 'CDC-CAS-CodeScan-Win-01'){
		bat(script: "python d:/ci-jenkins/workspace/TMCAS/CAS-codescan/cas-fortify/dev/cas-codescan/fortify_codescan/bin/fortify_codescan.py -d ${windowsRepos}")
	}
}

def scancodeTask(nodeName,targetOsReposMaps,branchName){
    return {
        node(nodeName){
            stage("scan code on node ${nodeName}") {
                if (currentBuild.currentResult != "SUCCESS") {
                    echo("The previous stage failed. Skip the current stage.")
                    return
                }
                catchError {
                    repolist = []
					targetOsReposMaps.get(nodeName).each{
						repolist.add(it)
					}
					def code_scan = ostypeCMD(nodeName)
				}
			}
		}
	}
}

timestamps {
    stage("scan code") {
        echo("processing scan in parallel..")
        if (currentBuild.currentResult != "SUCCESS") {
            echo("The previous stage failed. Skip the current stage.")
            return
        }
        pipelineTasks = [:]
        targetOsReposMaps.each {
            pipelineTasks[it.key] = scancodeTask(it.key as String, targetOsReposMaps,branchName)
        }
        parallel pipelineTasks
    }
}


def sendMailNotifcations(String buildStatus = 'STARTED') {
    // build status of null means successful
    buildStatus = buildStatus ?: 'SUCCESS'

    // Default values
    def subject = "[CodeScan]CAS Fortify Scan results upladed: ${buildStatus} "
    def mail_body = '''
        <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <body>
        <div id="container">
        <p><strong>Fortify CodeScan results has uploaded:</strong></p>
        <div id="content">
        <p>This scan includes the following repo(s):</p>
        <p>${linuxRepos} </p>
        <p>${windowsRepos} </p>
        <p><strong>Fortify SSC Service : https://codescan-ssc.trendmicro.com/ssc/ </strong></p>
        <p>Use your Trend Domain account to logon to the SSC page with DUO verification and check your repos scan status </p>

        </div>
        </div>
        </div>
        </body>
        </html>
    '''

    // Send email to user who has started the build
    emailext(
            mimeType: 'text/html',
            subject: subject,
            body:  mail_body,
            attachLog: false,
            compressLog: false,
            recipientProviders: [[$class: 'RequesterRecipientProvider']],
            to: 'AllofTrendOffice365OpsTeam@dl.trendmicro.com,AllofTrendCNOffice365SecurityQA@dl.trendmicro.com,AllofTrendCNOffice365SecurityDevelopers@dl.trendmicro.com'
    )
}

stage("send notification"){
    catchError {
        sendMailNotifcations(currentBuild.currentResult)
    }
}
