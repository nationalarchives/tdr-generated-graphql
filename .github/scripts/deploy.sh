git checkout $BRANCH_NAME
git push -u origin $BRANCH_NAME
mkdir -p src/main/resources
wget -O src/main/resources/schema.graphql https://raw.githubusercontent.com/nationalarchives/tdr-consignment-api/master/schema.graphql
cd ts
npm ci
npm run codegen
npm run build
npm version patch
git add package.json package-lock.json
git commit -m 'Update npm version'
npm publish --access public
cd ..
sbt 'release with-defaults'