import React from 'react';
import {connect} from 'react-redux';
import ResizeDetector from 'react-resize-detector';
import AppMain from '../AppMain/index';
import {MainProvider} from "app/AppstleMenu/context";
import './index.scss'

const Main = () => {
  return (
    <ResizeDetector
      handleWidth
      render={({width}) => (
        <MainProvider>
          <AppMain/>
        </MainProvider>
      )}
    />
  );
}

const mapStateToProp = function (state) {
  return {};
};

const mapDispatchToProps = {};

export default connect(mapStateToProp, mapDispatchToProps)(Main);
