import React, {useEffect, Fragment} from 'react';
import {connect} from 'react-redux';
import Loader from 'react-loaders';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";
import { getEntity, updateEntity, getWebhookPortal } from 'app/entities/shop-info/shop-info.reducer';
import { AppPortal } from "svix-react";
import {Col, Row} from "reactstrap";
import EmbeddedExternalLink from '../Utilities/EmbeddedExternalLink';

const WebhookIntegration = ({
  shopInfo,
  getEntity,
  updateEntity,
  getWebhookPortal,
  webhookPortal,
  loading,
  updating,
  loadingWebhookPortal,
  ...props}) => {

  useEffect(() => {
    if(!shopInfo) {
     getEntity(1)
    }
  }, [])

  useEffect(() => {
    if(shopInfo?.enableWebhook) {
      getWebhookPortal();
    }
  }, [shopInfo])


  const enableDisableWebhookIntegration = value => {
    const entity = {
      ...shopInfo,
      enableWebhook: value ? true : false
    }
    updateEntity(entity);
  };


  return (
    <FeatureAccessCheck
      hasAnyAuthorities={'webhookAccess'}
      upgradeButtonText="upgrade your plan to enable Webhook integrations"
    >
      <Fragment>
        <ReactCSSTransitionGroup
          component="div"
          transitionName="TabsAnimation"
          transitionAppear
          transitionAppearTimeout={0}
          transitionEnter={false}
          transitionLeave={false}>
          <PageTitle
            heading="Subscription Webhook Integration"
            subheading={`<div className="mt-3">
            <div><i class='metismenu-icon lnr-location'></i> <a target="blank" href="https://www.svix.com/event-types/us/org_2EobxsDhQUmWhbYVOVFd8H8tyXf">Click here to see all supported webhook events</a></div>
            <div><i class='metismenu-icon lnr-location'></i> <a target="blank" href="https://docs.svix.com/receiving/introduction">Click here learn how to consume webhooks</a></div>
          </div>`}
            icon="lnr-link icon-gradient"
            enablePageTitleAction={!shopInfo.enableWebhook}
            actionTitle="Enable"
            onActionClick={() => enableDisableWebhookIntegration(true)}
            onActionUpdating={updating}
            enableSecondaryPageTitleAction={shopInfo.enableWebhook}
            secondaryActionTitle="Disable"
            onSecondaryActionClick={() => enableDisableWebhookIntegration(false)}
            onSecondaryActionUpdating={updating}
            sticky={true}
          />
          {loadingWebhookPortal ?
            (<div style={{margin: "10% 0 0 43%"}}
                  className="loader-wrapper d-flex justify-content-center align-items-center">
              <Loader type="line-scale"/>
            </div>)
            : ''
          }
          { shopInfo && shopInfo.enableWebhook && webhookPortal && webhookPortal.url ?
            (<AppPortal
              url={webhookPortal.url}
              fullSize={true}
              primaryColor="#2392ec"
              //loadingIndicator={<Loader type="line-scale"/>}
            />) : (<Row className="align-items-center welcome-page">
              <Col sm='5'>
                <div>
                  <h4>Welcome to Subscription Webhook Integration</h4>
                  <p className="text-muted d-flex flex-column mt-3">
                  <div><i class='metismenu-icon lnr-location'></i> <EmbeddedExternalLink href="https://www.svix.com/event-types/us/org_2EobxsDhQUmWhbYVOVFd8H8tyXf">Click here to see supported webhook events</EmbeddedExternalLink></div>
                  <div><i class='metismenu-icon lnr-location'></i> <EmbeddedExternalLink href="https://docs.svix.com/receiving/introduction">Click here learn how to consume webhooks</EmbeddedExternalLink></div>
                  </p>
                </div>
              </Col>
            </Row>)
          }
        </ReactCSSTransitionGroup>
      </Fragment>
    </FeatureAccessCheck>
  )
}

const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity,
  loading: state.shopInfo.loading,
  updating: state.shopInfo.updating,
  webhookPortal: state.shopInfo.webhookPortal,
  loadingWebhookPortal: state.shopInfo.loadingWebhookPortal,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  getWebhookPortal
};

export default connect(mapStateToProps, mapDispatchToProps)(WebhookIntegration);
