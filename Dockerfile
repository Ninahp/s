FROM ubuntu 16.0.4

#Define enviroment variables
export AT_RESOLVER_HOST=”deino.at-internal.com”
export AT_RESOLVER_USER=”Ninah” 
export AT_RESOLVER_PASS=”ninah123”
export AT_RESOLVER_REALM=”Sonatype Nexus Repository Manager”

#Add Java certificate
$JAVA_HOME/jre/lib/security

#export Horus
export HORUS_ENV=dev 

CMD ["/bin/bash"]
