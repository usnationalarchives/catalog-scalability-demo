# for running locally source the AWS configurations.
# this file is found only locally
# 
# author: Matthew Mariano
# 
args=($@)
if [ -e ~/bin/setAwsEnvironmentVariables.sh ]
then
. ~/bin/setAwsEnvironmentVariables.sh dasnacdev
fi
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
rm -r jars/*
cp $appdir/$appjar jars/
echo jars listing:
ls jars
cd jars
jar -xf $appjar
rm $appjar
cd ..
export cp=`perl ../bin/make-library-classpath.pl $libDir`
echo classpath=$cp
#. ./export-classpath.sh
echo RUNNING LOCAL API
sleep 1
if [ "" != "$classesDir" ]
then
	cp="$classesDir:$cp"
fi
cp="jars:$appdir/$appjar:myconfig:$cp"
if [ -e "export-override.sh" ]
then
	source ./export-override.sh
fi
if [ "" != "$otherClasspathEntries" ]
then
	cp="$cp:$otherClasspathEntries"
else
	echo "no additional classpath entries"	
fi

echo java -cp "$cp" -Dspring.config.location=./myconfig $mainClass
java -cp "$cp" -Dspring.config.location=./myconfig $mainClass