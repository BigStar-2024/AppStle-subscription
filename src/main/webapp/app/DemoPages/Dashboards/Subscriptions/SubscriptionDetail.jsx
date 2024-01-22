import React, { Fragment, useEffect, useState } from 'react';
import { connect } from 'react-redux';
import Loader from 'react-loaders';
import BlockUi from '@availity/block-ui';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {toast} from 'react-toastify';
import MySubscriptionDetail from 'app/DemoPages/Dashboards/Shared/MySubscriptionDetail';
import { deleteEntity, getEntity } from 'app/entities/subscriptions/subscription.reducer';
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";

const SubscriptionDetail = ({
  subscriptionEntities,
  getEntity,
  deleteEntity,
  loading,
  updating,
  subscriptionListEntities,
  updateSuccess,
  ...props
}) => {
  const [removeBuffer, setRemoveBuffer] = useState(false);
  const [currentSubscriptionIndex, setCurrentSubscriptionIndex] = useState(null);
  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };
  useEffect(() => {
    if (subscriptionListEntities) {
      let indexOfCurrentSubscriptionDetails = subscriptionListEntities.findIndex(
        subscription => subscription.subscriptionContractId == props.match.params.id
      );
      setCurrentSubscriptionIndex(indexOfCurrentSubscriptionDetails);
    }
  }, [subscriptionListEntities, props.match.params.id]);

  const nextSubscriptionDetails = () => {
    if (currentSubscriptionIndex < subscriptionListEntities.length - 1) {
      const nextSubscriptionDetails = subscriptionListEntities[currentSubscriptionIndex + 1];
      props.history.push(`/dashboards/subscription/${nextSubscriptionDetails.subscriptionContractId}/detail`);
      setCurrentSubscriptionIndex(currentSubscriptionIndex + 1);
    }
  };
  const previousSubscriptionDetails = () => {
    if (currentSubscriptionIndex > 0) {
      const previousSubscriptionDetails = subscriptionListEntities[currentSubscriptionIndex - 1];
      props.history.push(`/dashboards/subscription/${previousSubscriptionDetails.subscriptionContractId}/detail`);
      setCurrentSubscriptionIndex(currentSubscriptionIndex - 1);
    }
  };

  useEffect(() => {
    getEntity(props.match.params.id);
  }, [props.match.params.id, updateSuccess]);

  const cancelSubscription = id => {
    deleteEntity(id, props.account.login)
    .then(res => console.log(res,'res'))
    .catch(error => {toast.error(error?.response?.data?.detail, options)});
  };

  return (
    <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle heading="Subscription Details" icon="pe-7s-pen icon-gradient" />
        <BlockUi tag="div" blocking={loading && !subscriptionEntities?.subscriptionContracts} loader={<Loader active type="line-scale" />}>
          {subscriptionEntities?.subscriptionContracts ? (
            <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
              <Loader type="line-scale" />
            </div>
          ) : (
            <MySubscriptionDetail
              subscriptionEntities={subscriptionEntities}
              cancelSubscription={cancelSubscription}
              isSubscriptionEntityUpdated={props.isSubscriptionEntityUpdated}
              removeBuffer={false}
              updating={updating}
              shopName={props.account.login}
              customerId={subscriptionEntities?.customer?.id.split('/')[4]}
              contractId={props.match.params.id}
              nextSubscriptionDetails={nextSubscriptionDetails}
              previousSubscriptionDetails={previousSubscriptionDetails}
              currentSubscriptionIndex={currentSubscriptionIndex}
              getSubscriptionEntity={getEntity}
            />
          )}
        </BlockUi>
        <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
            <h6>Top Bar</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/835-hT_GkDQ" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Adding and Removing Products and Attributes</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/5hsTuLus1i4" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Pricing Policy</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/c-EyYIoQjLM" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Central Panel</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/gEbM43EDUpo" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Discount Codes</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/NGMBXWfsZRA" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Side Panel</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/qW5_Dopw9BA" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Past Orders</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/GLeVBXSNmv4" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Upcoming Orders</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/R3gJZb7ZSIw" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Cancel Subscription</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/3Mhcr0aVYrM" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Activity Logs</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/dBlNeE3SLIk" title="YouTube video player" 
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
          </HelpPopUp>
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  subscriptionEntities: state.subscription.entity,
  // totalItem: customer.totalItems,
  updating: state.subscription.updating,
  loading: state.subscription.subscriptionLoading,
  account: state.authentication.account,
  isSubscriptionEntityUpdated: state.subscription.isSubscriptionEntityUpdated,
  subscriptionListEntities: state.subscriptionContractDetails.entities,
  updateSuccess: state.subscriptionContractDetails.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  deleteEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionDetail);
