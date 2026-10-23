#!/bin/bash
set -e
npm config set registry https://registry.npmjs.org/
cd ts
npm ci
npm run codegen
npm run build
npm version patch
git add package.json package-lock.json
git commit -m 'Update npm version'
git push
npm publish --access public
echo set-npm-version=$(awk '/version/{gsub(/("|",)/,"",$2);print $2}' package.json) >> $GITHUB_OUTPUT
cd ..
