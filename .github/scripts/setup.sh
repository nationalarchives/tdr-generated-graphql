#!/bin/bash
git config --global 181243999+tna-da-bot@users.noreply.github.com
git config --global user.name tna-da-bot
git checkout -b $BRANCH_NAME
git push -u origin $BRANCH_NAME
mkdir -p src/main/resources
wget -O src/main/resources/schema.graphql https://raw.githubusercontent.com/nationalarchives/tdr-consignment-api/master/schema.graphql
