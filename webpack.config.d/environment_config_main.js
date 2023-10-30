// Define environment variables here
var webpack = require("webpack");

var definePlugin = new webpack.DefinePlugin(
   {
      "PROCESS_ENV": JSON.stringify(process.env)
   }
)

config.plugins.push(definePlugin)