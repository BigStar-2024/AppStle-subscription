import React from 'react';

interface IErrorBoundaryProps {
  readonly children: JSX.Element | JSX.Element[];
}

declare global {
  interface Window {
      Shopify:any;
  }
}

interface IErrorBoundaryState {
  readonly error: any;
  readonly errorInfo: any;
}
import '../../appstle-custombootstrap.scss';
import { Card, CardBody } from 'reactstrap';
import { getEntity as getPortalSettings } from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import { connect } from 'react-redux';
import { redirectToAccountPage } from 'app/shared/util/customer-utils';

class ErrorBoundary extends React.Component<any> {
  readonly state: IErrorBoundaryState = { error: undefined, errorInfo: undefined };

  componentDidCatch(error, errorInfo) {
    this.setState({
      error,
      errorInfo
    });
    this.props.getPortalSettings(0, window?.Shopify.shop);
  }

  render() {
    const { error, errorInfo } = this.state;
    if (errorInfo) {
      return (
        <div className="app-main appstle-customer-portal">
          {/* <style>
            {this.props?.subCustomCSSEntity?.customerPoratlCSS?.replaceAll("\n", "").replaceAll('"', '')}
          </style> */}
          <div className="app-main__outer">
            <div className="app-main__inner">
              <div className="container clearfix">
                {this?.props?.loading ? (
                  <div
                    style={{ margin: '10% 0 0 43%', flexDirection: 'column' }}
                    className="loader-wrapper d-flex justify-content-center align-items-center"
                  >
                    <div className="appstle_preloader appstle_loader--big"></div>
                  </div>
                ) : (
                  <Card style={{ margin: '28px 0px', borderRadius: '8px 8px 0 0', boxShadow: '0 10px 20px 0 rgb(0 0 0 / 15%)' }}>
                    <CardBody>
                      <p style={{ textAlign: 'center' }}>
                        {this?.props?.customerPortalSettingEntity?.expiredTokenText}{' '}
                        <span style={{ color: '#13b5ea', cursor: 'pointer' }} onClick={() => redirectToAccountPage()}>
                          {this?.props?.customerPortalSettingEntity?.portalLoginLinkText}
                        </span>
                      </p>
                    </CardBody>
                  </Card>
                )}
              </div>
            </div>
          </div>
        </div>
      );
    }
    return this.props.children;
  }
}

const mapStateToProps = state => ({
  customerPortalSettingEntity: state.customerPortalSettings.entity,
  loading: state.customerPortalSettings.loading
});

const mapDispatchToProps = {
  getPortalSettings
};

export default connect(mapStateToProps, mapDispatchToProps)(ErrorBoundary);
