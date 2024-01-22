import PageTitle from 'app/Layout/AppMain/PageTitle';
import React, {Component, Fragment} from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {IntercomAPI} from 'react-intercom';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";

export class WidgetPlacement extends Component {
  render() {
    return (
      <FeatureAccessCheck
        hasAnyAuthorities={'enableWidgetPlacement'}
        upgradeButtonText="Upgrade to enable widget & price placement"
      >
        <Fragment>
          <ReactCSSTransitionGroup
            component="div"
            transitionName="TabsAnimation"
            transitionAppear
            transitionAppearTimeout={0}
            transitionEnter={false}
            transitionLeave={false}
          >
            <PageTitle
              heading="Widget & Price Placement"
              subheading="Enable Subscription Widget & Price on Collection and Home pages."
              icon="lnr-pencil icon-gradient bg-mean-fruit"
            />

            <div className="text-center">
              <p>
                By default, we configure the subscription widget only the Product Page. However, our widget and subscription price can also be configured on
                Collection
                and Home pages. Please message us{' '}
                <Link
                  onClick={() => {
                    IntercomAPI('showNewMessage', "I would like to enable widget on Collection and Home pages. Can you please help?");
                  }}
                >
                  here
                </Link>{', '}
                if you would like to have these additional widgets.
              </p>

              <p>
                Please note that this feature is available only in the 'Business' or a Higher Plan, and will require us to update your theme, through temporary access to your store.
              </p>

            </div>
          </ReactCSSTransitionGroup>
        </Fragment>
      </FeatureAccessCheck>
    );
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WidgetPlacement);
