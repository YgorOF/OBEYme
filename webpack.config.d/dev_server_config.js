if(config.devServer !== undefined) {
    config.devServer.historyApiFallback = true
    config.devServer.host = "0.0.0.0"
    config.devServer.disableHostCheck = true
}