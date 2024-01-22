import React, {useState, useEffect, Fragment, useCallback} from 'react';
import {connect} from 'react-redux';
import Loader from 'react-loaders';
import {
  Button,
  Table,
  Input,
  Label,
  Row,
  Col,
  ListGroup,
  Alert,
  Modal, 
  ModalHeader, 
  ModalBody, 
  ModalFooter, 
  FormFeedback,
  ListGroupItem
} from 'reactstrap';
import {Link} from "react-router-dom";
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {getEntities, deleteEntity} from 'app/entities/subscription-group/subscription-group.reducer'
import ConfirmDeletePopup from '../Utilities/ConfirmDeletePopup';
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import SweetAlert from 'sweetalert-react';
import axios from 'axios';
import YoutubeVideoPlayer from '../Tutorials/YoutubeVideoPlayer';

const SubscriptionGroupList = ({subscriptionGrpEntities, getEntities, deleteEntity, loading, history}) => {

  const [isOpenFlag, setIsOpenFlag] = useState(false);
  const [subsctiptionPlanId, setSubsctiptionPlanId] = useState(null)
  const [deleteLoading, setDeleteLoading] = useState(false)

  const [isExportModalOpen, setIsExportModalOpen] = useState(false);
  const [emailValidity, setEmailValidity] = useState(true);
  const [emailSendingProgress, setEmailSendingProgress] = useState(false);
  const [blurred, setBlurred] = useState(false);
  const [inputValueForTestEmailId, setInputValueForTestEmailId] = useState('');
  const [emailSuccessAlert, setEmailSuccessAlert] = useState(false);
  const [emailFailAlert, setEmailFailAlert] = useState(false);
  const [allowExport, setAllowExport] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);

  const toggleModal = () => setIsOpenFlag(!isOpenFlag);
  useEffect(() => {
    getEntities()
  }, [])

  useEffect(() => {
    if(subscriptionGrpEntities && subscriptionGrpEntities.length > 0) {
      setAllowExport(true);
    }
  }, [subscriptionGrpEntities])

  const intitialState = {
    search: ''
  };

  const removeSubscriptionGrp = (id) => {
    setSubsctiptionPlanId(id);
    setIsOpenFlag(true);
    // deleteEntity(id);
  }

  const deleteSubscriptionPlan = (id) => {
    toggleModal();
    deleteEntity(id);
  }

  const onDeleteModalClose = () => {
    setSubsctiptionPlanId(null);
    toggleModal();
  }

  const cleanupBeforeModalClose = () => {
    setEmailSendingProgress(false);
    setEmailValidity(true);
    setIsExportModalOpen(!isExportModalOpen);
    setBlurred(false);
    setInputValueForTestEmailId('');
  }

  const checkEmailValidity = (emailId) => {
    if (/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      .test(emailId)) {
      setEmailValidity(true);
      return true
    } else {
      setEmailValidity(false)
      return false;
    }
  }

  const sendExportEmail = (emailId) => {
    if (checkEmailValidity(emailId)) {
      setEmailSendingProgress(true);
      axios.get(`api/subscription-groups/export-subscription-group-plans`, {
        params: {
          email: emailId
        },
      }).then(response => {
        cleanupBeforeModalClose();
        setEmailSuccessAlert(true);
      })
        .catch(error => {
          cleanupBeforeModalClose();
          setEmailFailAlert(true);
        })
    }
  }


  return (
    <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}>
        <PageTitle heading="Subscription plans - Add/Manage"
                   subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/4924892-how-do-i-create-a-subscription-plan' target='blank'> Need help? Follow these instruction to create a Subscription plan.</a>"
                   icon="pe-7s-network icon-gradient"
                   enablePageTitleAction
                   actionTitle="Create Subscription Plan"
                   onActionClick={() => {
                     history.push('/dashboards/subscription-plan/new');
                   }}
                   enableSecondaryPageTitleAction
                   secondaryActionTitle="Export"
                   onSecondaryActionClick={() => {
                    setIsExportModalOpen(!isExportModalOpen);
                  }}
                  sticky={true}
                  tutorialButton={{
                    show: true,
                    videos: [
                      {
                        title: "Pay As You Go",
                        url: "https://www.youtube.com/watch?v=AKR4gfRdEMU",
                      },
                      {
                        title: "Prepaid One Time",
                        url: "https://www.youtube.com/watch?v=zNJSLOhOJYM",
                      },
                      {
                        title: "Prepaid Auto Renew",
                        url: "https://www.youtube.com/watch?v=P0ncMfwwz_s"
                      },
                      {
                        title: "Subscription Plan Types - Pay As You Go Vs Prepaid One Time Vs Prepaid Auto Renew",
                        url: "https://www.youtube.com/watch?v=lDyOnOxCuT4"
                      }
                    ],
                    docs: [
                      {
                        title: "How Do I Create a Subscription Plan?",
                        url: "https://intercom.help/appstle/en/articles/4924892-how-do-i-create-a-subscription-plan"
                      }
                    ]
                  }}
        />
        {/* <Alert className="mb-2" color="warning">
          <h6>Need assistance?</h6>
          <p>Watch our tutorial videos or read our help documentation to get a better understanding.</p>
          <span>
            <Button color="warning" onClick={toggleShowModal}>Video Tutorials</Button>
            <a 
              href='https://intercom.help/appstle/en/articles/4924892-how-to-create-a-subscription-plan'
              target='_blank'
              rel="noopener noreferrer"
              style={{marginLeft: '10px'}}
            >
              Help Documentation
            </a>
          </span>
        </Alert>
        <Modal isOpen={showModal} toggle={toggleShowModal}>
          <ModalHeader toggle={toggleShowModal}>Tutorial Videos</ModalHeader>
          <ModalBody>
            <div style={{ height: '500px', overflowY: 'scroll' }}>
              <div className="mt-4 border-bottom pb-4">
                <h6>Pay As You Go</h6>
                <YoutubeVideoPlayer 
                  url="https://www.youtube.com/watch?v=AKR4gfRdEMU"
                  iframeHeight="100%"
                  divClassName="video-container"
                  iframeClassName="responsive-iframe"
                />
              </div>
              <div className="py-4 border-bottom">
                <h6>Prepaid One Time</h6>
                <YoutubeVideoPlayer 
                  url="https://www.youtube.com/watch?v=zNJSLOhOJYM"
                  iframeHeight="100%"
                  divClassName="video-container"
                  iframeClassName="responsive-iframe"
                />
              </div>
              <div className="py-4 border-bottom">
                <h6>Prepaid Auto Renew</h6>
                <YoutubeVideoPlayer 
                  url="https://www.youtube.com/watch?v=P0ncMfwwz_s"
                  iframeHeight="100%"
                  divClassName="video-container"
                  iframeClassName="responsive-iframe"
                />
              </div>
            </div>
          </ModalBody>
          <ModalFooter>
            <Button color="link" onClick={toggleShowModal}>Cancel</Button>
          </ModalFooter>
        </Modal> */}
        <Modal isOpen={isExportModalOpen} toggle={() => setIsExportModalOpen(!isExportModalOpen)} backdrop>
          <ModalHeader>Export Subscription Group Plans</ModalHeader>
          <ModalBody>
            {allowExport ? <>
            <Label>Email id</Label>
            <Input
              type="email"
              invalid={!emailValidity}
              onBlur={event => {
                setInputValueForTestEmailId(event.target.value);
                checkEmailValidity(event.target.value);
                setBlurred(true);
              }}
              onInput={event => {
                if (blurred) {
                  setInputValueForTestEmailId(event.target.value);
                  checkEmailValidity(event.target.value);
                }
              }}
              placeholder="Please enter email id here"
            />
            <FormFeedback>Please Enter a valid email id</FormFeedback>
            <br/>
            </> : <p>
                No Subscription groups to export.
              </p>}

          </ModalBody>
          <ModalFooter>
            <Button color="secondary" onClick={() => {
              cleanupBeforeModalClose()
            }}>
              Cancel
            </Button>
            {allowExport && <MySaveButton
              onClick={() => {
                sendExportEmail(inputValueForTestEmailId)
              }}
              text="Send Email"
              updating={emailSendingProgress}
              updatingText={'Sending'}
            />}
          </ModalFooter>
        </Modal>

        <SweetAlert
          title="Export Request Submitted"
          confirmButtonColor=""
          show={emailSuccessAlert}
          text="Export may take time based on the number of subscriptions groups in your store. Rest assured, once it's processed, it will be emailed to you."
          type="success"
          onConfirm={() => setEmailSuccessAlert(false)}
        />
        <SweetAlert
          title="Failed"
          confirmButtonColor=""
          show={emailFailAlert}
          text="Something bad happened or an export is already running. Please try again."
          type="error"
          onConfirm={() => setEmailFailAlert(false)}
        />

        {
          loading && !subscriptionGrpEntities.length > 0 ?
            (<div style={{margin: "10% 0 0 43%"}}
                  className="loader-wrapper d-flex justify-content-center align-items-center">
              <Loader type="line-scale"/>
            </div>)
            :
            subscriptionGrpEntities && subscriptionGrpEntities.length > 0 ?
              <Fragment>
                <ListGroup>
                  <ListGroupItem>
                    <Table className="mb-0">
                      <thead>
                      <tr>
                        <th>Name</th>
                        <th>Product Count</th>
                        <th>Variant Count</th>
                        <th style={{textAlign: "center"}}>Loyalty</th>
                        <th style={{textAlign: "center"}}>Manage Plan</th>
                        <th style={{textAlign: "center"}}>Clone</th>
                      </tr>
                      </thead>
                      <tbody>
                      {
                        subscriptionGrpEntities?.map((item) => {
                          return (
                            <tr>
                              <td>{item.groupName}</td>
                              <td>{item.productCount}</td>
                              <td>{item?.productVariantCount}</td>
                              <td style={{textAlign: "center"}}>
                                <Link to={`/dashboards/loyalty-plan/${item.id}/edit`}>
                                  <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill"
                                          title="Edit Subscription Plan" color="info">
                                    <i className="lnr-pencil btn-icon-wrapper"> </i>
                                  </Button>
                                </Link>
                              </td>
                              <td style={{textAlign: "center"}}>
                                <Link to={`/dashboards/subscription-plan/${item.id}/edit`}>
                                  <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill"
                                          title="Edit Subscription Plan" color="info">
                                    <i className="lnr-pencil btn-icon-wrapper"> </i>
                                  </Button>
                                </Link>

                                <Link onClick={() => removeSubscriptionGrp(item.id)}>
                                  <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill"
                                          title="Remove Subscription Plan" color="warning">
                                    <i className="lnr-trash btn-icon-wrapper"> </i>
                                  </Button>
                                </Link>
                              </td>
                              <td style={{textAlign:"center"}}>
                                <Link to={`/dashboards/subscription-plan/new/${item.id}`}>
                                  <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill" title="Copy" color="info">
                                    <i className="pe-7s-copy-file btn-icon-wrapper"> </i>
                                  </Button>
                                </Link>
                              </td>
                            </tr>
                          )
                        })
                      }
                      </tbody>
                    </Table>
                  </ListGroupItem>
                </ListGroup>
              </Fragment>
              :
              (<div className='welcome-page-with-video'>

                <Row className="align-items-start">
                  <Col sm='6'>
                    <div>
                      <h4>Welcome to Subscription Plan</h4>
                      <p className='text-muted'>
                        This page will give you a quick drive-through of all the currently available subscription plans
                        in your store - both the products that can be subscribed, and the timeframe. You can add new
                        subscription plans and also make changes to the existing plans via this page.
                      </p>
                      <p className='text-muted'>
                        Please read the <a
                        href="https://intercom.help/appstle/en/articles/4924892-how-do-i-create-a-subscription-plan"
                        target="blank">this</a> doc, to know more about creating subscription plans, and to learn how
                        other merchants are using subscriptions!

                      </p>
                      <p className='text-muted'>If you have any questions at any time, just reach out to us on <a
                        href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>
                      <Button
                        color="primary"
                        size='lg'
                        onClick={() => {
                          history.push('/dashboards/subscription-plan/new');
                        }}>
                        <span className='font-size-lg font-weight-light'>Create First Subscription Plan</span>
                      </Button>
                      <br/> <br/>
                    </div>
                  </Col>
                </Row>
                </div>)
        }
        {
          // <HelpPopUp defaultOpen={(!loading && (subscriptionGrpEntities && subscriptionGrpEntities.length <= 0))}>
          //   <div className="mt-4 border-bottom pb-4">
          //     <h6>Pay As You Go</h6>
          //     <YoutubeVideoPlayer 
          //       url="https://www.youtube.com/watch?v=AKR4gfRdEMU"
          //       iframeHeight="100%"
          //       divClassName="video-container"
          //       iframeClassName="responsive-iframe"
          //     />
          //   </div>
          //   <div className="py-4 border-bottom">
          //     <h6>Prepaid One Time</h6>
          //     <YoutubeVideoPlayer 
          //       url="https://www.youtube.com/watch?v=zNJSLOhOJYM"
          //       iframeHeight="100%"
          //       divClassName="video-container"
          //       iframeClassName="responsive-iframe"
          //     />
          //   </div>
          //   <div className="py-4 border-bottom">
          //     <h6>Prepaid Auto Renew</h6>
          //     <YoutubeVideoPlayer 
          //       url="https://www.youtube.com/watch?v=P0ncMfwwz_s"
          //       iframeHeight="100%"
          //       divClassName="video-container"
          //       iframeClassName="responsive-iframe"
          //     />
          //   </div>
          // </HelpPopUp>
        }

      </ReactCSSTransitionGroup>


      {/* DELETE POPUP MODEL */}
      <ConfirmDeletePopup
        buttonLabel="Mybutton"
        modaltitle="Subscription Plan "
        modalMessage="Are you sure to delete ?"
        confirmBtnText="Yes"
        cancelBtnText="No"
        isOpenFlag={isOpenFlag}
        toggleModal={toggleModal}
        onCloseModel={onDeleteModalClose}
        deleteId={subsctiptionPlanId}
        deleteLoading={deleteLoading}
        deleteEntity={id => {
          setDeleteLoading(true);
          deleteEntity(id).then(res => {
            setIsOpenFlag(false);
            setDeleteLoading(false)
          }).catch(error => {
            setIsOpenFlag(false);
            setDeleteLoading(false)
          })
        }}
      />
    </Fragment>
  )
}

const mapStateToProps = state => ({
  subscriptionGrpEntities: state.subscriptionGroup.entities,
  loading: state.subscriptionGroup.loading,
});

const mapDispatchToProps = {
  getEntities,
  deleteEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionGroupList);
