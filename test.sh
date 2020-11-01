#!/bin/bash

while read line
do
    echo $line  
    array=($line)
    echo 'userid:'${array[0]}',orderno:'${array[1]}
        
done < 1.txt




curl  http://www.baidu.com 

