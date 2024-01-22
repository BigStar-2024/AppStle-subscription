import React, { useEffect, Fragment, useState } from 'react';
import { connect } from 'react-redux';
import Loader from 'react-loaders';
import { Card, CardBody, Table, ListGroup, ListGroupItem, Button, Row, Col, Modal, ModalHeader, ModalBody, ModalFooter, Input, Label, FormFeedback } from 'reactstrap';
import Switch from 'react-switch';
import { Link } from 'react-router-dom';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { getEntities, updateEntity, updateCheckbox, deleteEntity } from 'app/entities/sms-template-setting/sms-template-setting.reducer';
import axios from 'axios';
import SweetAlert from 'sweetalert-react';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import './TableCustomisation.scss';
import IntlTelInput from 'react-intl-tel-input';
import 'react-intl-tel-input/dist/main.css';
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";

const SmsTemplateList = ({ smsEntities, getEntities, updateCheckbox, updateEntity, deleteEntity, history }) => {
  useEffect(() => {
    getEntities();
  }, []);

  let [isModalOpen, setIsModalOpen] = useState(false);
  let [inputValueForTestSms, setInputValueForTestSms] = useState('');
  let [selectedAutomationForTestSms, setSelectedAutomationForTestSms] = useState('');
  let [phoneNumberValidity, setPhoneNumberValidity] = useState(true);
  let [blurred, setBlurred] = useState(false);
  let [smsSuccessAlert, setSmsSuccessAlert] = useState(false);
  let [smsFailAlert, setSmsFailAlert] = useState(false);
  let [smsSendingProgress, setSmsSendingProgress] = useState(false);
  let [selectedCountry, setSelectedCountry] = useState('')

  const checkboxHandler = async (checked, event, id) => {
    updateCheckbox(id, !checked);
    let updatedEntity;
    smsEntities.forEach(entity => {
      if (parseInt(id) === entity.id) {
        updatedEntity = { ...entity, sendSmsDisabled: !checked };
      }
    })
    await updateEntity(updatedEntity);
    getEntities();
  };

  const sendTestSms = (templateId, emailId) => {
    if (phoneNumberValidity) {
      setSmsSendingProgress(true);
      axios.post(`/api/sms-template-settings/send-test-sms/${selectedAutomationForTestSms}?phone=${inputValueForTestSms}`, {country: selectedCountry})
        .then(response => {
          cleanupBeforeModalClose();
          setSmsSuccessAlert(true);
        })
        .catch(error => {
          cleanupBeforeModalClose();
          setSmsFailAlert(true);
        })
    }
  }

  const cleanupBeforeModalClose = () => {
    setSmsSendingProgress(false);
    setPhoneNumberValidity(true);
    setIsModalOpen(!isModalOpen);
    setBlurred(false);
    setInputValueForTestSms('');
  }

  const onBlur = (status, value, countryData, number, id) => {
    setBlurred(true);
    setPhoneNumberValidity(status);
    setInputValueForTestSms(formatNumber(number));
    setSelectedCountry(countryData?.iso2?.toUpperCase());
  }

  const onChange = (status, value, countryData, number, id) => {
    setInputValueForTestSms(formatNumber(number));
    setPhoneNumberValidity(status);
    setSelectedCountry(countryData?.iso2?.toUpperCase());
  }

  const formatNumber = (number) => {
    return number
      .split(' ').join('')
      .split('-').join('')
      .split('(').join('')
      .split(')').join('')
      .split('/').join('')
      .split('\\').join('')
      .split('+').join('')
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
          heading="SMS Template"
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5133503-manage-sms-automation' target='blank'> Enable and modify SMS Template.</a>"
          icon="pe-7s-news-paper icon-gradient"
          // enablePageTitleAction
          // actionTitle="Create New Automation"
          // onActionClick={() => {
          //   history.push('/dashboards/sms-settings/new');
          // }}
        />
        {smsEntities && smsEntities.length === 0 ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div>
        ) : (
          <Fragment>
            <Card>
            <FeatureAccessCheck
                                    hasAnyAuthorities={'enableSmsAlert'}
                                    upgradeButtonText="Upgrade to enable SMS Alert"
                                  >
              <CardBody>
                <Table className="mb-0 mt-4" hover>
                  <thead>
                  <tr>
                    <th>Status</th>
                    <th>Sms type</th>
                    {/* <th>Sent</th>
                      <th>Clicks</th>
                      <th>Revenue</th> */}
                    <th style={{ textAlign: 'right' }}>Actions</th>
                  </tr>
                  </thead>
                  <tbody>
                  {smsEntities.map(item => {
                    return (
                      <tr key={item.id}>
                        <td>
                          <Switch
                            checked={!(Boolean(item.sendSmsDisabled))}
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
                          {item?.smsSettingType?.split('_').map(word => (word[0].toUpperCase() + word.substr(1).toLowerCase())).join(' ')}
                        </td>
                        {/* <td>
                            {item?.numberOfReminderSent}
                          </td>

                          <td>
                            {item?.clicks}
                          </td>

                          <td>
                            {item?.revenue}
                          </td> */}

                        <td style={{ textAlign: 'right' }}>
                          <Link to={`/dashboards/sms-settings/${item.id}/edit`} style={{textDecoration: 'none'}}>
                            <Button
                              className=" mr-2"
                              title="Edit Automation"
                              color="info"
                              style={{ display: 'inline-flex', alignItems: 'center', padding: '5px 12px' }}
                            >
                              <i className="lnr-pencil btn-icon-wrapper mr-1" style={{ fontSize: '13px', padding: '3px 0' }} />
                              Edit
                            </Button>
                          </Link>
                          <Button
                            className="mr-2"
                            title="Test Automation"
                            color="warning"
                            style={{ display: 'inline-flex', alignItems: 'center', padding: '4px 12px' }}
                            onClick={() => {
                              setIsModalOpen(!isModalOpen);
                              setSelectedAutomationForTestSms(item?.id);
                            }}
                          >
                            <i className="lnr-cloud-check btn-icon-wrapper mr-1" style={{ fontSize: '16px', padding: '3px 0' }} />
                            Test
                          </Button>
                          {/*<Button*/}
                          {/*  className="btn-wide ml-1 btn-icon btn-icon-right deleteButton"*/}
                          {/*  outline*/}
                          {/*  size="sm"*/}
                          {/*  color="danger"*/}
                          {/*  style={{ marginRight: '0px !important', padding: '3px 11px 2px 5px' }}*/}
                          {/*  onClick={() => {*/}
                          {/*    deleteEntity(item?.id)*/}
                          {/*  }}*/}
                          {/*>*/}
                          {/*  <i*/}
                          {/*    className="pe-7s-close btn-icon-wrapper mr-1"*/}
                          {/*    style={{ fontSize: '24px', marginLeft: '0' }}*/}
                          {/*  ></i>*/}

                          {/*  Delete*/}

                          {/*</Button>*/}
                        </td>

                      </tr>
                    );
                  })}
                  </tbody>
                </Table>
              </CardBody>
              </FeatureAccessCheck>
            </Card>
            <Modal isOpen={isModalOpen} toggle={() => setIsModalOpen(!isModalOpen)} backdrop>
              <ModalHeader>Send test SMS</ModalHeader>
              <ModalBody>
                <Label>Enter Phone Number</Label>

                <IntlTelInput style={{width: '100%'}}
                              containerClassName="intl-tel-input"
                              inputClassName="form-control"
                              onPhoneNumberChange={(status, value, countryData, number, id) => onChange(status, value, countryData, number, id)}
                              onPhoneNumberBlur={(status, value, countryData, number, id) => onBlur(status, value, countryData, number, id)}
                              onPhoneNumberFocus={(status, value, countryData, number, id) => onChange(status, value, countryData, number, id)}
                />
                {(blurred && !phoneNumberValidity) && <span style={{color: '#eb3023'}}>Please Enter a valid Phone Number</span>}
                <br/>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={() => {
                  cleanupBeforeModalClose()
                }}>
                  Cancel
                </Button>
                <MySaveButton
                  onClick={() => {
                    sendTestSms(selectedAutomationForTestSms, inputValueForTestSms)
                  }}
                  text="Send Test SMS"
                  updating={smsSendingProgress}
                  updatingText={'Sending'}
                />
              </ModalFooter>
            </Modal>
            <SweetAlert
              title="Success"
              confirmButtonColor=""
              show={smsSuccessAlert}
              text=""
              type="success"
              onConfirm={() => setSmsSuccessAlert(false)}
            />
            <SweetAlert
              title="Failed"
              confirmButtonColor=""
              show={smsFailAlert}
              text="Please try again."
              type="error"
              onConfirm={() => setSmsFailAlert(false)}
            />
          </Fragment>
        )}
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  smsEntities: state.smsTemplateSetting.entities,
  loading: state.smsTemplateSetting.loading
});

const mapDispatchToProps = {
  getEntities,
  updateEntity,
  updateCheckbox,
  deleteEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(SmsTemplateList);
