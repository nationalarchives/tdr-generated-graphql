#!/bin/bash
npm config set //registry.npmjs.org/:_authToken=$1
cd ts
npm ci
npm run codegen
npm run build
npm version patch
git add package.json package-lock.json
git commit -m 'Update npm version'
git push
npm publish --access public
cd ..
