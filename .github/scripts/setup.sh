#!/bin/bash
git config --global user.email digitalpreservation@nationalarchives.gov.uk
git config --global user.name tna-digital-archiving-jenkins
git checkout -b $BRANCH_NAME
git push -u origin $BRANCH_NAME
mkdir -p src/main/resources
wget -O src/main/resources/schema.graphql https://raw.githubusercontent.com/nationalarchives/tdr-consignment-api/master/schema.graphql
