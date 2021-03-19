
def python = python.isEmpty() ? [] : python.split(",")
def nodejs = nodejs.isEmpty() ? [] : nodejs.split(",")
def java = java.isEmpty() ? [] : java.split(",")
def Cpp = Cpp.isEmpty() ? [] : Cpp.split(",")
def csharp = csharp.isEmpty() ? [] : csharp.split(",")
def go = go.isEmpty() ? [] : go.split(",")
def php = php.isEmpty() ? [] : php.split(",")





def subProduct = SubProduct
def branchName = Branch
def jobName = JOB_NAME




def osReposMaps = [
        "dev": [
                "Linux": ["cas-xxx"
                ]
        ]

]

repos = [

        "cas-xxx": [
                "type": "GIT",
                "branch": branchName,
                "subDir": "${branchName}/cas-xxx",
                "subModules": [],
                "url": "git@xxx.github.xxx.com:Foundation-CAS/cas-xxx.git",
                "credential": "tmcas-selfci-ssh-key"
        ],
		[]

]



echo(python.toString())
echo(nodejs.toString())
echo(java.toString())
echo(go.toString())
echo(Cpp.toString())
echo(csharp.toString())
echo(php.toString())

def nodeName(osType){
	if(osType == 'Linux'){
		return 'CDC-CAS-CodeScan-Linux-02'
	}
	if(osType == 'Win'){
		return 'CDC-CAS-CodeScan-Win-01'
	}

}


def targetOsReposMaps = [:] as Map
allrepos = [] as List


timestamps {
    stage("node-repos assignment") {
        if (currentBuild.currentResult != "SUCCESS") {
            echo("The previous stage failed. Skip the current stage.")
            return
        }
        targetOsReposMaps[nodeName('Linux')] = ['base']
        if(python.size() > 0){
            python.each {
            targetOsReposMaps[nodeName('Linux')].add(it)
            allrepos.add(it)
		        }
        }
        if(nodejs.size() > 0){
            nodejs.each {
            targetOsReposMaps[nodeName('Linux')].add(it)
            allrepos.add(it)
            }
        }
        if(java.size() > 0){
            java.each {
            targetOsReposMaps[nodeName('Linux')].add(it)
            allrepos.add(it)
            }
        }
        if(Cpp.size() > 0){
            Cpp.each {
            targetOsReposMaps[nodeName('Linux')].add(it)
            allrepos.add(it)
            }
        }
        if(csharp.size() > 0){
            csharp.each {
            targetOsReposMaps[nodeName('Linux')].add(it)
            allrepos.add(it)
            }
        }
        if(php.size() > 0){
            php.each {
            targetOsReposMaps[nodeName('Linux')].add(it)
            allrepos.add(it)
            }
        }
        if(go.size() > 0){
            go.each {
            targetOsReposMaps[nodeName('Linux')].add(it)
            allrepos.add(it)
            }
        }

		echo(targetOsReposMaps[nodeName('Linux')].toString())
    }
}

allrepostring = allrepos.join(',')


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
		sh(script: "python /home/jenkinsbuild/fortify_codescan/workspace/TMCAS/CAS-codescan/cas-blackduck/dev/cas-codescan/blackduck_codescan/bin/blackduck_codescan.py -d ${allrepostring}")
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
    def subject = "[CodeScan]CAS BlackDuck Scan results upladed: ${buildStatus} "
    def mail_body = '''
        <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <body>
        <div id="container">
        <p><strong>BlackDuck CodeScan results has uploaded:</strong></p>
        <div id="content">
        <p>This scan includes the following repo(s):</p>
        <p>
        python : ${python} <br />
        nodejs : ${nodejs} <br />
        java : ${java} <br />
        Cpp : ${Cpp} <br />
        csharp : ${csharp} <br />
        php : ${php} <br />
        go : ${go} <br />
        </p>


        <p><strong>BlackDuck Service page : https://blackduck.xxx.com/ </strong></p>

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
            to: 'Developers@xxx.com,op@xx.xxx.com'
    )
}

stage("send notification"){
    catchError {
        sendMailNotifcations(currentBuild.currentResult)
    }
}
