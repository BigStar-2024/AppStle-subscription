import React, {useState, useEffect, Fragment, useCallback} from 'react';
import {connect, useSelector} from 'react-redux';
import Loader from 'react-loaders';
import axios from 'axios';
import {Help} from "@mui/icons-material";
import {CloudDownloadRounded} from "@mui/icons-material";

import {
  Button,
  Table,
  Input,
  Label,
  Row,
  Col,
  Card,
  CardBody,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  FormFeedback
} from 'reactstrap';
import Pagination from "react-js-pagination";
import {Link} from "react-router-dom";
import {JhiItemCount} from "react-jhipster";
import {useHistory, useLocation, useParams} from 'react-router';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  getEntities,
  exportSubscriptionContract
} from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import { getSellingPlans, getEntities as subGrpEntities } from 'app/entities/subscription-group/subscription-group.reducer'
import SubscriptionFilter from './SubscriptionFilter';
import FilterAction from "./FilterAction";
import BlockUi from '@availity/block-ui';
import {toast} from 'react-toastify';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import SweetAlert from 'sweetalert-react';
import queryString from 'query-string';
import Chip from "@mui/material/Chip";
import _ from "lodash";
import CustomHtmlToolTip from '../SubscriptionGroups/CustomHtmlToolTip';
import { convertToShopTimeZoneDate } from '../Shared/SuportedShopifyTImeZone';

const options = {
  autoClose: 500,
  position: toast.POSITION.BOTTOM_CENTER
};

