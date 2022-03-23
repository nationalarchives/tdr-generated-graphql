const path = require("path");
const fs = require("fs");

module.exports = {
  entry: "./src/index.ts",
  module: {
    rules: [
      {
        test: /\.ts?$/,
        use: "ts-loader",
        exclude: /node_modules/
      }
    ]
  },
  resolve: {
    extensions: [".ts", ".js"]
  },
  output: {
    library: "tdr-generated-graphql",
    libraryTarget: "commonjs-module",
    filename: "index.js",
    path: path.resolve(__dirname, ".")
  }
};
