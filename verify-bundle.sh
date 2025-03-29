#!/bin/bash

echo "Verifying Git bundle..."
git bundle verify file-indexer.bundle

if [ $? -eq 0 ]; then
    echo "Bundle verification successful!"
    echo "To clone this repository:"
    echo "git clone file-indexer.bundle file-indexer"
    echo
    echo "Or to pull into an existing repository:"
    echo "git pull file-indexer.bundle master"
else
    echo "Bundle verification failed!"
fi
