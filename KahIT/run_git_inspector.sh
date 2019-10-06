#!/bin/sh

cd ..
echo "Starting git inspector.."

python git_inspector/gitinspector.py --grading=1 --exclude="git_inspector" --format=html > index.html

echo "Report completed"

# pause