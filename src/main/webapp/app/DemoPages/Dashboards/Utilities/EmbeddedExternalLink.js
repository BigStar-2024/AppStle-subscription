import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import { Redirect } from '@shopify/app-bridge/actions';


export const EmbeddedExternalLink = props => {
  let externalUrl = props.href || props.url;
  return (
    <a href={externalUrl}
      className={props.className}
      onClick={(e) => {
        e.preventDefault();
        if (window.app) {
          Redirect.create(app).dispatch(Redirect.Action.REMOTE, {
            url: externalUrl,
            newContext: true,
          });
        } else {
          window.open(externalUrl, '_blank');
        }
      }}
    >{props.children || props.text || 'Click here'}</a>
  );
}

const mapStateToProp = state => ({
});

const mapDispatchToProps = {};

export default connect(mapStateToProp, mapDispatchToProps)(EmbeddedExternalLink);