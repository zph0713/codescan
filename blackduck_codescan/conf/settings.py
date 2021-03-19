import os,sys
BASEDIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(BASEDIR)

CONFDIR = os.path.join(BASEDIR,'conf')
COREDIR = os.path.join(BASEDIR,'core')
BINDIR = os.path.join(BASEDIR,'bin')

blackduck_info = {
'uploadToken' : '',
'projectName' : 'Foundation-CAS',
'nickName' : '',
'bdServerUrl':'https://blackduck.xxx.com'
}

RECEIVERS = ['hamm_zhou@trendmicro.com']
