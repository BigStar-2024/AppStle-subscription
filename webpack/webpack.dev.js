const webpack = require('webpack');
const { merge } = require('webpack-merge');
const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const FriendlyErrorsWebpackPlugin = require('@soda/friendly-errors-webpack-plugin');
const SimpleProgressWebpackPlugin = require('simple-progress-webpack-plugin');
const WebpackNotifierPlugin = require('webpack-notifier');
const path = require('path');
const sass = require('sass');

const utils = require('./utils.js');
const commonConfig = require('./webpack.common.js');

const ENV = 'development';

module.exports = (options) => merge(commonConfig({ env: ENV }), {
  devtool: 'cheap-module-source-map', // https://reactjs.org/docs/cross-origin-errors.html
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
    filename: 'app/[name].bundle.js',
    chunkFilename: 'app/[id].chunk.js',
    publicPath: 'http://localhost:9008/'
  },
  module: {
    rules: [
      {
        test: /\.(sa|sc|c)ss$/,
        use: ['style-loader', 'css-loader', 'postcss-loader', {
          loader: 'sass-loader',
          options: { implementation: sass }
        }
        ]
      }
    ]
  },
  devServer: {
    hot: true,
    static: './target/classes/static/',
    proxy: [{
      context: [
        '/api',
        '/services',
        '/management',
        '/swagger-resources',
        '/v2/api-docs',
        '/v3/api-docs',
        '/h2-console',
        '/auth'
      ],
      target: `http${options.tls ? 's' : ''}://localhost:8087`,
      // target: 'https://subscription-admin.appstle.com',
      secure: false,
      changeOrigin: options.tls
    }],
    https: options.tls,
    historyApiFallback: true,
    devMiddleware: {
      stats: options.stats,
      writeToDisk: true
    }
  },
  watchOptions: {
    ignored: ['/node_modules/', 'src/test']
  },
  stats: process.env.JHI_DISABLE_WEBPACK_LOGS ? 'none' : options.stats,
  plugins: [
    process.env.JHI_DISABLE_WEBPACK_LOGS
      ? null
      : new SimpleProgressWebpackPlugin({
        format: options.stats === 'minimal' ? 'compact' : 'expanded'
      }),
    new FriendlyErrorsWebpackPlugin(),
    new BrowserSyncPlugin({
      https: options.tls,
      host: 'localhost',
      port: 9008,
      proxy: {
        target: `http${options.tls ? 's' : ''}://localhost:9067`,
        proxyOptions: {
          changeOrigin: false  //pass the Host header to the backend unchanged  https://github.com/Browsersync/browser-sync/issues/430
        }
      },
      socket: {
        clients: {
          heartbeatTimeout: 60000
        }
      }
    }, {
      reload: false
    }),
    new WebpackNotifierPlugin({
      title: 'JHipster',
      contentImage: path.join(__dirname, 'logo-jhipster.png')
    })
  ].filter(Boolean)
});