const SubscriptionList = (props) => {
  const {subscriptionEntities, getEntities,subGrpEntities,account, loading, subscriptionGrpPlanEntities, subscriptionGrpEntities,loadingSellingPlans, exportSubscriptionContract, exporting, shopInfo, getSellingPlans} = props;
  const location = useLocation();
  const history = useHistory()
  const [sortByType, setSortByType] = useState('id');
  const [activePage, setActivePage] = useState(1);
  const [totalRowData, setTotalRowData] = useState(0);
  const [hasData, setHasData] = useState(false);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [sortByDir, setSortByDir] = useState('desc');
  const [failMessage, setFailMessage]= useState('')
  const sortEntities = [
    {key: 'subscription_contract_id', title: 'Subscription Id'},
    {key: 'customer_name', title: 'Customer'},
    {key: 'billing_policy_interval_count', title: 'Subscription Frequency'},
    {key: 'order_name', title: 'Order No'},
    {key: 'created_at', title: 'Create Order date'},
    {key: 'next_billing_date', title: 'Next order date'},
    {key: 'status', title: 'Status'},
    {key: 'currency_code', title:'Currency'},
  ];
  const sortTypes = [
    {key: 'desc', title: 'Desc'},
    {key: 'asc', title: 'Asc'},
  ]
  const {totalItems} = useSelector(state => state.subscriptionContractDetails);
  let initFilter = FilterAction.getFilterObject();
  let [isModalOpen, setIsModalOpen] = useState(false);
  let [emailValidity, setEmailValidity] = useState(true);
  let [emailSendingProgress, setEmailSendingProgress] = useState(false);
  let [blurred, setBlurred] = useState(false);
  let [inputValueForTestEmailId, setInputValueForTestEmailId] = useState('');
  let [emailSuccessAlert, setEmailSuccessAlert] = useState(false);
  let [emailFailAlert, setEmailFailAlert] = useState(false);
  const [filterVal, setFilterVal] = useState(initFilter);
  const [allowExport, setAllowExport] = useState(false);
  const handleGridData = () => {
    let sortField = `${sortByType},${sortByDir}`;
    if (sortByType === 'billingPolicyIntervalCount') {
      sortField = `billingPolicyInterval,${sortByType},${sortByDir}`;
    }
    if (sortByType === 'nextBillingDate') {
      sortField = `${sortByType},id,${sortByDir}`;
    }
    getEntities(activePage - 1, itemsPerPage, sortField, filterVal);
  }
  useEffect(() => {
    handleGridData();
  }, [activePage, sortByType, sortByDir, filterVal])

  useEffect(() => {
    setTotalRowData(totalItems);
    if (totalItems > 0) {
      setHasData(true);
    }
  }, [subscriptionEntities]);
  const handlePagination = activePage => {
    setActivePage(activePage);
  };

  const onApplyFilter = (filter) => {
    setFilterVal(filter)
    setActivePage(1)
    FilterAction.setFilterObject(filter);
  }

  useEffect(() => {
    if (!location.state?.from) {
      const filterStatus = queryString.stringify(filterVal);
      props.history.push(`${props.location.pathname}?page=${activePage}&size=${itemsPerPage}${filterStatus ? `&${filterStatus}` : ""}`);
    } else {
      const filterStatus = queryString.stringify(filterVal);
      setActivePage(1);
      props.history.push(`${props.location.pathname}?page=${activePage}&size=${itemsPerPage}${filterStatus ? `&${filterStatus}` : ""}`);
    }
  }, [activePage, itemsPerPage, filterVal]);

  useEffect(() => {
    if (totalRowData) {
      setAllowExport(true)
    }
  }, [totalRowData])

  useEffect(() => {
    getSellingPlans();
    subGrpEntities();
    if(!location.state?.from) {
      setFilterVal({})
    }
  }, [])

  const intitialState = {
    search: ''
  };
  const hasFilter = () => {
    return (_.size(filterVal) > 0) || hasData;
  }
  const cleanupBeforeModalClose = () => {
    setEmailSendingProgress(false);
    setEmailValidity(true);
    setIsModalOpen(!isModalOpen);
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

  const sendFilteredSubscriptionMail = (emailId) => {
    if (checkEmailValidity(emailId)) {
      setEmailSendingProgress(true);
      axios.get(`api/subscription-contract-details/export/all`, {
        params: {
          emailId: emailId,
          fromCreatedDate: filterVal.fromCreatedDate ? filterVal.fromCreatedDate : undefined,
          toCreatedDate: filterVal.toCreatedDate ? filterVal.toCreatedDate : undefined,
          fromNextDate: filterVal.fromNextDate ? filterVal.fromNextDate : undefined,
          toNextDate: filterVal.toNextDate ? filterVal.toNextDate : undefined,
          status: filterVal.status ? filterVal.status : undefined,
          planType: filterVal.planType ? filterVal.planType : undefined,
          recordType: filterVal.recordType ? filterVal.recordType : undefined,
          subscriptionContractId: filterVal.subscriptionContractId ? filterVal.subscriptionContractId : undefined,
          customerName: filterVal.customerName ? filterVal.customerName : undefined,
          orderName: filterVal.orderName ? filterVal.orderName : undefined,
          billingPolicyIntervalCount: filterVal.billingPolicyIntervalCount ? filterVal.billingPolicyIntervalCount : undefined,
          billingPolicyInterval: filterVal.billingPolicyInterval ? filterVal.billingPolicyInterval : undefined,
          sellingPlanIds: filterVal.sellingPlanIds || undefined,
          variantId: filterVal.variantId || undefined,
          productId: filterVal.productId || undefined,
          minOrderAmount: filterVal.minOrderAmount || undefined,
          maxOrderAmount: filterVal.maxOrderAmount || undefined
        },
      }).then(response => {
        cleanupBeforeModalClose();
        setEmailSuccessAlert(true);
      })
        .catch(error => {
          cleanupBeforeModalClose();
          setEmailFailAlert(true);
          setFailMessage(error.response.data.message ? error.response.data.message : 'Something bad happened. Please try again.');
        })
    }
  }

  const handleChipFilterDelete = (filterKey) => {
    const filter = {...filterVal};
    filter[filterKey] = undefined;
    setFilterVal(filter);
    setActivePage(1);
  }

  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);

  return (
    <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}>
        <PageTitle heading="Subscriptions"
                   subheading="Quick view of all subscriptions."
                   icon="pe-7s-news-paper icon-gradient"
                   enablePageTitleAction
                   actionTitle="Export"
                   updatingText="Exporting"
                   onActionClick={() => {
                     setIsModalOpen(!isModalOpen);
                   }}
                   onActionUpdating={exporting}
                   sticky={true}
                   tutorialButton={{
                    show: true,
                    videos: [
                      {
                        title: "Create Subscription",
                        url: "https://www.youtube.com/watch?v=4ViJlPmWSSA",
                      },
                      {
                        title: "Subscriptions and Filtering",
                        url: "https://www.youtube.com/watch?v=QDEP-0jLWek",
                      },
                      {
                        title: "Export Subscription Data",
                        url: "https://www.youtube.com/watch?v=AvohNGVCX6E"
                      }
                    ],
                    docs:[
                      {
                        title: "Subscription Management",
                        url: "https://intercom.help/appstle/en/collections/2776380-subscription-management"
                      }
                    ]
                  }}
        />
          <Modal isOpen={isModalOpen} toggle={() => setIsModalOpen(!isModalOpen)} backdrop>
          <ModalHeader>Export Subscription Contract List</ModalHeader>
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
                A minimum of 1 and maximum 1000 subscriptions can be exported at a time. Please use subscription filter criteria to reduce/increase the search results.
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
                sendFilteredSubscriptionMail(inputValueForTestEmailId)
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
          text="Export may take time based on the number of subscriptions in your store. Rest assured, once it's processed, it will be emailed to you."
          type="success"
          onConfirm={() => setEmailSuccessAlert(false)}
        />
        <SweetAlert
          title="Failed"
          confirmButtonColor=""
          show={emailFailAlert}
          text={failMessage}
          type="error"
          onConfirm={() => setEmailFailAlert(false)}
        />
        {
          (loading && !hasFilter()) ?
            (<div style={{margin: "10% 0 0 43%"}}
                  className="loader-wrapper d-flex justify-content-center align-items-center">
              <Loader type="line-scale"/>
            </div>) :

            (subscriptionEntities && (subscriptionEntities?.length > 0 || hasFilter())?
                <Fragment>
                  {/* <Alert className="mb-2" color="warning">
                    <h6>Need assistance?</h6>
                    <p>Watch our tutorial videos or read our help documentation to get a better understanding.</p>
                    <span>
                      <Button color="warning" onClick={toggleShowModal}>Video Tutorials</Button>
                      <a
                        href='https://intercom.help/appstle/en/collections/2776380-subscription-management'
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
                          <h6>Create Subscription</h6>
                          <YoutubeVideoPlayer
                            url="https://www.youtube.com/watch?v=4ViJlPmWSSA"
                            iframeHeight="100%"
                            divClassName="video-container"
                            iframeClassName="responsive-iframe"
                          />
                        </div>
                        <div className="py-4 border-bottom">
                          <h6>Subscriptions and Filtering</h6>
                          <YoutubeVideoPlayer
                            url="https://www.youtube.com/watch?v=QDEP-0jLWek"
                            iframeHeight="100%"
                            divClassName="video-container"
                            iframeClassName="responsive-iframe"
                          />
                        </div>
                        <div className="py-4 border-bottom">
                          <h6>Export Subscription Data</h6>
                          <YoutubeVideoPlayer
                            url="https://www.youtube.com/watch?v=AvohNGVCX6E"
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
                  <Card className="main-card">
                    <CardBody>
                      <Row>
                        <Col sm={12} md={12} lg={6}>
                            {
                              Object.keys(filterVal).map((keys, index) => {
                                return (
                                  <Fragment key={index}>
                                    {
                                      (filterVal[keys] && filterVal[keys] !== undefined && filterVal[keys] !== null) ? <Chip
                                        size="small"
                                        label={
                                          filterVal[keys] ?
                                            <>
                                              {
                                                (keys === "fromCreatedDate" || keys === "toCreatedDate" || keys === "fromNextDate" || keys === "toNextDate") ? <><b>{_.startCase(keys)}</b>:<span>{new Date(filterVal[keys]).toDateString()}</span></> :
                                                <><b>{_.startCase(keys)}</b>:<span>{filterVal[keys]}</span></>
                                              }
                                            </>
                                            : ""
                                        }
                                        // onDelete={() => handleChipFilterDelete(keys)}
                                        clickable
                                        variant="outlined"
                                        color="primary"
                                        style={{margin: "2px 2px"}}
                                      /> : ""
                                    }
                                  </Fragment>
                                )
                              })
                            }
                        </Col>
                        <Col sm={12} md={12} lg={6}>
                          <div style={{display: 'flex', alignItems: 'center', justifyContent: 'flex-end'}}>
                            <b>Sort: &nbsp;&nbsp;</b>
                            <div style={{width: "31%"}}>
                              <Input type="select" name="sortName" defaultValue={sortByType}
                                     onChange={(e) => setSortByType(e.target.value)}>
                                {
                                  sortEntities.map(item => <option key={item.key} value={item.key}>{item.title}</option>)
                                }
                              </Input>
                            </div>

                            &nbsp; &nbsp;
                            <div style={{width: "20%"}}>
                              <Input type="select" name="sortDir" defaultValue={sortByDir}
                                     onChange={(e) => setSortByDir(e.target.value)}>
                                {
                                  sortTypes.map(item => <option key={item.key} value={item.key}>{item.title}</option>)
                                }
                              </Input>
                            </div>
                            &nbsp; &nbsp;
                            <div>
                              <SubscriptionFilter
                                subscriptionGrpPlanEntities = {subscriptionGrpPlanEntities}
                                filterVal={filterVal}
                                onApply={onApplyFilter}
                              />
                            </div>
                          </div>
                        </Col>
                      </Row>

                      <BlockUi
                        tag="div"
                        blocking={loading}
                        loader={<Loader active type="line-scale"/>}
                      >
                        <div>

                          <Table className="mb-0 mt-4 table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl" hover>
                            <thead>
                            <tr>
                              <th>Subscription Id</th>
                              <th>Customer</th>
                              <th>Frequency</th>
                              <th>Plan Type</th>
                              <th>Order No</th>
                              <th>Create Order date</th>
                              <th>Next order date</th>
                              <th className="text-right">Total Amount
                                <CustomHtmlToolTip
                                  interactive
                                  placement="right"
                                  arrow
                                  enterDelay={0}
                                  className="ml-1"
                                  title={
                                      <div style={{padding: '8px'}}>
                                          <div
                                              style={{textAlign: 'center'}}>
                                              Shipping(Delivery) amount not included in total.
                                          </div>
                                      </div>
                                  }
                                >
                                    <Help style={{fontSize: '1rem'}}/>
                                </CustomHtmlToolTip>
                              </th>
                              <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                              subscriptionEntities?.map((item) => {
                                return (
                                  <tr key={item?.id}>
                                    <td><Link
                                      to={{
                                        pathname: `/dashboards/subscription/${item?.subscriptionContractId}/detail`,
                                        state: {
                                          from: `${location.search}`,
                                          pathname: location?.pathname
                                        }
                                      }}> #{item?.subscriptionContractId}</Link>
                                      <CustomHtmlToolTip
                                        interactive
                                        placement="right"
                                        arrow
                                        enterDelay={0}
                                        className="ml-1"
                                        title={
                                            <div style={{padding: '8px'}}>
                                                <div
                                                    style={{textAlign: 'center'}}>
                                                    <b>Products Included</b>
                                                </div>
                                                <hr style={{borderColor: '#fff'}}/>
                                                {(JSON.parse(item?.contractDetailsJSON)?.map((item, index) => {
                                                  return <p>{index + 1}. {item?.title} {item?.variantTitle}</p>
                                                }))}
                                            </div>
                                        }
                                      >
                                          <Help style={{fontSize: '1rem'}}/>
                                      </CustomHtmlToolTip>
                                      {
                                        item?.importedId != null &&
                                        <CustomHtmlToolTip
                                        interactive
                                        placement="right"
                                        popper
                                        enterDelay={0}
                                        className="ml-1"
                                        title={
                                            <div style={{padding: '8px'}}>
                                                <div
                                                    style={{textAlign: 'center'}}>
                                                    <b>Imported</b>
                                                </div>
                                            </div>
                                        }
                                         >
                                          <CloudDownloadRounded style={{fontSize: '1rem'}}/>
                                        </CustomHtmlToolTip>
                                      }

                                    </td>
                                    <td><a style={{color: "#545cd8", cursor: "pointer"}} onClick={() =>{window.top.location.href = `https://${shopInfo.shop}/admin/customers/${item.customerId}`}} target={'_blank'}>{item?.customerName}</a> <br></br>{item?.customerEmail} </td>
                                    <td>{item?.deliveryPolicyIntervalCount + " " + item?.deliveryPolicyInterval} </td>
                                    <td>{item?.billingPolicyIntervalCount == item?.deliveryPolicyIntervalCount ? 'Pay As You Go Plan' : 'Prepaid Plan'} </td>
                                    <td><a href={`https://${item?.shop}/admin/orders/${item?.orderId}`}
                                           target="_blank">{item?.orderName}</a></td>
                                    <td>
                                      {convertToShopTimeZoneDate(item?.createdAt, shopInfo.ianaTimeZone)}
                                    </td>
                                    <td>{(item?.status?.toLowerCase() === "active") ? (convertToShopTimeZoneDate(item?.nextBillingDate, shopInfo.ianaTimeZone)) : " - "}</td>
                                     <td className="text-right">
                                      {+parseFloat(JSON.parse(item?.contractDetailsJSON)?.reduce((total, item) => {
                                          if(item?.discountedPrice) {
                                            return total = total + parseFloat(item?.discountedPrice);
                                          } else {
                                            return total = total + (parseFloat(item?.currentPrice) * parseInt(item?.quantity));
                                          }
                                        }, 0)).toFixed(2)
                                      } {item?.currencyCode}
                                    </td>
                                    <td>{item?.status?.toLowerCase() === "active" ?
                                      <div className="badge badge-pill badge-success"> {item?.status}</div>
                                      : <div className="badge badge-pill badge-secondary"> {item?.status}</div>
                                    }
                                    </td>
                                  </tr>
                                );
                              })
                            }
                            </tbody>
                          </Table>
                          {
                            subscriptionEntities?.length == 0 &&
                            <div className="text-center m-3">No data available</div>
                          }
                          <Row style={{textAlign: 'center'}}>

                            <Col md={12}>
                              <br/>
                              <div style={{display: 'flex', justifyContent: 'center'}}>
                                <Pagination
                                  activePage={activePage}
                                  itemsCountPerPage={itemsPerPage}
                                  totalItemsCount={totalRowData}
                                  // pageRangeDisplayed={5}
                                  onChange={handlePagination}
                                />
                              </div>
                              <JhiItemCount page={activePage} total={totalRowData} itemsPerPage={itemsPerPage}/>
                            </Col>
                          </Row>
                        </div>
                      </BlockUi>
                    </CardBody>
                  </Card>

                </Fragment>
                :
                (subscriptionGrpEntities && subscriptionGrpEntities?.length === 0 ? <Fragment>
                  <Card className="main-card">
                    <CardBody>

                      <div className='container-fluid p-5'>
                      <div>
                        <h4 className='text-uppercase'>
                      {account?.firstName}
                        </h4>
                        </div>
                        <Row>
                          <Col md={5} lg={5} className='mt-4'>
                            <div>
                            <h5 className='mt-4'>Ready to start selling subscriptions?</h5>
                            <span className='mt-4'>Launch your first subscription product with just a few easy steps</span>
                            </div>
                            <div>
                            <Button color="primary" className='mt-4'onClick={() => {
                              history.push(`/dashboards/subscription-plan`);
            }}>
              I'm ready, let's go!
            </Button>
                            </div>
                          </Col>
                         <Col md={1} lg={1}> <div style={{borderLeft: "1px solid #6c757d91",height: '100px', width:'1px'}}></div>
                          <span style={{position:"relative", left:'-6px', color:'#6c757dd1', fontSize:'13px'}}>Or</span>
                          <div style={{borderLeft: "1px solid #6c757d91",height: '100px' ,width:'1px'}}></div></Col>
                          <Col md={5} lg={5} className='mt-4'><div>
                            <h5 className='mt-4'>Already selling subscriptions?</h5>
                            <span className='mt-4'>We offer merchants a seamsless, full-service from any-where</span>
                            </div>
                            <div className='d-flex align-items-center mt-4'>
                            <Button color="primary" onClick={() => {
              window.open('https://calendly.com/appstle/appstle-migration-discussion')
            }}>
              Talk to a migration specialist
            </Button>
            <a href='#' className='ml-3'>Learn more</a>
                            </div></Col>
                        </Row>
                      </div>
                      <div className='pl-5'> <a href="javascript:window.Intercom('showNewMessage')">Need anything? Talk to us!</a></div>
                    </CardBody>
                  </Card>
                </Fragment>: <Row className="align-items-center welcome-page">
                  <Col sm='5'>
                    <div>
                      <h4>Welcome to Subscriptions</h4>
                      <p className='text-muted'>
                        This page will give you the complete snapshot of all
                        the subscriptions that are tied to your store and key details such as their current status,
                        customer information, important dates, etc.
                        If you want to learn more about a particular subscription, click on the subscription ID,
                        to see a detailed view.
                      </p>
                      <p className='text-muted'>If you have any questions at any time, just reach out to us on <a
                        href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>
                    </div>
                  </Col>
                </Row>)
            )
        }
        {/* <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
              <h6>Create Subscription</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=4ViJlPmWSSA"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />
            </div>
            <div className="py-4 border-bottom">
              <h6>Subscriptions and Filtering</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=QDEP-0jLWek"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />
            </div>
            <div className="py-4 border-bottom">
              <h6>Export Subscription Data</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=AvohNGVCX6E"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />
            </div>
          </HelpPopUp> */}
      </ReactCSSTransitionGroup>
    </Fragment>
  )
}

const mapStateToProps = state => ({
  subscriptionEntities: state.subscriptionContractDetails.entities,
  subscriptionGrpEntities: state.subscriptionGroup.entities,
  subscriptionGrpPlanEntities: state.subscriptionGroup.sellingPlanData,
  loadingSellingPlans: state.subscriptionGroup.loading,
  loading: state.subscriptionContractDetails.loading,
  exporting: state.subscriptionContractDetails.exporting,
  shopInfo: state.shopInfo.entity,
  account: state.authentication.account,
});

const mapDispatchToProps = {
  getEntities,
  subGrpEntities,
  exportSubscriptionContract,
  getSellingPlans
};

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionList);
