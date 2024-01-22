import cx from 'classnames';
import React, {Component, useEffect, useState} from 'react';
import {connect} from 'react-redux';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import {Alert} from 'antd';
import {getEntities} from 'app/entities/bulk-automation/bulk-automation.reducer'
import CustomHtmlToolTip from "../../DemoPages/Dashboards/SubscriptionGroups/CustomHtmlToolTip";
import LaddaButton, {ZOOM_IN} from 'react-ladda';
import {faBatteryThreeQuarters} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {MdHelp} from 'react-icons/md';
import Axios from "axios";
import useScreenSize from "use-screen-size";
import {THEME_APP_EXTENSION_HANDLE, THEME_APP_EXTENSION_UUID} from "app/config/constants";
import {
  Alert as ReactStrapAlert,
  Button,
  UncontrolledTooltip,
  Progress,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from 'reactstrap';
import YoutubeVideoPlayer from 'app/DemoPages/Dashboards/Tutorials/YoutubeVideoPlayer';
import { Videocam } from 'react-ionicons';

const PageTitle = (props) => {
  const [state, setState] = useState({
    showingErrors: false,
    runningBulkITem: false,
    runningItem: 0,
    stopedItem: 0,
    pollingCount: 0,
    delay: 6000
  })
  const size = useScreenSize()

  function randomize(myArray) {
    return myArray[Math.floor(Math.random() * myArray.length)];
  }

  const calculateBulkItems = async () => {
    if (props?.skipBulkAutomationPolling) return;
    Axios.get('api/bulk-automations').then((response) => {
      let bulkAutomation = response?.data
      if (bulkAutomation?.length) {
        let itemRunning = bulkAutomation?.filter(item => item?.running);
        let stopAction = bulkAutomation?.filter(item => !item?.running);
        let runningAction = bulkAutomation?.filter(item => item?.running);
        setState({
          ...state,
          pollingCount: state.pollingCount + 1,
          runningBulkITem: Boolean(itemRunning.length),
          stopedItem: stopAction,
          runningItem: runningAction
        })
        if (Boolean(itemRunning.length)) {
          setTimeout(calculateBulkItems, state.delay);
        }
      }
    })
  }

  useEffect(() => {
    calculateBulkItems();
    // clearInterval(state.interval)
  }, [])

  useEffect(() => {
    if (props.delay !== state.delay) {
      calculateBulkItems();
    }
    if (props.errorsVisibilityToggle !== props.errorsVisibilityToggle) {
      setState({...state, showingErrors: props.errorsVisibilityToggle})
    }
  }, [])

  function sentenceCase(str) {
    if ((str === null) || (str === ''))
      return false;
    else
      str = str.toString();

    return str.replace(/\w\S*/g,
      (txt) => {
        return txt.charAt(0).toUpperCase() +
          txt.substr(1).toLowerCase();
      });
  }

  const {
    enablePageTitleIcon,
    enablePageTitleSubheading,
    heading,
    icon,
    subheading,
    className,
    enablePageTitleAction,
    actionTitle,
    updatingText,
    onActionClick,
    onActionUpdating,
    enableSecondaryPageTitleAction,
    enableThirdPageTitleAction,
    secondaryActionTitle,
    thirdActionTitle,
    onSecondaryActionClick,
    onThirdActionClick,
    onSecondaryActionUpdating,
    onThirdActionUpdating,
    thirdActionTooltip,
    secondaryUpdatingText,
    thirdUpdatingText,
    removeBuffer,
    enableAnalyticsPeriod,
    formErrors,
    formData,
    alertType,
    titleMessage,
    customComponent,
    progressValue,
    progressType,
    progressAnimated,
    progressText,
    showProgress,
    bulkAutomation,
    sticky,
    tutorialButton,
  } = props;

  const isDataSyncRequired = props.keyValue && props.keyValue.value === 'true';
  const isObject = o => (!!o) && (o.constructor === Object);
  let automationBulk = props.bulkAutomation;
  const isInsideShopify = ((window.location.href.includes("myshopify.com") || (window?.location?.ancestorOrigins?.[0] ? window?.location?.ancestorOrigins[0]?.includes("myshopify.com") : false)) && size.width >= 993);

  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);

  const TutorialButtonDesc = () => {
    if (!tutorialButton?.videos?.length && tutorialButton?.docs?.length) {
      return <p>Read our help documentation to get a better understanding.</p>;
    } else if (!tutorialButton?.docs?.length && tutorialButton?.videos?.length) {
      return <p>Watch our tutorial videos to get a better understanding.</p>;
    } else {
      return <p>Read our help documentation or watch our tutorial videos to get a better understanding.</p>;
    }
  };

  return (
    <>
      {/* <Sticky top=".app-header" innerZ="12" > */}

    <div className={`app-page-title ${className}`} style={(isInsideShopify && sticky) ? {position: 'sticky', top: '0px', zIndex: '10'} : sticky ? {position: 'sticky', top: '60px', zIndex: '10'} : {zIndex: '10'}}>
        <div className="page-title-wrapper">
          <div className="page-title-heading">
          <div className={cx('page-title-icon', {'d-none': !enablePageTitleIcon})}>
            <i className={icon} style={{backgroundColor: 'darkblue'}}/>
            </div>
            <div>
              {heading}
              <div
              className={cx('page-title-subheading', {'d-none': !enablePageTitleSubheading})}
              dangerouslySetInnerHTML={{__html: subheading}}
              />
              {state.runningBulkITem &&
                <div className='d-flex align-items-center'>
                <div className="progress" style={{width: "300px"}}>
                    <Progress animated={progressAnimated || true} bar color={progressType || "success"}
                      value={progressValue || 100}>

                      {progressText || +" " + state.runningBulkITem ? `Bulk Actions are running` : ''}
                    </Progress>
                  </div>
                  <div className='ml-2'>
                  <MdHelp size={20} id="Tooltip-123" color="#3ac47d"/>
                    <UncontrolledTooltip placement="right" target="Tooltip-123">
                      <div>
                      <div style={{fontSize: '18px', fontWeight: '600'}}>Running Actions</div>
                        <div>{state?.runningItem && state?.runningItem?.length ? state.runningItem?.map(item => {
                          return <p
                          style={{fontSize: '14px'}}>{sentenceCase(item?.automationType?.split('_').join(" "))}</p>
                        }) : null}
                        </div>
                      </div>
                    </UncontrolledTooltip></div>
                </div>
              }

              <div className="page-title-actions">
                {/* <Button className="btn-pill btn-shadow mr-3" onClick={notify22} color="success" id="Tooltip-123">
              <FontAwesomeIcon icon={faBatteryThreeQuarters} />
            </Button> */}
              </div>
            </div>
          </div>
          {props.children}

          <div
            className={cx('page-title-actions d-flex flex-gap-2', {
              'd-none': !enablePageTitleAction && !enableSecondaryPageTitleAction && !tutorialButton?.show,
            })}
          >
            {/*<Button color="primary" onClick={onActionClick}>
              {actionTitle}
            </Button>*/}
            {tutorialButton?.show && (
              <>
                <Button color="info" onClick={toggleShowModal} className="d-flex flex-gap-1 align-items-center">
                  {!!tutorialButton?.videos?.length && <Videocam color="#fff" />}
                  View Tutorials
                </Button>
                <Modal isOpen={showModal} toggle={toggleShowModal}>
                  <ModalHeader toggle={toggleShowModal}>Help</ModalHeader>
                  <ModalBody>
                    <h3>Need assistance?</h3>
                    <TutorialButtonDesc />
                    {!!tutorialButton?.docs?.length && (
                      <ul className='mb-4' style={{ paddingInlineStart: '20px' }}>
                        {tutorialButton.docs.map((doc, index) => {
                          return (
                            <li>
                              <a key={index} href={doc.url} target="_blank" rel="noopener noreferrer">
                                {`Docs: ${doc.title}`}
                              </a>
                            </li>
                          );
                        })}
                      </ul>
                    )}
                    {!!tutorialButton?.videos?.length && (
                      <div style={{ maxHeight: '500px', overflowY: 'scroll' }}>
                        <div>
                          {tutorialButton.videos.map((video, index) => {
                            return (
                              <div key={index} className={cx('pb-4 border-bottom', { 'pt-4': index > 0 })}>
                                <h6>Video: {video.title}</h6>
                                <YoutubeVideoPlayer
                                  url={video.url}
                                  iframeHeight="100%"
                                  divClassName="video-container"
                                  iframeClassName="responsive-iframe"
                                />
                              </div>
                            );
                          })}
                        </div>
                      </div>
                    )}
                  </ModalBody>
                  <ModalFooter>
                    <Button color="link" onClick={toggleShowModal}>
                      Cancel
                    </Button>
                  </ModalFooter>
                </Modal>
              </>
            )}
            {enableSecondaryPageTitleAction ? (
              <MySaveButton
                className="btn btn-secondary"
                onClick={onSecondaryActionClick}
                updating={onSecondaryActionUpdating ? onSecondaryActionUpdating : false}
                updatingText={secondaryUpdatingText}
                text={secondaryActionTitle}
              />
            ) : null}
            {enableSecondaryPageTitleAction ? <span> </span> : null}
            {enablePageTitleAction ? (
              <MySaveButton
                updating={onActionUpdating ? onActionUpdating : false}
                onClick={onActionClick}
                text={actionTitle}
                updatingText={updatingText}
                addBuffer={!removeBuffer}
              >
                {actionTitle}
              </MySaveButton>
            ) : null}
            {enableThirdPageTitleAction ? (
              <MySaveButton className={'ml-1'} onClick={onThirdActionClick}
                updating={onThirdActionUpdating ? onThirdActionUpdating : false}
                updatingText={thirdUpdatingText} text={thirdActionTitle} title={thirdActionTooltip}/>
            ) : null}
          </div>
          {customComponent}

        </div>

        {formErrors && state.showingErrors && (
        <div style={{marginTop: '10px'}}>
            <Alert
              type='error'
              message="Kindly get rid of these errors to submit the form."
              description={
                <ul>
                  {
                    Object?.entries(formErrors)?.map((err, i) => {
                      //return <li key={`_li2_${i}`}>{`${err[1].toString()}`}</li>
                      if (Array.isArray(err[1])) {
                        let erobj = {};
                        err[1]?.forEach((err, i) => {
                          if (typeof err === "object" && !Array.isArray(err)) {
                            Object?.entries(err)?.map((er, i) => {
                              if (Array.isArray(er[1])) {
                                er[1]?.forEach((e, i) => {
                                  if (e) {
                                  erobj = {...erobj, ...e};
                                  }
                                })
                              } else {
                              erobj = {...erobj, ...er};
                              }
                            })
                          } else {
                          erobj = {...erobj, ...err};
                          }

                        })
                        return Object?.entries(erobj)?.map((err, i) => {
                          return mayBeAddErrorMessage(isObject, err, i);
                        })
                      }
                      return mayBeAddErrorMessage(isObject, err, i);
                    })
                  }
                </ul>
              }
            afterClose={() => setState({...state, showingErrors: false})}
              banner
              closable
            showIcon/>
          </div>
        )}
        {/* {state.runningBulkITem ?
          <div style={{marginTop: '10px'}}>
            <Alert
            style={{maxWidth:'350px'}}
              type={'success'}
              message={<div> <div className='text-center' style={{fontSize:'18px', fontWeight:'600'}}>Running Actions</div>
              <div>{state.runningItem?.map(item => {return <p className='text-center' style={{fontSize:'14px'}}>{sentenceCase(item?.automationType?.replace("_"," ")) }</p>})}</div>
               </div>}
              banner
              showIcon={false}
              />

          </div> : null} */}

        {titleMessage && (
        <div style={{marginTop: '10px'}}>
            <Alert
              type={alertType ? alertType : 'info'}
              message={titleMessage}
              // description={

              // }
              // afterClose={() => setState({showingErrors: false})}
              banner
              // closable
            showIcon/>
          </div>
        )}
      </div>
      {/* // </Sticky> */}
      <>
        {
          (!props?.isAppEmbed && props?.themeSettingsList?.shopifyThemeInstallationVersion === "V2") && <ReactStrapAlert className="appstle_error_box" color="warning">
            <b>Before you continue</b> please enable the <b>Appstle's embedded block</b> on your live theme. This
            enables us to configure Subscription Widget on your product pages when products are set up for
            Subscription Plan.
                  <br/>
            <a
              href={`https://${props?.themeSettingsEntity.shop}/admin/themes/current/editor?context=apps&appEmbed=gid://shopify/OnlineStoreThemeAppEmbed/${THEME_APP_EXTENSION_HANDLE}?app_embed_uuid=${THEME_APP_EXTENSION_UUID}`}
              target="_blank"
            >
              Click Here To Activate Embedded Block
            </a>
          </ReactStrapAlert>
        }
        {
          (props?.shopPaymentInfoEntity && props?.shopPaymentInfoEntity.paymentSettings && !props?.shopPaymentInfoEntity?.paymentSettings?.supportedDigitalWallets?.includes("SHOPIFY_PAY")) &&
          <ReactStrapAlert className="appstle_error_box" color="warning">
            <h6><b>The widget will not show on your storefront:</b></h6>
            <p>
              Shopify currently requires all merchants looking to install any subscription app to use Shopify
              Payments, Stripe, Paypal express gateway, Apple Pay, Shop Pay or Authorized.Net only.
              Please&nbsp;
              <a href={`https://${props?.account?.login}/admin/settings/payments`} target="blank">click here</a> to
              update your payment gateway.
            </p>
            <p>
              You can see all qualifying criteria for Shopify subscriptions API here: <a target="_blank"
                href="https://help.shopify.com/en/manual/products/subscriptions/setup#eligibility-requirements">Eligibility
                Criteria</a>
            </p>
          </ReactStrapAlert>
        }
      </>
    </>
  );


  function mayBeAddErrorMessage(isObject, err, i) {
    if (isObject(err[1])) {
      return Object?.entries(err[1])?.map((_err, _i) => {
        return mayBeAddErrorMessage(isObject, _err, i);
      });
    } else {
      return buildErrorMessage(err, i);
    }
  }

  function buildErrorMessage(err, i) {
    if (err[1].toString().startsWith('#')) {
      return <li key={`_li2_${i}`}>{`${err[1].toString().slice(1)}`}</li>
    } else {
      return <li key={`_li2_${i}`}>{`${err[1]}`}</li>
    }
  }
};

const mapStateToProps = state => ({
  enablePageTitleIcon: state.ThemeOptions.enablePageTitleIcon,
  enablePageTitleSubheading: state.ThemeOptions.enablePageTitleSubheading,
  bulkAutomation: state.bulkAutomation.entities,
  themeSettingsEntity: state.themeSettings.entity,
  shopPaymentInfoEntity: state.shopPaymentInfo.entities,
  account: state.authentication.account,
  themeSettingsList: state.themeSettings.entity,
  isAppEmbed: state.shopInfo.isAppEmbed
});

const mapDispatchToProps = {
  getEntities
};

export default connect(mapStateToProps, mapDispatchToProps)(PageTitle);
