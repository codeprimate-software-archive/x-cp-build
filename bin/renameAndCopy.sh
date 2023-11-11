#!/bin/bash

alias ls=`ls`

presentWorkingDirectory=`pwd`
newFile=""

function renameFile {
  filename=$1
  newFile=${filename//'|'/''}
  return 0
}

fromDirectory=$1
#fromDirectory=${fromDirectory//' '/'\ '}
filter=$2
toDirectory=$3

echo Finding $filter files in directory ${fromDirectory}...

cd "${fromDirectory}"
newWorkingDirectory=`pwd`

echo "present working directory - "${newWorkingDirectory}

for file in $(ls -1 $filter); do
  echo "" # newline
  echo "filename - "$file
  file=`basename $file`
  echo "base filename - "$file
  renameFile $file
  echo "renamed file - "$newFile
  echo Moving file $file to ${toDirectory}${newFile}
  cp $file ${toDirectory}${newFile}
done

cd ${presentWorkingDirectory}
 
