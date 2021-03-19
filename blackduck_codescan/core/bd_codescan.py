import os,sys
BASEDIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(BASEDIR)

import subprocess
import platform
import time
#from core.threadprocess import ThreadPoolManager
from conf.settings import blackduck_info
from conf.version_mapping import language_mapping

class CodeScan(object):
    def __init__(self,repo_list):
        self.repo_list = repo_list
        self.os_type = platform.system()
        self.path_manage()

    def path_manage(self):
        self.synopsys_jar_path = os.path.join(BASEDIR,'detect_scripts/synopsys-detect-6.7.0.jar')
        if self.os_type == 'Linux':
            self.java_path = '/usr/java/jdk1.8.0_102/bin'
            self.main_path = '/home/jenkinsbuild/codescan/workspace/XXXTEAM/xx-xxx/xx-blackduck/dev/'
        elif self.os_type == 'Windows':
            self.java_path = 'C:/Program Files/Java/jre1.8.0_102/bin'
            self.main_path = 'D:/ci-jenkins/workspace/XXXTEAM/xx-xxx/xx-blackduck/dev/'

    def version_map(self,repo_name):
        for k,v in language_mapping.items():
            if repo_name in v:
                return k

    def run_scan(self):
        print('start run scan')
        url = '--blackduck.url=%s'%blackduck_info['bdServerUrl']
        token = '--blackduck.api.token=%s'%blackduck_info['uploadToken']
        projectname = '--detect.project.name=%s'%blackduck_info['projectName']
        nickname = '--detect.project.version.nickname=%s'%blackduck_info['nickName']
        for repo in self.repo_list:
            code_path = os.path.join(self.main_path,repo)
            reponame = '--detect.project.version.name=%s'%(self.version_map(repo))
            sourcepath = '--detect.source.path=%s'%code_path
            #if self.version_map(repo) in ['nodejs','java']:
            #    self.pkg_install(self.version_map(repo),code_path)
            java_cmd = os.path.join(self.java_path,'java')
            run_scan_cmd = [java_cmd,'-jar',self.synopsys_jar_path,url,token,projectname,nickname,reponame,sourcepath]
            print(run_scan_cmd)
            bd_scan_status = subprocess.call(run_scan_cmd)
            if bd_scan_status == 0:
                print('%s bd scan sucess' % repo)
            else:
                print('%s bd scan fail' % repo)
            time.sleep(60)
