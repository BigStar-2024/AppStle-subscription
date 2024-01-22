import React, {useState, useEffect, Fragment} from 'react';
import {connect, useSelector} from 'react-redux';
import axios from 'axios';
import moment from 'moment';
import {getCustomerIdentifier, redirectToAccountPage} from 'app/shared/util/customer-utils';
import {
  Container,
  Table,
  InputGroup,
  Input,
  InputGroupText,
  Label,
  FormGroup,
  Row,
  Col,
  Card,
  Collapse,
  CardHeader,
  CardFooter,
  Alert,
  CardBody,
  Badge,
  ListGroup,
  UncontrolledButtonDropdown,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  ListGroupItem,
  Pagination,
  PaginationItem,
  PaginationLink
} from 'reactstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faExternalLinkSquareAlt, faEdit, faInfoCircle, faPencilAlt} from '@fortawesome/free-solid-svg-icons';
import {Link} from 'react-router-dom';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import {getCustomerPortalEntity, getvalidCustomerEntity} from 'app/entities/customers/customer.reducer';
import {getEntity as getPortalSettings} from 'app/entities/customer-portal-settings/customer-portal-settings.reducer';
import {
  BorderColor,
  BorderColorRounded,
  CardMembership,
  Edit,
  KeyboardArrowRight,
  NearMe,
  Visibility
} from '@mui/icons-material';
import Loader from 'react-loaders';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import {DateTime} from "luxon";

