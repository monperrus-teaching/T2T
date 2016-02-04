#!/bin/bash


if [[ $# -ne 1 ]]; then
	echo ERROR: Missing project root 1>&2
    exit 1 # terminate and indicate error
fi

rm toDo2Trello.t2t


files=$(find $1 -name '*.java')
regex='^public[[:space:]]+(class|interface)[[:space:]]+([aA-zZ])+[[:space:]]+{'
IFS=$'\n'
for file in $files
do	
	
    classname=$(cat test.java | tr -s ' ' | grep -E '^public[[:space:]]+(class|interface)[[:space:]]+([aA-zZ])+[[:space:]]+{' | cut -d ' ' -f 3)
	echo $classname
	for line in $(grep "// TODO" $file)
	do
		echo `echo $line | sed -e 's/^[ \t]*//' | sed -e 's/^[ 	]*//'` $classname >> toDo2Trello.t2t
	done
done

cat toDo2Trello.t2t

	