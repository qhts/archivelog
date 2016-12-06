#!/bin/bash

# 日志压缩脚本
#
# 参数：
# $1 日志根目录
# $2 归档输出文件名
# $3 需要归档的文件距现在的天数

#参数个数！=3中断执行
if [ $# -ne 3 ];then
	echo -e "参数错误"
	exit 1
fi

log_source_dir=$1
log_dest=$2
modifyTime=$3

if [ ! -d "$log_source_dir" ]; then
	echo "目录文件夹不存在"
	exit 1
fi 
cd $log_source_dir
find -maxdepth 2 -mindepth 1 -type f -mtime +$modifyTime -exec zip -m $log_dest {} \; 

