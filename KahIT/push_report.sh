#!/bin/bash

cd ..
echo "Starting git inspector.."

python git_inspector/gitinspector.py --grading=1 --exclude="git_inspector" --format=html > git_inspector_report.html

echo "Report completed"

pause