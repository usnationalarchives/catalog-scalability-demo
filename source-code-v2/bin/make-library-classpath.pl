# author: Matthew Mariano
# 
my $a=`ls`;
my $dir = "$ARGV[0]";
my $a=`ls $dir/*.jar`;
sub usage{
	print "perl <me> <dirname> \n where <me> is the name of this file and <dirname> is the name of the directory \n containing jar files.";
}
$a =~ s/\n/:/g;
$a=~s/(.*):$/\1/;
print $a;