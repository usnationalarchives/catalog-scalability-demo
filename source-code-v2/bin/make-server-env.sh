# author: Matthew Mariano
# 
function mkdirIfNotExist(){
	_d="$1"
	if [ -d "$_d" ]
	then
		echo directory already exists: "$_d" 
	else
		mkdir "$_d" 
	fi
}
mkdirIfNotExist bin

mkdirIfNotExist "api"
mkdirIfNotExist "api/myconfig"
mkdirIfNotExist "api/target"
mkdirIfNotExist "api/logs"
mkdirIfNotExist "api/jars"

mkdirIfNotExist "chunk"
mkdirIfNotExist "chunk/myconfig"
mkdirIfNotExist "chunk/target"
mkdirIfNotExist "chunk/logs"
mkdirIfNotExist "chunk/jars"

mkdirIfNotExist "process"
mkdirIfNotExist "process/myconfig"
mkdirIfNotExist "process/target"
mkdirIfNotExist "process/logs"
mkdirIfNotExist "process/jars"

mkdirIfNotExist "monitor"
mkdirIfNotExist "monitor/myconfig"
mkdirIfNotExist "monitor/target"
mkdirIfNotExist "monitor/logs"
mkdirIfNotExist "monitor/jars"