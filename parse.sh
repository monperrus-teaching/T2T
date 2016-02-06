#!/bin/bash
IFS=$'\n'

for line in $(cat $1)
do
	classname=$(echo $line | awk -F"in " '{print $2}' | cut -d ' ' -f 1)
	method=$(echo $line | awk -F"in " '{print $2}' | cut -d ' ' -f 2)
	name=$(echo $line | cut -d " " -f 4)
	task=$(echo $line | cut -d "\"" -f 2)
	date=$(echo $line | awk -F"for " '{print $2}' | cut -d ' ' -f 1)

	echo "$name;$task;$date;$classname;$method" >> toDo2TrelloParsed.t2t
	echo $name has to $task \for $date into $classname $method
done

rm $1