const SubscriptionsList = props => {
  const {
    getCustomerPortalEntity,
    getvalidCustomerEntity,
    customerEntity,
    getPortalSettings,
    customerPortalSettingEntity,
    loading
  } = props;

  const loadingCustomer = useSelector(state => state.customer.loading);
  const customerId = getCustomerIdentifier();
  const [validContractId, setValidContractId] = useState([]);
  const [showRedirectMessage, setShowRedirectMessage] = useState(false);
  if (!customerId) {
    // redirectToAccountPage();
    setShowRedirectMessage(true);
  }
  useEffect(() => {
    getCustomerPortalEntity(customerId);
    // getvalidCustomerEntity(customerId, Shopify.shop);
    getPortalSettings(0);
    window?.parent?.postMessage("appstle_scroll_top");
  }, []);

  useEffect(() => {
    axios
      .get(`api/subscription-customers/valid/${customerId}`)
      .then(res => setValidContractId(res.data))
      .catch(err => {
        console.log(err)
        setShowRedirectMessage(true);
      });
  }, []);
  let frequencyIntervalTranslate = {
    week: customerPortalSettingEntity?.weekText,
    day: customerPortalSettingEntity?.dayText,
    month: customerPortalSettingEntity?.monthText,
    year: customerPortalSettingEntity?.yearText
  };
  const getFrequencyTitle = interval => {
    return frequencyIntervalTranslate[interval.toLowerCase(interval)] ? frequencyIntervalTranslate[interval.toLowerCase()] : interval;
  };

  const replaceStringWithSpecificWord = (textMsg, replaceWord, newReplacableWord) => {
    return textMsg.replace(replaceWord, newReplacableWord)
  }

  return (
    <>
      {loadingCustomer ? (
        <div
          style={{margin: '10% 0 0 43%', flexDirection: 'column'}}
          className="loader-wrapper d-flex justify-content-center align-items-center"
        >
          <div class="appstle_preloader appstle_loader--big"></div>
        </div>
      ) : (
        <div class="container clearfix">
          {!loadingCustomer && customerEntity && Object.keys(customerEntity).length > 0 &&
            <div class="d-flex" style={{justifyContent: "space-between", alignItems: "center", marginTop: "28px"}}>
              <h2
                className="appstle_greeting_header">{customerPortalSettingEntity?.helloNameText || `Hello`} {(appstleCustomerData?.firstName || appstleCustomerData?.lastName) ? <>
                  <span className="appstle_customer_firstName">{appstleCustomerData?.firstName}</span> <span
                  className="appstle_customer_lastName">{appstleCustomerData?.lastName}{","}</span> </> :
                <span className="appstle_greeting_text">{"There"}{","}</span>}</h2>
              {appstleCustomerData?.customerId && <div
                style={{fontSize: "20px"}}>{customerPortalSettingEntity?.customerIdText || `Customer ID`}: {appstleCustomerData?.customerId}</div>}
            </div>
          }
          {!loadingCustomer && showRedirectMessage && (
            <Card
              style={{margin: '28px 0px', borderRadius: '8px 8px 0 0', boxShadow: '0 10px 20px 0 rgb(0 0 0 / 15%)'}}>
              <CardBody>
                <center>{customerPortalSettingEntity?.expiredTokenText} <span
                  style={{color: "#13b5ea", cursor: "pointer"}}
                  onClick={() => redirectToAccountPage()}>{customerPortalSettingEntity?.portalLoginLinkText}</span>
                </center>
              </CardBody>
            </Card>
          )}
          {!loadingCustomer && customerEntity?.subscriptionContracts?.edges?.length === 0 && !showRedirectMessage &&
            <Card
              style={{margin: '28px 0px', borderRadius: '8px 8px 0 0', boxShadow: '0 10px 20px 0 rgb(0 0 0 / 15%)'}}>
              <CardBody>
                <center>{customerPortalSettingEntity?.noSubscriptionMessageV2}</center>
                :
              </CardBody>
            </Card>
          }
          {!loadingCustomer && customerEntity && customerEntity?.subscriptionContracts && customerEntity?.subscriptionContracts?.edges &&
            customerEntity?.subscriptionContracts.edges
              .sort(function (a, b) {
                if (a.node.status < b.node.status) {
                  return -1;
                }
                if (a.node.status > b.node.status) {
                  return 1;
                }
                // names must be equal
                return 0;
              })
              .map(({node}) => {
                var options = {year: 'numeric', month: 'long', day: 'numeric'};
                return (
                  <>
                    {((validContractId.some(item => item == node.id.split('/').pop()) && node.status == 'CANCELLED') || (node.status != 'CANCELLED')) && (
                      <Card style={{
                        margin: '28px 0px',
                        borderRadius: '8px 8px 0 0',
                        boxShadow: '0 10px 20px 0 rgb(0 0 0 / 15%)'
                      }}>
                        <CardHeader
                          style={{
                            background: '#242222',
                            color: '#fff',
                            padding: '3px 0',
                            borderRadius: '8px 8px 0 0',
                            justifyContent: 'center'
                          }}
                        >
                          <p className="appstle_sub-title" style={{color: '#fff'}}>
                            {' '}
                            <CardMembership/>
                            &nbsp;
                            <span className='appstle-subscription-text'>
                          {
                            replaceStringWithSpecificWord(customerPortalSettingEntity?.subscriptionNoText, "{{subscriptionFrequency}}", node?.billingPolicy?.intervalCount + ' ' + getFrequencyTitle(node?.billingPolicy?.interval))
                          }
                         </span>&nbsp;
                            <span className='appstle-subscription-id'># {node.id.split('/')[4]}</span>
                          </p>
                        </CardHeader>
                        <CardBody>
                          <Row>
                            <Col md={4} className="appstle-text-center">
                              {node.lines?.edges.map(contractItem => {
                                return (
                                  contractItem.node.productId !== null && (
                                    <img
                                      src={contractItem.node?.variantImage?.transformedSrc}
                                      alt=""
                                      style={{
                                        maxHeight: '100%',
                                        alignSelf: 'flex-start',
                                        padding: '5px',
                                        borderRadius: '2px',
                                        flexGrow: 1,
                                        maxWidth: '150px',
                                        marginBottom: '15px'
                                      }}
                                    />
                                  )
                                );
                              })}
                            </Col>
                            <Col md={8} style={{display: 'flex', flexDirection: 'column'}}>
                              {
                                <p className="appstle_sub-title  appstle_subscription_contract_title">
                                  {node.billingPolicy.intervalCount + ' ' + getFrequencyTitle(node.billingPolicy.interval)}{' '}
                                  {customerPortalSettingEntity?.subscriptionNoText}
                                  <span style={{
                                    marginLeft: '20px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center'
                                  }}>
                                    {node.status == 'ACTIVE' ? (
                                      <span className={`appstle_badge appstle_${node.status}`}>
                                        {customerPortalSettingEntity?.activeBadgeText}
                                      </span>
                                    ) : node.status == 'CANCELLED' ? (
                                      <span className={`appstle_badge appstle_${node.status}`}>
                                        {customerPortalSettingEntity?.closeBadgeText}
                                      </span>
                                    ) : node.status == 'PAUSED' ? (
                                      <span className={`appstle_badge appstle_${node.status}`}>
                                      {customerPortalSettingEntity?.pauseBadgeText}
                                      </span>
                                    ) : (
                                      ''
                                    )}
                                  </span>
                                </p>
                              }
                              {node.status == 'ACTIVE' && (
                                <p>
                                  <b>{customerPortalSettingEntity?.nextOrderText}:</b>{' '}
                                  {DateTime.fromISO(node?.nextBillingDate).toFormat(customerPortalSettingEntity?.dateFormat)}
                                </p>
                              )}
                              {(node.status == 'ACTIVE' || node.status == 'PAUSED') && (
                                <>
                                  <p>
                                    <b>{customerPortalSettingEntity?.orderFrequencyTextV2}:</b>
                                    <span>{customerPortalSettingEntity?.everyLabelTextV2}</span> {' '}
                                    {node?.billingPolicy.intervalCount} {' '}
                                    <span>{getFrequencyTitle(node.billingPolicy.interval)}</span>{' '}
                                  </p>
                                </>
                              )}
                              <b>{customerPortalSettingEntity?.productLabelTextV2}</b>
                              {node.lines?.edges.map((contractItem, index) => (
                                <div
                                  className={`appstle_font_size ${contractItem.node.productId == null && 'appstle_strike_out'}`}
                                  style={{marginTop: '5px', color: '#13b5ea'}}
                                >
                                  {contractItem.node?.title}
                                  {(contractItem?.node?.variantTitle && contractItem?.node?.variantTitle !== "-" && contractItem?.node?.variantTitle !== "Default Title") && (' - ' + contractItem?.node?.variantTitle)}
                                  {contractItem.node?.quantity > 1 ? (
                                    <span style={{marginLeft: '8px'}}>x {contractItem.node?.quantity}</span>
                                  ) : (
                                    ''
                                  )}
                                  {contractItem.node.productId == null && (
                                    <>
                                      <FontAwesomeIcon
                                        data-for={`Tooltip-${node.id.split('/')[4]}${index}`}
                                        data-tip={customerPortalSettingEntity?.productRemovedTooltip}
                                        icon={faInfoCircle}
                                        color="#465661"
                                        style={{
                                          marginLeft: '10px',
                                          cursor: 'pointer',
                                          bottom: '2px',
                                          position: 'relative'
                                        }}
                                      />

                                      <ReactTooltip
                                        html={true}
                                        id={`Tooltip-${node.id.split('/')[4]}${index}`}
                                        effect="solid"
                                        delayUpdate={500}
                                        place="right"
                                        border={true}
                                        type="info"
                                      />
                                    </>
                                  )}
                                </div>
                              ))}
                              <Link to={`/subscriptions/${node.id.split('/').pop()}/detail`}
                                    className="appstle_contract_see_more">
                                <button className="appstle_order-detail_update-button">
                                  {customerPortalSettingEntity?.seeMoreDetailsText} <KeyboardArrowRight
                                  style={{height: '18px'}}/>
                                </button>
                              </Link>
                            </Col>
                          </Row>
                        </CardBody>
                      </Card>
                    )}
                  </>
                );
              })}
        </div>
      )}
    </>
  );
};

const mapStateToProps = state => ({
  customerEntity: state.customer.entity,
  customerPortalSettingEntity: state.customerPortalSettings.entity,
  loading: state.customerPortalSettings.loading,
});

const mapDispatchToProps = {
  getCustomerPortalEntity,
  getPortalSettings
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionsList);
