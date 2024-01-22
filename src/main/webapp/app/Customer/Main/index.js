import React, {Fragment} from 'react';
import {connect} from 'react-redux';
import ResizeDetector from 'react-resize-detector';
import {withRouter} from 'react-router-dom';
import AppMain from '../AppMain/index';
import "sweetalert-react/node_modules/sweetalert/dist/sweetalert.css";
import "react-toastify/dist/ReactToastify.css";
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css";
import "./index.scss";

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
