#!/bin/bash


if [[ $# -ne 1 ]]; then
	echo ERROR: Missing project root 1>&2
    exit 1 # terminate and indicate error
fi

rm toDo2Trello.t2t


files=$(find $1 -name '*.java')
IFS=$'\n'
for file in $files
do	
	for line in $(grep "TODO" $file)
	do
		echo $line | sed -e 's/^[ \t]*//' | sed -e 's/^[ 	]*//' >> toDo2Trello.t2t
	done
done

cat toDo2Trello.t2t

	