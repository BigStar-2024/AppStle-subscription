import React, { Fragment } from 'react';
import './AppFooter.scss';
import {connect} from 'react-redux';

class AppFooter extends React.Component {
  render() {

    const isDataSyncRequired = this.props.keyValue && this.props.keyValue.value === 'true'
    if(isDataSyncRequired) {
      return (
        <Fragment>
          {/* <div className="app-footer">
              <div className="app-footer__inner">
                  <div className="app-footer-left">
                    <b style={{paddingRight: 80}}>You have made changes that require running data-sync.</b>
                  </div>
              </div>
          </div> */}
        </Fragment>
      );
    } else {
      return null;
    }
  }
}

const mapStateToProps = state => ({
  enablePageTitleIcon: state.ThemeOptions.enablePageTitleIcon,
  enablePageTitleSubheading: state.ThemeOptions.enablePageTitleSubheading
});

const mapDispatchToProps = dispatch => ({});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AppFooter);
