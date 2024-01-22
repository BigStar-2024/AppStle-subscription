const webpack = require('webpack');
const { merge } = require('webpack-merge');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const WorkboxPlugin = require('workbox-webpack-plugin');
const MomentLocalesPlugin = require('moment-locales-webpack-plugin');
const sass = require('sass');

const utils = require('./utils.js');
const commonConfig = require('./webpack.common.js');

const ENV = 'production';

module.exports = merge(commonConfig({ env: ENV }), {
  //devtool: 'source-map', // Enable source maps. Please note that this will slow down the build
  mode: ENV,
  entry: {
    main: './src/main/webapp/app/index',
    backend: './src/main/webapp/app/backend',
    customer: './src/main/webapp/app/customer',
    'customer-v3': './src/main/webapp/app/customer-v3',
    bundles: './src/main/webapp/app/bundles',
    'bundles-v2': './src/main/webapp/app/bundles-v2',
    'bundles-v3': './src/main/webapp/app/bundles-v3',
    'appstle-menu': './src/main/webapp/app/appstle-menu'
  },
  output: {
    path: utils.root('target/classes/static/'),
    filename: 'app/[name].bundle.js?v=[fullhash]',
    chunkFilename: 'app/[name].chunk.js?v=[chunkhash]',
    publicPath: 'https://subscription-admin.appstle.com/'
  },
  module: {
    rules: [
      {
        test: /\.(sa|sc|c)ss$/,
        use: [
          {
            loader: MiniCssExtractPlugin.loader,
            options: {
              publicPath: '../'
            }
          },
          'css-loader',
          'postcss-loader',
          {
            loader: 'sass-loader',
            options: { implementation: sass }
          }
        ]
      }
    ]
  },
  plugins: [
    new MiniCssExtractPlugin({
      // Options similar to the same options in webpackOptions.output
      filename: 'content/[name].css?v=[fullhash]',
      chunkFilename: 'content/[name].css?v=[chunkhash]',
    }),
    new MomentLocalesPlugin({
      localesToKeep: [
        'en'
        // jhipster-needle-i18n-language-moment-webpack - JHipster will add/remove languages in this array
      ]
    }),
    new webpack.LoaderOptionsPlugin({
      minimize: false,
      debug: true
    }),
    new WorkboxPlugin.GenerateSW({
      clientsClaim: true,
      skipWaiting: true,
      exclude: [/swagger-ui/]
    })
  ]
});
