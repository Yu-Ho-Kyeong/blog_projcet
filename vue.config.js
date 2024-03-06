// const { defineConfig } = require('@vue/cli-service')
module.exports = {
  devServer: {
      proxy: {
          '/api': {  // 해당경로로 요청이 들어올 경우 우회하여 요청
              target: 'http://localhost:8081',
          }
      }
  },
  publicPath: "/blog_projcet/",
  outputDir: "./docs"
}