#!/bin/bash

# Dynamically define the base directory path as the current working directory
BASE_DIR=$(pwd)

# Navigate to the base directory
cd "$BASE_DIR" || { echo "Could not change to base directory. Exiting."; exit 1; }

# Function to print in red
print_red() {
    echo -e "\033[31m$1\033[0m"
}

# Find all .java files in the base directory and its subdirectories
# and compile them, printing errors in red if any.
find . -name "*.java" > java_files_to_compile.txt

while IFS= read -r file; do
    echo "Compiling $file"
    # Compile and capture both stdout and stderr
    output=$(javac "$file" 2>&1)
    exit_code=$?
    if [ $exit_code -ne 0 ]; then
        # If javac exits with a non-zero exit code, print the output in red
        print_red "$output"
    fi
done < java_files_to_compile.txt

# Cleanup
rm java_files_to_compile.txt

echo "Compilation process complete."