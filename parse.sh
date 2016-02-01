#!/bin/bash
IFS=$'\n'

for line in $(cat $1)
do
	name=$(echo $line | cut -d " " -f 4)
	task=$(echo $line | cut -d "\"" -f 2)
	date=$(echo $line | awk -F"for " '{print $2}')

	echo "$name;$task;$date" >> toDo2TrelloParsed.t2t
	echo $name has to $task \for $date
done

rm $1