#!/bin/bash

 # chmod +x jenkins-mac.sh && ./jenkins-mac.sh
 # 
#brew uninstall jenkins
#delete data 
#sudo rm -rf /var/lib/jenkins
brew install jenkins && brew services start jenkins && open http://localhost:8080
