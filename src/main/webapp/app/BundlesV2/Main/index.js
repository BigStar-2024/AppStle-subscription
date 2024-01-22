import React, {Fragment} from 'react';
import {connect} from 'react-redux';
import ResizeDetector from 'react-resize-detector';
import {withRouter} from 'react-router-dom';
import AppMain from '../AppMain/index';

class Main extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
    };
  }

  componentDidMount() {

  }

  componentWillReceiveProps = newProps => {

  };

  render() {
    return (
      <ResizeDetector
        handleWidth
        render={({width}) => (
          <Fragment>
            <div>
              <AppMain/>
            </div>
          </Fragment>
        )}
      />
    );
  }
}

const mapStateToProp = function (state) {

  return {

  };
};

const mapDispatchToProps = {

};

export default withRouter(connect(mapStateToProp, mapDispatchToProps)(Main));
