import React from 'react';
import { Card, CardBody } from 'reactstrap';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import { IntercomAPI } from 'react-intercom';

const HomeAndCollectionPageWidget = () => {
  const openIntercomRequest = event => {
    event.preventDefault();
    IntercomAPI('showNewMessage', 'I would like to enable widget on Collection and Home pages. Can you please help?');
  };

  return (
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle
          heading="Home Page & Collection Page Widget"
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5000394-customizing-the-subscription-widget' target='blank'> Click here to learn about customizing your subscription cart widget.</a>"
          icon="lnr-home icon-gradient bg-mean-fruit"
          sticky={true}
        />
        <FeatureAccessCheck hasAnyAuthorities={'enableWidgetPlacement'} upgradeButtonText="Upgrade your plan">
        <Card className="main-card">
          <CardBody>
            <p>
              By default, we configure the subscription widget only for the Product Page. However, our widget and subscription price can
              also be configured for the Home page and Collection page. Please message us{' '}
              <a href="#" onClick={openIntercomRequest}>
                here
              </a>
              {', '}
              if you would like to have these additional widgets.
            </p>

            <p>
              Please note that this feature is available only in the 'Business' or a Higher Plan, and will require us to update your theme,
              through temporary access to your store.
            </p>
          </CardBody>
        </Card>
        </FeatureAccessCheck>
      </ReactCSSTransitionGroup>
  );
};

export default HomeAndCollectionPageWidget;
