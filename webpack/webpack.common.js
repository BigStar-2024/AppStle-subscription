const path = require('path');
const webpack = require('webpack');
const { BaseHrefWebpackPlugin } = require('base-href-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const utils = require('./utils.js');

const getTsLoaderRule = env => {
  const rules = [
    {
      loader: 'thread-loader',
      options: {
        // There should be 1 cpu for the fork-ts-checker-webpack-plugin.
        // The value may need to be adjusted (e.g. to 1) in some CI environments,
        // as cpus() may report more cores than what are available to the build.
        workers: require('os').cpus().length - 1,
      },
    },
    {
      loader: 'ts-loader',
      options: {
        transpileOnly: true,
        happyPackMode: true,
      },
    },
  ];
  if (env === 'development') {
    rules.unshift({
      loader: 'react-hot-loader/webpack',
    });
  }
  return rules;
};

module.exports = options => ({
  cache:
    options.env !== 'production'
      ? {
          type: 'filesystem',
          cacheDirectory: path.resolve('target/cache-loader'),
        }
      : false,
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx', '.json'],
    modules: ['node_modules'],
    alias: utils.mapTypescriptAliasToWebpackAlias(),
  },
  ignoreWarnings: [
    /* These are to remove the cannot parse source-map warnings*/
    /Failed to parse source map/,
    {
      module: /node_modules\/css-loader\/dist/,
    },
    {
      module: /node_modules\/sass-loader\/dist/,
    },
  ],
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: getTsLoaderRule(options.env),
        include: [utils.root('./src/main/webapp/app')],
        exclude: [utils.root('node_modules')],
      },
      {
        test: /\.(jpe?g|png|gif|svg|woff2?|ttf|eot)$/i,
        type: 'asset/resource',
      },
      /*
      {
        test: /\.(jpe?g|png|gif|svg|woff2?|ttf|eot)$/i,
        loader: 'file-loader',
        options: {
          digest: 'hex',
          hash: 'sha512',
          name: 'content/[hash].[ext]'
        }
      },*/
      {
        enforce: 'pre',
        test: /\.jsx?$/,
        exclude: /node_modules/,
        use: ['source-map-loader'],
      },
      /* {
        test: /\.(j|t)sx?$/,
        enforce: 'pre',
        loader: 'eslint-loader',
        exclude: [utils.root('node_modules')]
      }, */
      {
        test: /\.(js|mjs|jsx)$/,
        include: [utils.root('./src/main/webapp/app')],
        loader: 'babel-loader',
      },
    ],
  },
  stats: {
    children: false,
  },
  optimization: {
    splitChunks: {
      chunks: 'async',
      minSize: 20000,
      minChunks: 1,
      maxAsyncRequests: 30,
      maxInitialRequests: 30,
      cacheGroups: {
        defaultVendors: {
          test: /[\\/]node_modules[\\/]/,
          priority: -10,
          reuseExistingChunk: true,
        },
        default: {
          minChunks: 2,
          priority: -20,
          reuseExistingChunk: true,
        },
      },
    },
    moduleIds: 'named',
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env': {
        // APP_VERSION is passed as an environment variable from the Gradle / Maven build tasks.
        VERSION: `'${process.env.hasOwnProperty('APP_VERSION') ? process.env.APP_VERSION : 'DEV'}'`,
        DEBUG_INFO_ENABLED: options.env === 'development',
        // The root URL for API calls, ending with a '/' - for example: `"https://www.jhipster.tech:8087/myservice/"`.
        // If this URL is left empty (""), then it will be relative to the current context.
        // If you use an API server, in `prod` mode, you will need to enable CORS
        // (see the `jhipster.cors` common JHipster property in the `application-*.yml` configurations)
        SERVER_API_URL: `''`,
      },
      'process.env.NODE_ENV': JSON.stringify(options.mode || 'development'),
    }),
    //new ForkTsCheckerWebpackPlugin({ eslint: true }),
    new CopyWebpackPlugin({
      patterns: [
        {
          from: "./node_modules/swagger-ui/dist/",
          to: 'swagger-ui',
          globOptions: {
            ignore: ['**/*.map' ,'index.html'],
          },
          noErrorOnMissing: true,
        },

        { from: './node_modules/axios/dist/axios.min.js', to: 'swagger-ui', noErrorOnMissing: true },
        { from: './src/main/webapp/swagger-ui/', to: 'swagger-ui', noErrorOnMissing: true },
        { from: './src/main/webapp/content/', to: 'content', noErrorOnMissing: true },
        { from: './src/main/webapp/favicon.ico', to: 'favicon.ico', noErrorOnMissing: true },
        { from: './src/main/webapp/manifest.webapp', to: 'manifest.webapp', noErrorOnMissing: true },
        { from: './src/main/webapp/manifest.json', to: 'manifest.json', noErrorOnMissing: true },
        { from: './src/main/webapp/static/backend/', to: 'backend', noErrorOnMissing: true },
        { from: './src/main/webapp/static/customer/', to: 'customer', noErrorOnMissing: true },
        { from: './src/main/webapp/static/customer-v3/', to: 'customer-v3', noErrorOnMissing: true },
        { from: './src/main/webapp/static/bundles/', to: 'bundles', noErrorOnMissing: true },
        { from: './src/main/webapp/static/bundles/', to: 'bundles-v2', noErrorOnMissing: true },
        { from: './src/main/webapp/static/bundles/', to: 'bundles-v3', noErrorOnMissing: true },
        { from: './src/main/webapp/static/appstleMenu/', to: 'appstle-menu', noErrorOnMissing: true },
        // jhipster-needle-add-assets-to-webpack - JHipster will add/remove third-party resources in this array
        { from: './src/main/webapp/robots.txt', to: 'robots.txt' },
      ],
    }),
    new HtmlWebpackPlugin({
      template: './src/main/webapp/index.html',
      inject: 'body',
      excludeChunks: ['backend', 'customer', 'customer-v3', 'bundles', 'bundles-v2', 'appstle-menu', 'bundles-v3'],
    }),
    new HtmlWebpackPlugin({
      filename: 'backend/index.html',
      template: './src/main/webapp/index.html',
      inject: 'body',
      excludeChunks: ['main', 'customer', 'customer-v3', 'bundles', 'bundles-v2', 'appstle-menu', 'bundles-v3'],
    }),
    new HtmlWebpackPlugin({
      filename: 'customer/index.html',
      template: './src/main/webapp/customer.html',
      inject: 'body',
      excludeChunks: ['main', 'backend', 'bundles', 'bundles-v2', 'customer-v3', 'appstle-menu', 'bundles-v3'],
    }),
    new HtmlWebpackPlugin({
      filename: 'customer-v3/index.html',
      template: './src/main/webapp/customer-v3.html',
      inject: 'body',
      excludeChunks: ['main', 'backend', 'bundles', 'bundles-v2', 'customer', 'appstle-menu', 'bundles-v3'],
    }),
    new HtmlWebpackPlugin({
      filename: 'bundles/index.html',
      template: './src/main/webapp/bundles.html',
      inject: 'body',
      excludeChunks: ['main', 'backend', 'customer-v3', 'customer', 'bundles-v2', 'appstle-menu', 'bundles-v3'],
    }),
    new HtmlWebpackPlugin({
      filename: 'bundles-v2/index.html',
      template: './src/main/webapp/bundles-v2.html',
      inject: 'body',
      excludeChunks: ['main', 'backend', 'customer-v3', 'customer', 'bundles', 'appstle-menu', 'bundles-v3'],
    }),
    new HtmlWebpackPlugin({
      filename: 'appstle-menu/index.html',
      template: './src/main/webapp/appstle-menu.html',
      inject: 'body',
      excludeChunks: ['main', 'backend', 'customer-v3', 'customer', 'bundles', 'bundles-v2', 'bundles-v3'],
    }),
    new HtmlWebpackPlugin({
      filename: 'bundles-v3/index.html',
      template: './src/main/webapp/bundles-v3.html',
      inject: 'body',
      excludeChunks: ['main', 'backend', 'customer-v3', 'customer', 'bundles', 'appstle-menu', 'bundles-v2'],
    }),
    new BaseHrefWebpackPlugin({ baseHref: '/' }),
  ],
});
