import PageTitle from 'app/Layout/AppMain/PageTitle';
import React, { Component, Fragment } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect } from 'react-redux';
import { Card, CardBody, Col, Row } from 'reactstrap';
import { getEntity } from 'app/entities/shop-info/shop-info.reducer';
import BasicIntegrationV2 from "app/DemoPages/Dashboards/Integrations/BasicIntegrationV2";
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";

export class Integrations extends Component {
  constructor(props) {
    super(props);
    this.state = {
      klaviyoContactSync: false,
      klaviyoEmail: false,
      pushowl: false,
      mailchimp: false,
      campaignMonitor: false,
      salesforce: false,
      simpleTexting: false,
      clickSendSMS: false,
      elasticEmail: false,
      wiser: false,
      hubspotContact: false,
      zapier: false
    };
  }

  componentDidMount() {
    this.props.getEntity(-1);
  }

  render() {
    const { showModal } = this.state;
    const toggleShowModal = () => this.setState({ showModal: !showModal });

    return (
      <FeatureAccessCheck
        hasAnyAuthorities={'enableIntegrations'}
        upgradeButtonText="Upgrade to enable integrations"
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
            heading="App Integrations"
            subheading="Integrations with other apps and services"
            icon="lnr-pencil icon-gradient bg-mean-fruit"
            actionTitle="Request New Integration"
            enablePageTitleAction
            onActionClick={() => {
              Intercom('showNewMessage', 'I would like to request a new integration.');
            }}
            sticky={true}
            tutorialButton={{
              show: true,
              videos: [
                {
                  title: "App Integrations for Appstle Subscriptions (with Klaviyo Integration Example)",
                  url: "https://www.youtube.com/watch?v=Vf67zzsFIbo"
                },
                {
                  title: 'Klaviyo Integration - Subscriptions',
                  url: "https://youtu.be/A0UaERSE1XI"
                },
                {
                  title: "Omnisend Integration (All Apps)",
                  url: "https://www.youtube.com/watch?v=TC50zZ4IPdU"
                }
              ],
              docs: [
                {
                  title: "App Integrations",
                  url: "https://intercom.help/appstle/en/collections/3319537-app-integrations"
                }
              ]
            }}
          />
          <Row>
            <Col>
              <Card className="card-tabs">
                <CardBody className="top-elem">
                  <BasicIntegrationV2 />
                </CardBody>
              </Card>
            </Col>
          </Row>
        </ReactCSSTransitionGroup>
      </Fragment>
      </FeatureAccessCheck>
    );
  }
}

const mapStateToProps = ({ shopInfo }) => ({
  shopInfo: shopInfo.entity,
});

const mapDispatchToProps = {
  getEntity,
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Integrations);
