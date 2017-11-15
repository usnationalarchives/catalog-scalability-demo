#!/bin/sh
# author: Matthew Mariano
# 
export api1=ec2-52-54-91-83.compute-1.amazonaws.com
export api2=ec2-34-229-94-12.compute-1.amazonaws.com
export api1url=http://$api1:9000
export INGEST_QUEUE_URL=https://sqs.us-east-1.amazonaws.com/147760863049/ref-app-processing
export CHUNK_QUEUE_URL=https://sqs.us-east-1.amazonaws.com/147760863049/ref-app-chunking
export es_server_url=https://search-naradas-bopsowd64ievka3tnzxfjxysnm.us-east-1.es.amazonaws.com
