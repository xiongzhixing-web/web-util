#!/bin/bash
if [ $# != 2 ]
then
    echo "参数长度必须为2"
    exit
fi

sourPath=$1
tarPath=$2
if [ ! -d $1 ] 
then 
   echo '源目录不存在'
   exit
fi

if [ ! -d $2 ]
then
   mkdir -p $2
fi

cp -r $sourPath $tarPath

shutdownPort=0;
httpPort=0;
ajpPort=0;
i=0;
port=8080
while (($i < 3))
do
  row=`lsof -i:$port | wc -l`
  if [ $row == 0 ]
  then
     if [ $shutdownPort == 0 ]
     then
          shutdownPort=$port
     elif [ $httpPort == 0 ]
     then
          httpPort=$port
     else
          ajpPort=$port
     fi
     let "i++"
  fi
  let "port++"
done

sourDir=${sourPath##*/}
echo $tarPath"/"$sourDir"/conf"


sed -i "s/<Server port=\"[0-9]*\" shutdown=\"SHUTDOWN\">/<Server port=\"$shutdownPort\" shutdown=\"SHUTDOWN\">/g" $tarPath"/"$sourDir"/conf/server.xml"
sed -i "s/<Connector port=\"[0-9]*\" protocol=\"AJP\/1.3\" redirectPort=\"8443\" \/>/<Connector port=\"$ajpPort\" protocol=\"AJP\/1.3\" redirectPort=\"8443\" \/>/g" $tarPath"/"$sourDir"/conf/server.xml"
sed -i "s/<Connector port=\"[0-9]*\" protocol=\"HTTP\/1.1\" connectionTimeout=\"20000\" redirectPort=\"8443\" \/>/<Connector port=\"$httpPort\" protocol=\"HTTP\/1.1\" connectionTimeout=\"20000\" redirectPort=\"8443\" \/>/g" $tarPath"/"$sourDir"/conf/server.xml"


echo "shutdownPort:"${shutdownPort}
echo "httpPort:"${httpPort}
echo "ajpPort:"${ajpPort}










