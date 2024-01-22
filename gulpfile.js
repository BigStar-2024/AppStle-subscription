const { src, dest } = require('gulp');
const babel = require('gulp-babel');
const uglify = require('gulp-uglify');
const rename = require('gulp-rename');


function minify() {
  return src(['src/main/webapp/content/js/appstle-subscription.js', 'src/main/webapp/content/js/appstle-subscription-v2.js', 'src/main/webapp/content/js/appstle-bundle-v1.js'])
    .pipe(babel())
    .pipe(uglify())
    .pipe(rename({ extname: '.min.js' }))
    .pipe(dest('src/main/webapp/content/js/'));
}

exports.minify = minify;
