import React, { Fragment, useEffect, useState } from 'react';
import { connect } from 'react-redux';
import Loader from 'react-loaders';
import { Button, ButtonGroup, Card, CardBody, Col, Row, Table } from 'reactstrap';
import { Link } from 'react-router-dom';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { changeBuildABoxStatus, deleteEntity, getEntities } from 'app/entities/subscription-bundling/subscription-bundling.reducer.ts';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import YoutubeVideoPlayer from '../Tutorials/YoutubeVideoPlayer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCopy, faEdit, faExternalLinkAlt, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import ConfirmDeletePopup from '../Utilities/ConfirmDeletePopup';
import cx from 'classnames';
import HelpPopUp from 'app/DemoPages/Components/HelpPopUp/HelpPopUp';
import { completeChecklistItem } from 'app/entities/onboarding-info/onboarding-info.reducer';
import OnboardingChecklistStep from 'app/shared/model/enumerations/onboarding-checklist-step.model';

function SubscriptionBundlingList({
  subscriptionBundlingEntities,
  getEntities,
  deleteEntity,
  loading,
  history,
  changeBuildABoxStatus,
  updating,
  completeChecklistItem
}) {
  const [isOpenFlag, setIsOpenFlag] = useState(false);
  const [buildABoxId, setBuildABoxId] = useState(null);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [processingId, setProcessingId] = useState(null);
  const toggleModal = () => setIsOpenFlag(!isOpenFlag);
  const [isPageAccessible, setIsPageAccessible] = useState(true);

  useEffect(() => {
    getEntities();
    completeChecklistItem(OnboardingChecklistStep.BUILD_A_BOX)
  }, []);

  const handleRemove = id => {
    setBuildABoxId(id);
    setIsOpenFlag(true);
  };

  const onDeleteModalClose = () => {
    setBuildABoxId(null);
    toggleModal();
  };

  const handleActiveInactiveBuildABox = (buildABoxId, status) => {
    setProcessingId(buildABoxId);
    changeBuildABoxStatus(buildABoxId, status);
  };

  return (
    <>
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
            heading="Build-A-Box - Manage"
            /*subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/4924892-how-do-i-create-a-subscription-plan' target='blank'> Need help? Follow these instruction to manage subscription bundling.</a>"*/
            icon="pe-7s-network icon-gradient"
            enablePageTitleAction={isPageAccessible}
            actionTitle="Create Build-A-Box"
            onActionClick={() => {
              history.push('/dashboards/subscription-bundling/new');
            }}
            sticky={true}
            tutorialButton={{
              show: true,
              videos: [
                {
                  title: 'Build-A-Box Classic',
                  url: 'https://www.youtube.com/watch?v=1sIU8pTYwd4'
                },
                {
                  title: 'Build-A-Box Single Product',
                  url: 'https://www.youtube.com/watch?v=4QPhUyZV0wI'
                }
              ],
              docs: [
                {
                  title: 'How to setup Build-A-Box',
                  url: 'https://intercom.help/appstle/en/articles/5555314-how-to-setup-build-a-box'
                },
                {
                  title: "How to Offer Build-A-Box As a One Time Purchase",
                  url: "https://intercom.help/appstle/en/articles/8048358-how-to-offer-build-a-box-as-a-one-time-purchase"
                }
              ]
            }}
          />
          <FeatureAccessCheck hasAnyAuthorities={'accessBuildABox'} setIsPageAccessible={setIsPageAccessible} upgradeButtonText="Upgrade your plan">
            {loading && !subscriptionBundlingEntities.length > 0 ? (
              <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
                <Loader type="line-scale" />
              </div>
            ) : subscriptionBundlingEntities && subscriptionBundlingEntities.length > 0 ? (
              <Fragment>
                <Card>
                  <CardBody>
                    <Table className="mb-0">
                      <thead>
                        <tr>
                          <th />
                          <th>Name</th>
                          <th>Plan Name</th>
                          <th>Type</th>
                          <th>Min Product</th>
                          <th>Max Product</th>
                          <th>Is Build-A-Box Enabled?</th>
                          <th>Unique Id</th>
                          <th style={{ textAlign: 'center' }}>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        {subscriptionBundlingEntities?.map((item, index) => {
                          return (
                            <tr key={item.subscriptionId}>
                              <td>{index + 1}</td>
                              <td>{item.name || '-'}</td>
                              <td>
                                {item.subscriptionGroup != null
                                  ? JSON.parse(item.subscriptionGroup)
                                      .map(subscription => subscription.label)
                                      .join(', ')
                                  : item.groupName}
                              </td>
                              <td>{item.buildABoxType === 'SINGLE_PRODUCT' ? 'Single Product' : 'Classic'}</td>
                              <td>{item.minProductCount || '-'}</td>
                              <td>{item.maxProductCount || '-'}</td>
                              <td>
                                <div className={'d-flex align-items-center'}>
                                  <div
                                    className="switch has-switch"
                                    data-on-label={true}
                                    data-off-label={false}
                                    onClick={event => {
                                      handleActiveInactiveBuildABox(item.id, !item.subscriptionBundlingEnabled);
                                    }}
                                  >
                                    <div
                                      className={cx('switch-animate', {
                                        'switch-on': item.subscriptionBundlingEnabled,
                                        'switch-off': !item.subscriptionBundlingEnabled
                                      })}
                                    >
                                      <input type="checkbox" />
                                      <span className="switch-left bg-success">ON</span>
                                      <label>&nbsp;</label>
                                      <span className="switch-right bg-success">OFF</span>
                                    </div>
                                  </div>
                                  {processingId === item.id && updating ? <span className="spinner-border spinner-border-sm ml-2" /> : ''}
                                </div>
                              </td>
                              <td>{item.uniqueRef || '-'}</td>
                              <td style={{ textAlign: 'center' }}>
                                <ButtonGroup size="sm" style={{ marginLeft: 'auto' }}>
                                  <Button
                                    disabled={!item?.subscriptionBundleLink}
                                    color="primary"
                                    onClick={() => window.open(item?.subscriptionBundleLink, '_blank')}
                                  >
                                    <FontAwesomeIcon icon={faExternalLinkAlt} />
                                    &nbsp;Open
                                  </Button>
                                  <Button
                                    tag={Link}
                                    to={`/dashboards/subscription-bundling/${item.id}/edit`}
                                    color={'primary'}
                                    className="ml-1"
                                  >
                                    <FontAwesomeIcon icon={faEdit} />
                                    &nbsp;Edit
                                  </Button>
                                  <Button
                                    tag={Link}
                                    to={`/dashboards/subscription-bundling/${item.id}/clone`}
                                    color={'primary'}
                                    className="ml-1"
                                  >
                                    <FontAwesomeIcon icon={faCopy} />
                                    &nbsp;Clone
                                  </Button>
                                  <Button color={'danger'} className="ml-1" onClick={() => handleRemove(item.id)}>
                                    <FontAwesomeIcon icon={faTrashAlt} />
                                    &nbsp;Delete
                                  </Button>
                                </ButtonGroup>
                              </td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </Table>
                  </CardBody>
                </Card>
              </Fragment>
            ) : (
              <Row className="align-items-center welcome-page-with-video">
                <Col sm="5">
                  <div>
                    <h4>Welcome to Subscription Build-A-Box</h4>
                    <p className="text-muted">
                      <span className="text-muted as-font-bold">Build-A-Box</span>, also known as bundling, is a subset of subscription or
                      recurring revenue-based business models. In Build-A-Box, a customer is allowed to literally build their own customized
                      box of products or services and check them out together, as a bundle. The same bundle is delivered on a recurring
                      basis to the customer.
                    </p>
                    <p className="text-muted">
                      This page will give you a quick drive-through of all the currently available subscription plans in your store.
                    </p>
                    <p className="text-muted">
                      Read the{' '}
                      <a href="https://intercom.help/appstle/en/articles/5555314-how-to-setup-build-a-box" target="blank">
                        documentation
                      </a>{' '}
                      to know more about creating subscription bundling, and learn how other merchants are using subscription Build-A-Box!
                    </p>
                    <p className="text-muted">
                      If you have any questions at any time, just reach out to us on{' '}
                      <a href="javascript:window.Intercom('showNewMessage')">our chat widget</a>.
                    </p>
                    <br /> <br />
                  </div>
                </Col>
              </Row>
            )}
          </FeatureAccessCheck>
          <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
              <h6>Build-A-Box - Classic</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=1sIU8pTYwd4"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />
            </div>
            <div className="py-4 border-bottom">
              <h6>Build-A-Box - Single Product</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=4QPhUyZV0wI"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />
            </div>
          </HelpPopUp>
        </ReactCSSTransitionGroup>
      </Fragment>

      <ConfirmDeletePopup
        buttonLabel="Mybutton"
        modaltitle="Build-A-Box"
        modalMessage="Are you sure you want to delete?"
        confirmBtnText="Yes"
        cancelBtnText="No"
        isOpenFlag={isOpenFlag}
        toggleModal={toggleModal}
        onCloseModel={onDeleteModalClose}
        deleteId={buildABoxId}
        deleteLoading={deleteLoading}
        deleteEntity={id => {
          setDeleteLoading(true);
          deleteEntity(id)
            .then(res => {
              setIsOpenFlag(false);
              setDeleteLoading(false);
            })
            .catch(error => {
              setIsOpenFlag(false);
              setDeleteLoading(false);
            });
        }}
      />
    </>
  );
}

const mapStateToProps = state => ({
  subscriptionBundlingEntities: state.subscriptionBundling.entities,
  loading: state.subscriptionBundling.loading,
  updating: state.subscriptionBundling.updating
});

const mapDispatchToProps = {
  getEntities,
  deleteEntity,
  changeBuildABoxStatus,
  completeChecklistItem
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionBundlingList);
