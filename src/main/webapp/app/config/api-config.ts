/* tslint:disable */
let isLocalhost;

const hostname = window && window.location && window.location.hostname;
if (hostname === 'localhost') {
  isLocalhost = true;
} else {
  isLocalhost = false;
}
export const IS_LOCALHOST = isLocalhost;
