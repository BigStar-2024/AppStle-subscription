/* eslint-disable no-sequences */
/* eslint-disable no-unused-expressions */
/* eslint-disable prefer-rest-params */
/* eslint-disable vars-on-top */
/* eslint-disable no-var */
/* eslint-disable func-names */
// eslint-disable-next-line func-names

import axios from 'axios';
import React from 'react';

const BoardToken = 'ebc9e1db-36f3-bf11-b675-f105a986b744';

export default class Feedback extends React.Component {
  componentDidMount() {
    axios.get('api/miscellaneous/canny-sso-token').then(response => {
      const { data } = response;
      console.log(data);

      (function(w, d, i, s) {
        function l() {
          if (!d.getElementById(i)) {
            const f = d.getElementsByTagName(s)[0];
            const e = d.createElement(s);
            (e.type = 'text/javascript'), (e.async = !0), (e.src = 'https://canny.io/sdk.js'), f.parentNode.insertBefore(e, f);
          }
        }
        if (typeof w.Canny !== 'function') {
          var c = function() {
            c.q.push(arguments);
          };
          (c.q = []),
            (w.Canny = c),
            d.readyState === 'complete' ? l() : w.attachEvent ? w.attachEvent('onload', l) : w.addEventListener('load', l, !1);
        }
      })(window, document, 'canny-jssdk', 'script');

      Canny('render', {
        boardToken: BoardToken,
        basePath: null, // See step 2
        ssoToken: data
      });
    });
  }

  render() {
    return <div data-canny />;
  }
}
