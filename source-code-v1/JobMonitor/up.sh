
. ../bin/exports.sh
.  ./local-exports.sh
args=($@)
function _usage(){
	echo "upload files to aws"
	echo "usage upload <999> "
	echo "where <999> is 1 for api1 or 2 for api2"
}
i=${args[0]}
option=${args[1]}
found=""
if [ "1" = "$i" ]
then
	echo "specified server api1"
	found="found"
elif [ "2" = "$i" ]
then
	echo "specified server api2"
	found="found"
else
	_usage
fi
if [ "found" = "$found" ]
then
	serverNameVariable="api"$i
	eval server=\$$serverNameVariable
	echo "scp -i ~/.aws/dasnacdev.pem myconfig/application.properties $user@$server:$serverFolder/myconfig"
	echo "scp -i ~/.aws/dasnacdev.pem local-exports.sh $user@$server:$serverFolder"
	echo "scp -i ~/.aws/dasnacdev.pem target/$appjar $user@$server:$serverFolder/target"
	echo option=$option
	if [ "$option" = "p" ]
	then
		echo 
	else
		echo EXECUTING COMMANDS
		scp -i ~/.aws/dasnacdev.pem myconfig/application.properties $user@$server:$serverFolder/myconfig
		scp -i ~/.aws/dasnacdev.pem local-exports.sh $user@$server:$serverFolder
		scp -i ~/.aws/dasnacdev.pem target/$appjar $user@$server:$serverFolder/target
	fi

else
	echo "execution failed"
fi