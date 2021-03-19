param([string]$d,[switch]$incremental,[switch]$upload)
$json_data = $d|ConvertFrom-Json

Set-Location -Path D:\ci-jenkins
$Token = ''
$SSC_Url = 'https://ssc.xxx.com/ssc'
##cmd set##
$scan_cmd = "sourceanalyzer.exe"
$cli_cmd = "fortifyclient.bat"
##args set##
$buildID_args = ""
$clean_args = "-clean"
$upload_ssc_full_args = "-url $SSC_Url -authtoken $Token uploadFPR -file win.fpr -application 'XXTEAM' -applicationVersion '3.0_win'"


foreach($RoleRepo in $json_data){
    $Role = $RoleRepo.psobject.properties.name
    $Repo = $RoleRepo.psobject.properties.value
    if($Repo -match "cas-scanner"){
        echo "scanner"
        $buildID_args += " -b cas-scanner "
        .\scanner_rebuild.bat
    }
    if($Repo -match "cas-delegator"){
        echo "delegator"
        $buildID_args += " -b cas-delegator "
        .\delegator_rebuild.bat
    }
}


if($incremental){
    $scan_args = "-scan -incremental -mt -f win.fpr"
    $scan_full_args = $buildID_args + $scan_args
    echo "$scan_cmd $scan_full_args"
    Start-Process $scan_cmd -ArgumentList $scan_full_args -Wait
}
else{
    $scan_args = "-scan -incremental-base -mt -f win.fpr"
    $clean_full_args = $buildID_args + $clean_args
    Start-Process $scan_cmd -ArgumentList $clean_full_args -Wait
    $scan_full_args = $buildID_args + $scan_args
    echo "$scan_cmd $scan_full_args"
    Start-Process $scan_cmd -ArgumentList $scan_full_args -Wait
}

if($upload){
    Start-Process $cli_cmd -ArgumentList $upload_ssc_full_args -Wait
}
