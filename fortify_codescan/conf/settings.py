import os,sys
BASEDIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(BASEDIR)

import reporole

CONFDIR = os.path.join(BASEDIR,'conf')
COREDIR = os.path.join(BASEDIR,'core')
BINDIR = os.path.join(BASEDIR,'bin')

Config_file = os.path.join(CONFDIR,'config.ini')



RECEIVERS = ['hamm_zhou@trendmicro.com']
