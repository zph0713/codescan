
def linuxRepos = LinuxRepos.isEmpty() ? [] : LinuxRepos.split(",")
def windowsRepos = WindowsRepos.isEmpty() ? [] : WindowsRepos.split(",")


def subProduct = SubProduct
def branchName = Branch
def jobName = JOB_NAME

def allLinux = AllLinux.toBoolean()
def allWin = AllWindows.toBoolean()


def osReposMaps = [
        "dev": [
                "Linux": ["xxx-xxx",
                ],
                "Win": ["cas-gmscanner","cas-delegator","cas-scanner"]
        ]

]

repos = [

        "base": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-codescan",
                "subModules": [],
                "url": "git@xxx.github.xxx.com:Foundation-CAS/cas-codescan.git",
                "credential": "ssh-key"
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
		sh(script: "python $pa/CAS-codescan/cas-fortify/dev/cas-codescan/fortify_codescan/bin/fortify_codescan.py -d ${linuxRepos}")
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
        <p><strong>Fortify SSC Service : https://ssc.xxx.com/ssc/ </strong></p>
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
            to: ''
    )
}

stage("send notification"){
    catchError {
        sendMailNotifcations(currentBuild.currentResult)
    }
}
