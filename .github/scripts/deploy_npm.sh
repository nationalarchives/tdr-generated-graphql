#!/bin/bash
set -e
npm config set //registry.npmjs.org/:_authToken=$1
cd ts
npm ci
npm run codegen
npm run build
npm version patch
git add package.json package-lock.json
git commit -m 'Update npm version'
git push
echo set-npm-version=$(awk '/version/{gsub(/("|",)/,"",$2);print $2}' package.json) >> $GITHUB_OUTPUT
npm publish --access public
cd ..
