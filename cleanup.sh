#!/bin/bash

# Dynamically define the base directory path as the current working directory
BASE_DIR=$(pwd)

# Navigate to the base directory
cd "$BASE_DIR" || { echo "Could not change to base directory. Exiting."; exit 1; }

# Function to print in red
print_red() {
    echo -e "\033[31m$1\033[0m"
}

# Find all .class files in the base directory and its subdirectories
# and delete them.
find . -name "*.class" -print0 | while IFS= read -r -d $'\0' file; do
    echo "Deleting $file"
    rm "$file"
done

echo "Cleanup complete."

