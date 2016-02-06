
if [[ $# -ne 1 ]]; then
	echo ERROR: Missing project root 1>&2
    exit 1 # terminate and indicate error
fi

./getTodo.sh $1
./parse.sh