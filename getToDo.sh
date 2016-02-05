#!/bin/bash


if [[ $# -ne 1 ]]; then
	echo ERROR: Missing project root 1>&2
    exit 1 # terminate and indicate error
fi

rm toDo2Trello.t2t


files=$(find $1 -name '*.java')
#regex='^public[[:space:]]+(abstract[[:space:]]+)?(enum|class|interface)[[:space:]]+[aA-zZ]+[[:space:]]*(extends[[:space:]]+[aA-zZ]+[[:space:]]*)?(implements[[:space:]]+[aA-zZ]+[[:space:]]*)?{'
IFS=$'\n'

for file in $files
do	
    classname=$(echo $(basename $file) | cut -d '.' -f 1)

	for line in $(grep "// TODO" $file)
	do
		if [[ $line != *"Auto-generated"* ]]
		then
			echo `echo $line | sed -e 's/^[ \t]*//' | sed -e 's/^[ 	]*//'` in $classname >> toDo2Trello.t2t
		fi
	done
done

cat toDo2Trello.t2t

	
