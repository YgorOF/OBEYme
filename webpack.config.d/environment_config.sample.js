/**
Follow the following steps to activate environment variables:
    1) Copy this file, and rename the copy it to environment_config.js
    2) Remove the comments on the at line 10 and line 23
    3) Add the a .env file with the environment variables for this project
*/

/** //Uncomment this snippet of code
var webpack = require("webpack")

// Define environment variables here
var path = require('path');
var dotenv = require('dotenv').config({ path: path.resolve(__dirname, '../../../../.env') });

var definePlugin = new webpack.DefinePlugin(
   {
      "PROCESS_ENV": JSON.stringify(dotenv.parsed)
   }
)

*/ //Uncomment this snippet of code