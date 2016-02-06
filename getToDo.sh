#!/bin/bash


if [[ $# -ne 1 ]]; then
	echo ERROR: Missing project root 1>&2
    exit 1 # terminate and indicate error
fi

files=$(find $1 -name '*.java')
#regex='^public[[:space:]]+(abstract[[:space:]]+)?(enum|class|interface)[[:space:]]+[aA-zZ]+[[:space:]]*(extends[[:space:]]+[aA-zZ]+[[:space:]]*)?(implements[[:space:]]+[aA-zZ]+[[:space:]]*)?{'
regex='(public|private|protected)[[:space:]]+(void|[aA-zZ]+)[[:space:]]+[[:alnum:]]+[[:space:]]*\('
IFS=$'\n'

for file in $files
do	
    classname=$(echo $(basename $file) | cut -d '.' -f 1)
	for line in $(tail -r $file | grep -n "// TODO")
	do
		content=$(echo $line | cut -d ':' -f 2)
		nbLine=$(echo $line | cut -d ':' -f 1)
		method=$(tail -r $file | tail -n +$nbLine | grep -m 1 -E $regex | sed -e 's/^[[:space:]]*//' | cut -d ' ' -f 3 | cut -d '(' -f 1)
		if [[ $line != *"Auto-generated"* ]]
		then
			echo `echo $content | sed -e 's/^[[:space:]]*//' ` in $classname $method >> toDo2Trello.t2t
		fi
	done
done

	
