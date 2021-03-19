import os,sys
BASEDIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(BASEDIR)

import json
import argparse

from core.codescan import CodeScan

def buildParameter():
    parser = argparse.ArgumentParser(add_help=True, description='codescan')
    exclusive_group = parser.add_mutually_exclusive_group(required=True)
    exclusive_group.add_argument('-d', '--data', nargs='+')
    obj = parser.parse_args()
    if obj.data:
        datalist = list()
        for i in obj.data[0].split(','):
            datalist.append(i)
        CS = CodeScan(datalist)
        CS.clean_ids()
        CS.run_scan()


if __name__ == '__main__':
    buildParameter()
