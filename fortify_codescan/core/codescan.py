import os,sys
BASEDIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(BASEDIR)

import subprocess
import json
import shutil
import platform
import time
#from core.threadprocess import ThreadPoolManager

from conf.reporole import roles_os,nonsupport_mapping,nonsupport_filetype



Token = ''


class CodeScan(object):
    def __init__(self,repo_list):
        self.repo_list = repo_list
        self.os_type = platform.system()
        self.path_manage()
        self.exclude_nonsupport_filelist(repo_list)

    def path_manage(self):
        if self.os_type == "Linux":
            self.fortify_path = "/opt/Fortify/Fortify_SCA_and_Apps_20.1.0/"
            self.code_path = "/home/jenkinsbuild/fortify_codescan/workspace/xxteam/CAS-codescan/cas-fortify/dev/"
        if self.os_type == "Windows":
            self.fortify_path = "C:/Program Files/Fortify/Fortify_SCA_and_Apps_20.1.0/"
            self.code_path = u'D:/ci-jenkins/workspace/xxteam/CAS-codescan/cas-fortify/dev/'
        self.cmd_path = os.path.join(self.fortify_path,'bin')
        self.winscripts_path = os.path.join(self.code_path,'cas-codescan/fortify_codescan/winscripts/')

    def exclude_nonsupport_filelist(self,repo_list):
        print('--exclude_nonsupport_filelist--')
        for repo in repo_list:
            if repo in nonsupport_mapping.keys():
                for pth in nonsupport_mapping[repo]:
                    file_path = os.path.join(self.code_path,repo,pth)
                    print(file_path)
                    if os.path.exists(file_path):
                        if os.path.isdir(file_path):
                            print('remove dir : ',file_path)
                            shutil.rmtree(file_path)
                        elif os.path.isfile(file_path):
                            print('remove file : ',file_path)
                            os.remove(file_path)
                        else:
                            print('unknow filetype')
                    else:
                        print('unknow filepath :%s'% file_path)
            else:
                print('%s no exclude'% repo)

    def clean_ids(self):
        print('--clean ids--')
        sourceanalyzer_cmd = os.path.join(self.cmd_path, 'sourceanalyzer')
        clean_cmd = [sourceanalyzer_cmd,'-debug','-clean']
        for repo in self.repo_list:
            clean_cmd.append('-b')
            clean_cmd.append(repo)
        print('--start clear ids %s--' % repo)
        print('clean_cmd: ',clean_cmd)
        clear_previous_scan = subprocess.call(clean_cmd)
        if clear_previous_scan == 0:
            print('clear ids success')
            time.sleep(1)
            print('--start create ids--')
            self.create_ids()
        else:
            print('clean ids fail')

    def create_ids(self):
        print('--create ids--')
        sourceanalyzer_cmd = os.path.join(self.cmd_path, 'sourceanalyzer')
        for repo in self.repo_list:
            build_id_path = os.path.join(self.code_path,repo)
            create_ids_cmd = [sourceanalyzer_cmd,'-debug','-b',repo,build_id_path]
            create_buildid_file = subprocess.call(create_ids_cmd)
            if create_buildid_file == 0:
                print('%s create ids success'% repo)
            else:
                print('%s create ids fail'% repo)

    def run_scan(self):
        print('start run scan')
        sourceanalyzer_cmd = os.path.join(self.cmd_path,'sourceanalyzer')
        windows_repo = ['cas-gmscanner','cas-delegator','cas-scanner']
        for repo in self.repo_list:
            result_path = os.path.join(self.code_path,repo)
            run_scan_cmd = [sourceanalyzer_cmd,'-debug','-scan','-mt','-j','4','-autoheap','-f',result_path+'.fpr','-b',repo]
            if repo not in windows_repo:
                #run_scan_cmd = [sourceanalyzer_cmd,'-scan','-mt','-autoheap','-f',result_path+'.fpr']
                print('scan cmd: ',run_scan_cmd)
                run_scan = subprocess.call(run_scan_cmd)
                if run_scan == 0:
                    print(repo+'_scan sucess')
                    time.sleep(10)
                    self.post_report(repo)
                else:
                    print(self.os_type+'scan fail')
            else:
                if repo == 'cas-delegator':
                    bat_path = os.path.join(self.winscripts_path,'delegator_rebuild.bat')
                elif repo == 'cas-scanner':
                    bat_path = os.path.join(self.winscripts_path,'scanner_rebuild.bat')
                elif repo == 'cas-gmscanner':
                    bat_path = os.path.join(self.winscripts_path,'gmscanner_rebuild.bat')
                run_build_cmd = [bat_path]
                print('build cmd: ',run_build_cmd)
                run_build = subprocess.call(run_build_cmd)
                if run_build == 0:
                    print('build sucess')
                    print('scan cmd: ',run_scan_cmd)
                    run_scan = subprocess.call(run_scan_cmd)
                    if run_scan == 0:
                        print(repo+'_scan sucess')
                        time.sleep(10)
                        self.post_report(repo)
                    else:
                        print(self.os_type+'scan fail')
                else:
                    print('build fail')


    def post_report(self,repo_name):
        print('start post result to ssc')
        #fortifyclient_cmd = os.path.join(self.cmd_path,'fortifyclient')
        ssc_url = ''
        token = Token
        app_name = 'TMCAS'
        fortifyresult = repo_name+'.fpr'
        result_path = os.path.join(self.code_path,fortifyresult)
        if self.os_type == 'Linux':
            os_t = 'linux'
            fortifyclient_cmd = os.path.join(self.cmd_path,'fortifyclient')
        if self.os_type == 'Windows':
            os_t = 'win'
            fortifyclient_cmd = os.path.join(self.cmd_path,'fortifyclient.bat')
        post_cmd = [fortifyclient_cmd,'-debug','-url',ssc_url,'-authtoken',token,'uploadFPR','-file',result_path,'-application',app_name,'-applicationVersion',repo_name]
        print('post_cmd: ',post_cmd)
        post_ssc_servier = subprocess.call(post_cmd)
        if post_ssc_servier == 0:
            print('TMCAS %s scan result upload sucess' % repo_name)
        else:
            print('upload fail')
