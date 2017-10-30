# for running locally source the AWS configurations.
# this file is found only locally
# 
args=($@)
. ~/bin/setAwsEnvironmentVariables.sh dasnacdev
# source the main exports for common vars like api1url etc
. ~/bin/exports.sh
#source the local exports for project specific vars
. ./local-exports.sh
dir=`pwd`
if [ -d "jars" ]
then
	echo found jars folder
else
	echo jars folder not found. making it.
	mkdir jars
fi
cp $appdir/$appjar jars/
echo jars listing:
ls jars
cd jars
jar -xf $appjar
cd ..
export cp=`perl ../bin/make-library-classpath.pl $libDir`
echo classpath=$cp
#. ./export-classpath.sh
export INGEST_QUEUE_URL=https://sqs.us-east-1.amazonaws.com/147760863049/ref-app-processing
export CHUNK_QUEUE_URL=https://sqs.us-east-1.amazonaws.com/147760863049/ref-app-chunking
export es_server_url=https://search-naradas-bopsowd64ievka3tnzxfjxysnm.us-east-1.es.amazonaws.com
echo RUNNING LOCAL API
sleep 1
if [ "" != "$classesDir" ]
then
	cp="$classesDir:$cp"
fi
cp="$appdir/$appjar:myconfig:$cp"
if [ -e "export-override.sh" ]
then
	source export-override.sh
fi
echo java -cp "$cp" -Dspring.config.location=./myconfig $mainClass
java -cp "$cp" -Dspring.config.location=./myconfig $mainClass