# author: Matthew Mariano
# 
rm -r api/jars/*
rm -r ProcessingService/jars/*
rm -r ChunkingService/jars/*
rm -r JobMonitor/jars/*
tar -cf all.tar bin api ProcessingService ChunkingService JobMonitor
gzip all.tar
