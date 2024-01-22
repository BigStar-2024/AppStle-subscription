import React, { Fragment, useEffect, useState } from 'react';
import { connect } from 'react-redux';
import Loader from 'react-loaders';
import {
  Button,
  Col,
  FormFeedback,
  Input,
  Label,
  ListGroup,
  ListGroupItem,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Row,
  Table,
  Alert
} from 'reactstrap';
import Switch from 'react-switch';
import { Link } from 'react-router-dom';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { getEntities, reset, updateCheckbox, updateEntity, updateBulkEmailTemplateEntity } from 'app/entities/email-template-setting/email-template-setting.reducer';
import axios from 'axios';
import SweetAlert from 'sweetalert-react';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import EmailAdminNotification from './EmailAdminNotification';
import Select from "react-select";
import swal from 'sweetalert';

const BulkEmails = ({ emailTemplateSettingEntities, getEntities, updateCheckbox, updateEntity, reset, shopInfo, updateBulkEmailTemplateEntity }) => {
  useEffect(() => {
    getEntities();
    reset();
  }, []);

  let [isModalOpen, setIsModalOpen] = useState(false);
  let [isGlobalSettingsOpen, setIsGlobalSettingsOpen] = useState(false);

  let [inputValueForTestEmailId, setInputValueForTestEmailId] = useState('');
  let [selectedTemplateForTestEmail, setSelectedTemplateForTestEmail] = useState('');
  let [emailValidity, setEmailValidity] = useState(true);
  let [blurred, setBlurred] = useState(false);
  let [emailSuccessAlert, setEmailSuccessAlert] = useState(false);
  let [emailFailAlert, setEmailFailAlert] = useState(false);
  let [emailSendingProgress, setEmailSendingProgress] = useState(false);
  let [updateBulkEmailSettingProgress, setUpdateBulkEmailSettingProgress] = useState(false);
  let [propertyValueValidity, setPropertyValueValidity] = useState({isValid: true, message: ""});
  let [propertyValueBlurred, setPropertyValueBlurred] = useState(false);
  let [bulkEmailSending, setBulkEmailSending] = useState(false);

  let [propertyName, setPropertyName] = useState({
    label: 'From Email',
    value: 'fromEmail'
  });
  let [propertyValue, setPropertyValue] = useState('');
  const propertyList = [
    {
      label: 'From Email',
      value: 'fromEmail'
    },
    {
      label: 'Logo URL',
      value: 'logo'
    },
    {
      label: 'Logo Height(px)',
      value: 'logoHeight'
    },
    {
      label: 'Logo Width(px)',
      value: 'logoWidth'
    },
    {
      label: 'Logo Alignment',
      value: 'logoAlignment'
    }
  ];
  

  const checkboxHandler = async (checked, event, id) => {
    updateCheckbox(id, !checked);
    let updatedEntity;
    emailTemplateSettingEntities.forEach(entity => {
      if (parseInt(id) === entity.id) {
        updatedEntity = { ...entity, sendEmailDisabled: !checked };
      }
    });
    await updateEntity(updatedEntity);
    getEntities();
  };

  const sendTestEmail = (templateId, emailId) => {
    if (checkEmailValidity(emailId)) {
      setEmailSendingProgress(true);
      axios
        .post(`/api/email-template-settings/send-test-mail/${templateId}?emailId=${emailId}`)
        .then(response => {
          cleanupBeforeModalClose();
          setEmailSuccessAlert(true);
        })
        .catch(error => {
          cleanupBeforeModalClose();
          setEmailFailAlert(true);
        });
    }
  };

  const sendBulkEmails  = (templateId) => {
    swal({
      text: "Are you sure want to send bulk emails?",
      icon: "warning",
      buttons: [("Cancel"), ("Confirm")],
      dangerMode: true
    }).then(value => {
      if (value) {
        setBulkEmailSending(true);
        axios.post(`/api/email-template-settings/send-bulk-mails/${templateId}`)
        .then(response => {
          setBulkEmailSending(false);
          setEmailSuccessAlert(true);
        })
        .catch(error => {
          setBulkEmailSending(false);
          setEmailFailAlert(true);
        });
      } 
    });
  };

  const updateEmailBulkSetting = () => {
    if (checkPropertyValueValidation(propertyValue)) {
      setUpdateBulkEmailSettingProgress(true);
      updateBulkEmailTemplateEntity(propertyName.value, propertyValue)
        .then(response => {
          cleanupBeforeEmailSettingModalClose();
        })
        .catch(error => {
          setUpdateBulkEmailSettingProgress(false);
        });
    }
  };

  const cleanupBeforeModalClose = () => {
    setEmailSendingProgress(false);
    setEmailValidity(true);
    setIsModalOpen(!isModalOpen);
    setBlurred(false);
    setInputValueForTestEmailId('');
  };

  const cleanupBeforeEmailSettingModalClose = () => {
    setUpdateBulkEmailSettingProgress(false);
    setPropertyName({ label: 'From Email', value: 'fromEmail' });
    setPropertyValue('');
    setIsGlobalSettingsOpen(!isGlobalSettingsOpen)
  };

  const checkEmailValidity = emailId => {
    if (
      /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
        emailId
      )
    ) {
      setEmailValidity(true);
      return true;
    } else {
      setEmailValidity(false);
      return false;
    }
  };

  const checkPropertyValueValidation = value => {
    setPropertyValueValidity({isValid: true, message: ""});
    
    if (!value) {
      setPropertyValueValidity({isValid: false, message: "Please enter property value."});
      return false;
    }

    if (propertyName?.value == "fromEmail" && !isValidEmail(value)) {
      setPropertyValueValidity({isValid: false, message: "Email is not valid."});
      return false;
    }
  }

  function isValidEmail(email) {
    if (isValid(email)) {
      return true;
    }

    let isMatchEmail = extractEmail(email);
    if (isMatchEmail) {
      return isValid(isMatchEmail);
    }

    return false;

    // Function to validate a standard email address
    function isValid(email) {
      var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
      return emailPattern.test(email);
    }

    function extractEmail(inputString) {
      var emailMatch = inputString.match(/<([^>]+)>/); // Match text within angle brackets
      if (emailMatch && emailMatch.length >= 2) {
        var email = emailMatch[1]; // Extract the email address
        return email?.trim();
      }
      return null; // If no valid email address is found
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
        transitionLeave={false}
      >
        <PageTitle
          heading="Bulk Emails"
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5129455-email-template-customization' target='blank'> Need help? Follow these instruction to customize your email.</a>"
          icon="pe-7s-news-paper icon-gradient"
          sticky={false}
          tutorialButton={{
            show: true,
            docs: [
              {
                title: "How to Customize Emails",
                url: "https://intercom.help/appstle/en/articles/5129455-email-template-customization"
              }
            ]
          }}
        />
        {emailTemplateSettingEntities && emailTemplateSettingEntities.length === 0 ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div>
        ) : (
          <Fragment>
            <Row className="align-items-center" style={{ marginLeft: '0px' }}>
              <Col>
                <div>
                  <h4>Welcome to Bulk Email template settings</h4>
                  <p className="text-muted">
                    This section focuses on the requirements when you want to send your own personalized email to your subscribers. Such as for your sale offers, 
                    to promote your other subscription products, and remind your paused subscribers to activate their subscriptions, etc. Click on the status buttons
                    to enable/disable the emails and click on "edit" to modify the content.
                  </p>
                  <p className="text-muted">
                    If you have any questions at any time, just reach out to us on{' '}
                    <a href="javascript:window.Intercom('showNewMessage')">our chat widget</a>
                  </p>
                  <Alert color="info" className="appstle_error_box">
                    {shopInfo.klaviyoApiKey && shopInfo.klaviyoPublicApiKey ? (
                      <p>Emails will be sent using Klaviyo email service</p>
                    ) : shopInfo.mailchimpApiKey ? (
                      <p>Emails will be sent using Mailchimp email service</p>
                    ) : (
                      <p>Emails will be sent using Appstle email service</p>
                    )}
                  </Alert>
                </div>
              </Col>
            </Row>
            <ListGroup>
              <ListGroupItem>
                <Row className="align-items-center" style={{ margin: '0px' }}>
                  <Col>
                    <div className="d-flex justify-content-end">
                      <button
                        title={
                          'The settings that you are defining in Universal Email Settings will automatically be applicable to all of the email templates so you don’t need to make individual edits to individual email templates.'
                        }
                        className="btn btn-primary"
                        onClick={() => setIsGlobalSettingsOpen(!isGlobalSettingsOpen)}
                      >
                        Universal Email Settings
                      </button>
                    </div>
                  </Col>
                </Row>
                <Table className="mb-0 mt-4" hover>
                  <thead>
                    <tr>
                      <th>Status</th>
                      <th>Name</th>
                      <th style={{ textAlign: 'center' }}>Action</th>
                      <th style={{ textAlign: 'center' }}>Admin Notification</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {emailTemplateSettingEntities.map(item => {
                      if (String(item.emailSettingType).startsWith('BULK_')) {
                        return (
                          <tr key={item.id}>
                            <td>
                              <Switch
                                checked={!Boolean(item.sendEmailDisabled)}
                                onColor="#3ac47d"
                                onChange={checkboxHandler}
                                handleDiameter={20}
                                boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                                activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                                height={17}
                                width={36}
                                className="mt-2"
                                id={String(item.id)}
                              />
                            </td>
  
                            <td>
                              <Link to={`/dashboards/bulk-emails/${item.id}/edit`}>
                                {item.emailSettingType
                                  .split('_')
                                  .map(word => word[0].toUpperCase() + word.substr(1).toLowerCase())
                                  .join(' ')}
                              </Link>
                            </td>
  
                            <td style={{ textAlign: 'center' }}>
                              <Link to={`/dashboards/bulk-emails/${item.id}/edit`}>
                                <Button
                                  className=" mr-2  btn-pill"
                                  title="Edit Email Template"
                                  color="info"
                                  style={{
                                    display: 'inline-flex',
                                    alignItems: 'center',
                                    padding: '4px 12px'
                                  }}
                                >
                                  <i className="lnr-pencil btn-icon-wrapper mr-1" style={{ fontSize: '13px', padding: '3px 0' }} />
                                  Edit
                                </Button>
                              </Link>
                              <Button
                                className="btn-pill mr-2"
                                title="Test Email Template"
                                color="warning"
                                style={{
                                  display: 'inline-flex',
                                  alignItems: 'center',
                                  padding: '4px 12px'
                                }}
                                onClick={() => {
                                  setIsModalOpen(!isModalOpen);
                                  setSelectedTemplateForTestEmail(item?.id);
                                }}
                              >
                                <i className="lnr-cloud-check btn-icon-wrapper mr-1" style={{ fontSize: '16px', padding: '3px 0' }} />
                                Test Email
                              </Button>
                            </td>
                            <td style={{ textAlign: 'center' }}>
                              <EmailAdminNotification item={item} getEmailEntities={getEntities} />
                            </td>
                            <td> 
                              <button
                                disabled={bulkEmailSending}
                                className="btn btn-primary"
                                title="Send Bulk"
                                onClick={() => {
                                  sendBulkEmails(item?.id);
                                }}
                                > Bulk Send
                              </button>
                            </td>
                          </tr>
                        );
                      }
                      
                    })}
                  </tbody>
                </Table>
              </ListGroupItem>
            </ListGroup>
            <Modal isOpen={isModalOpen} toggle={() => setIsModalOpen(!isModalOpen)} backdrop>
              <ModalHeader>Send test Email</ModalHeader>
              <ModalBody>
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
                <br />
              </ModalBody>
              <ModalFooter>
                <Button
                  color="secondary"
                  onClick={() => {
                    cleanupBeforeModalClose();
                  }}
                >
                  Cancel
                </Button>
                <MySaveButton
                  onClick={() => {
                    sendTestEmail(selectedTemplateForTestEmail, inputValueForTestEmailId);
                  }}
                  text="Send Test Email"
                  updating={emailSendingProgress}
                  updatingText={'Sending'}
                />
              </ModalFooter>
            </Modal>

            <Modal isOpen={isGlobalSettingsOpen} toggle={() => setIsGlobalSettingsOpen(!isGlobalSettingsOpen)} backdrop>
              <ModalHeader>Universal Email Settings</ModalHeader>
              <ModalBody>
                <div>
                  <Label>Property Name</Label>
                  <Select
                    value={propertyName}
                    options={propertyList}
                    onChange={e => {
                      setPropertyName(e);
                    }}
                  />
                </div>
                <div className="pt-3">
                  <Label>Property Value</Label>
                  <Input
                    type="email"
                    placeholder="Please enter property value"
                    invalid={!propertyValueValidity?.isValid}
                    onBlur={event => {
                      setPropertyValue(event.target.value);
                      checkPropertyValueValidation(event.target.value);
                      setPropertyValueBlurred(true);
                    }}
                    onInput={event => {
                      if (propertyValueBlurred) {
                        setPropertyValue(event.target.value);
                        checkPropertyValueValidation(event.target.value);
                      }
                    }}
                  />
                  <FormFeedback>{propertyValueValidity?.message}</FormFeedback>
                </div>
              </ModalBody>
              <ModalFooter>
                <Button
                  color="secondary"
                  onClick={() => {
                    setIsGlobalSettingsOpen(!isGlobalSettingsOpen);
                  }}
                >
                  Cancel
                </Button>
                <MySaveButton
                  onClick={() => {
                    updateEmailBulkSetting();
                  }}
                  text="Update"
                  updating={updateBulkEmailSettingProgress}
                  updatingText={'Updating'}
                />
              </ModalFooter>
            </Modal>

            <SweetAlert
              title="Success"
              confirmButtonColor=""
              show={emailSuccessAlert}
              text=""
              type="success"
              onConfirm={() => setEmailSuccessAlert(false)}
            />
            <SweetAlert
              title="Failed"
              confirmButtonColor=""
              show={emailFailAlert}
              text="Please try again."
              type="error"
              onConfirm={() => setEmailFailAlert(false)}
            />
          </Fragment>
        )}
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  emailTemplateSettingEntities: state.emailTemplateSetting.entities,
  loading: state.emailTemplateSetting.loading,
  shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
  getEntities,
  updateEntity,
  updateCheckbox,
  reset,
  updateBulkEmailTemplateEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(BulkEmails);
