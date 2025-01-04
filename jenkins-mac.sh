#!/bin/bash

 # chmod +x jenkins-mac.sh && ./jenkins-mac.sh
 # delete sudo rm -rf /Users/Shared/Jenkins
brew install jenkins && brew services start jenkins && open http://localhost:8080