import React, { Fragment, useEffect, useState } from 'react';
import { connect } from 'react-redux';
import Loader from 'react-loaders';
import axios from 'axios';
import moment from 'moment';
import {
  Button,
  ButtonGroup,
  Card,
  CardBody,
  CardHeader,
  Col,
  FormText,
  Input,
  InputGroup,
  InputGroupAddon,
  Row,
  Spinner,
  Table
} from 'reactstrap';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import { faCopy } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { getEntity } from 'app/entities/customers/customer.reducer';
import { deleteEntity } from 'app/entities/subscriptions/subscription.reducer';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import { aryIannaTimeZones, convertToShopTimeZoneDate } from '../Shared/SuportedShopifyTImeZone';
import { toast } from 'react-toastify';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";

var momentTZ = require('moment-timezone');

const CustomerDetails = ({ customerEntities, getEntity, loading, deleteEntity, updating, shopInfo, ...props }) => {
  const [manageSubLoading, setManageSubLoading] = useState(false);
  const [manageSubURL, setManageSubURL] = useState('');
  const [tokenExpirationTime, setTokenExpirationTime] = useState(null);
  const [copied, setCopied] = useState(false);
  const [validContractId, setValidContractId] = useState([]);
  const [linkEmailSending, setLinkEmailSending] = useState(false);

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  useEffect(() => {
    getEntity(props.match.params.id);
  }, []);

  useEffect(() => {
    axios
      .get(`api/manage-subscription-link/${props.match.params.id}`)
      .then(res => {
        setManageSubURL(res?.data?.manageSubscriptionLink);
        setTokenExpirationTime(res?.data?.tokenExpirationTime);
      })
      .catch(error => {
        setManageSubLoading(false);
        toast.error('Something Error occured, Try after some time');
        console.log('error', error);
      });
  }, []);

  const cancelSubscription = id => {
    (async () => {
      await deleteEntity(id, props.account.login);
      await getEntity(props.match.params.id);
    })();
  };

  const manageSubscription = () => {
    setManageSubLoading(true);
    axios
      .get(`api/manage-subscription-link/${props.match.params.id}`)
      .then(res => {
        setManageSubLoading(false);
        window.open(res.data.manageSubscriptionLink, '_blank');
      })
      .catch(error => {
        setManageSubLoading(false);
        toast.error('Something Error occured, Try after some time');
        console.log('error', error);
      });
  };

  const sendMagicLinkEmail = () => {
    if (validContractId && validContractId.length > 0) {
      var firstContractId = validContractId[0].subscriptionContractId;
      setLinkEmailSending(true);

      axios
        .get(`api/subscription-contract-details/resend-email?emailSettingType=SUBSCRIPTION_MANAGEMENT_LINK&contractId=${firstContractId}`)
        .then(res => {
          setLinkEmailSending(false);
          toast.success(res?.data || 'Email triggered successfully', options);
        })
        .catch(err => {
          setLinkEmailSending(false);
          toast.error(err?.response?.data || 'Failed to trigger email', options);
        });
    } else {
      toast.error('There is no subscriptio contract available for customer', options);
    }
  };

  useEffect(() => {
    axios
      .get(`api/subscription-customers-detail/valid/${props.match.params.id}`)
      .then(res => setValidContractId(res.data))
      .catch(err => console.log(err));
  }, []);

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
          heading="Customer Details"
          icon="pe-7s-pen icon-gradient"
          tutorialButton={{
            show: true,
            videos: [
              {
                title: "Customer Section",
                url: "https://www.youtube.com/watch?v=e2k77lyHjZM",
              },
              {
                title: "Syncing Customer Emails",
                url: "https://www.youtube.com/watch?v=OR-2FVvO-q4",
              },
              {
                title: "How to Access the Customer Portal",
                url: "https://www.youtube.com/watch?v=aIslGRp83cY",
              }
            ],
            docs: [
              {
                title: "How Can I Keep Track of Customer Subscriptions",
                url: "https://intercom.help/appstle/en/articles/4928299-how-can-i-keep-track-of-customer-subscriptions"
              }
            ]
          }}
        />

        {loading ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div>
        ) : customerEntities && customerEntities?.subscriptionContracts?.edges.length > 0 ? (
          <Fragment>
            <Card className="mb-3 card-hover-shadow-2x">
              <CardHeader>
                <i className="header-icon lnr-user icon-gradient bg-plum-plate"> </i> Customer info
                <ButtonGroup size="sm" style={{ marginLeft: 'auto' }}>


                  <Button color="info" onClick={sendMagicLinkEmail}>
                    {linkEmailSending ? (
                      <>
                        <Spinner size="sm" color="light" />
                        &nbsp;{'Sending email..'}
                      </>
                    ) : (
                      'Email Magic Link to Customer'
                      )}
                  </Button>
                  <Button color="info" className="ml-2" onClick={manageSubscription}>
                    {manageSubLoading ? (
                      <>
                        <Spinner size="sm" color="light" />
                        &nbsp;{'Loading..'}
                      </>
                    ) : (
                      'View Customer Subscription Page'
                    )}
                  </Button>
                </ButtonGroup>
              </CardHeader>
              <CardBody>
                <Row>
                  <Col md-6>
                    <p>
                      <span style={{ fontWeight: 'bold' }}>First Name: </span>
                      {validContractId[0]?.customerFirstName}
                      <br />
                    </p>
                  </Col>
                  <Col md-6>
                    <span style={{ fontWeight: 'bold' }}>Last Name: </span>
                    {validContractId[0]?.customerLastName}
                  </Col>
                </Row>
                <Row>
                  <Col md-6>
                    <p>
                      <span style={{ fontWeight: 'bold' }}>Email: </span> {validContractId[0]?.customerEmail}
                      <br />
                    </p>
                  </Col>
                  <Col md-6>
                    <p><span
                          style={{fontWeight: "bold"}}>Phone: </span> {validContractId[0]?.customerPhone}<br/>
                        </p>
                  </Col>
                </Row>
                <Row>
                  <Col md={3}>
                    <span style={{ fontWeight: 'bold' }}>Customer Subscription Magic Link: </span>
                  </Col>
                  <Col md={9}>
                    <InputGroup>
                      <Input value={manageSubURL} disabled />
                      <InputGroupAddon addonType='append'>
                        <CopyToClipboard text={manageSubURL} onCopy={() => setCopied(true)}>
                          <Button color="primary">
                            {copied ? (
                              <>
                                <FontAwesomeIcon icon={faCopy} />
                                &nbsp; Copied{' '}
                              </>
                            ) : (
                              <FontAwesomeIcon icon={faCopy} />
                            )}
                          </Button>
                        </CopyToClipboard>
                      </InputGroupAddon>
                    </InputGroup>
                    <FormText>
                      {'As per the Shopify guideline, magic link will be refreshed in every 7 days. If you are providing it to your customer, please inform them that its usage would last for 7 days only. After 7 days, they would be shown a message to access customer portal via their account page. The link will refresh/change on ' +
                        tokenExpirationTime}
                    </FormText>
                  </Col>
                  <Col>
                    <div style={{ marginLeft: 'auto', marginTop: '30px', display: 'flex', justifyContent: 'flex-end' }}>
                      <MySaveButton
                        onClick={() => window.open(`https://${shopInfo.shop}/admin/customers/${props.match.params.id}`)}
                        text={' View Customer info in Shopify Dashboard'}
                        addBuffer={false}
                      >
                        View Customer info in Shopify
                      </MySaveButton>
                    </div>
                  </Col>
                </Row>
              </CardBody>
            </Card>
            <Card>
              <CardHeader>
                <i className="header-icon lnr-book icon-gradient bg-plum-plate"> </i>Subscription info
              </CardHeader>
              <CardBody>
                <Table className="mt-4 mb-0" hover>
                  <thead>
                    <tr>
                      <th>Subscription Id</th>
                      <th>Order No</th>
                      <th>Create order date</th>
                      <th>Next order date</th>
                      <th>Total Product</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {validContractId.map(item => {
                      return (
                        <tr>
                          <td>
                            <Link to={`/dashboards/subscription/${item.graphSubscriptionContractId.split('/')[4]}/detail`}>
                              {' '}
                              #{item.graphSubscriptionContractId.split('/')[4]}
                            </Link>
                          </td>
                          <td>
                            <a href={`https://${props.account.login}/admin/orders/${item?.orderId}`} target="_blank">
                              {item?.orderId}
                            </a>
                          </td>
                          <td>
                            {convertToShopTimeZoneDate(item.createdAt, shopInfo.ianaTimeZone)}
                          </td>
                          <td>
                            {item.status.toLowerCase() === 'active' ? convertToShopTimeZoneDate(item.nextBillingDate, shopInfo.ianaTimeZone) : ' - '}
                          </td>
                          <td>{JSON.parse(item.contractDetailsJSON).length}</td>
                          <td>
                            {item.status.toLowerCase() === 'active' ? (
                              <div className="badge badge-pill badge-success"> {item.status}</div>
                            ) : (
                              <div className="badge badge-pill badge-secondary"> {item.status}</div>
                            )}
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
          ''
        )}
         {/* <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
              <h6>Customer Section</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/e2k77lyHjZM" title="YouTube video player"
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>Syncing Customer Emails</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/OR-2FVvO-q4" title="YouTube video player"
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
            <div className="py-4 border-bottom">
            <h6>How to Access the Customer Portal</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/aIslGRp83cY" title="YouTube video player"
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
          </HelpPopUp> */}
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  customerEntities: state.customer.entity,
  // totalItem: customer.totalItems,
  loading: state.customer.loading,
  updating: state.subscription.updating,
  account: state.authentication.account,
  shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
  getEntity,
  deleteEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(CustomerDetails);
