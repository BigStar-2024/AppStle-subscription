import React, { useState, useEffect, Fragment, useCallback } from 'react';
import { connect } from 'react-redux';
import Loader from 'react-loaders';
import axios from 'axios';
import moment from 'moment';
import {
  Button,
  ButtonGroup,
  Table,
  InputGroup,
  Input,
  Label,
  FormGroup,
  Row,
  Col,
  Card,
  CardBody,
  ListGroup,
  UncontrolledButtonDropdown,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  ListGroupItem,
  Dropdown,
  PaginationLink,
  Alert,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter
} from 'reactstrap';
import Pagination from "react-js-pagination";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFilter, faSync } from '@fortawesome/free-solid-svg-icons';
import { Field, Form } from 'react-final-form';
import { Link } from "react-router-dom";
import { JhiItemCount, JhiPagination } from "react-jhipster";
import { useHistory, useLocation, useParams } from 'react-router';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { getEntities } from "app/entities/subscription-contract-details/user-subscription-contract-details.reducer";
import FilterAction from "./FilterAction";
import CustomerFilter from "./CustomerFilter";
import BlockUi from '@availity/block-ui';
import {aryIannaTimeZones, convertToShopTimeZoneDate} from "../Shared/SuportedShopifyTImeZone";
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";
import YoutubeVideoPlayer from '../Tutorials/YoutubeVideoPlayer';
var momentTZ = require('moment-timezone');

const CustomerList = ({ getEntities, customerEntities, loading, totalItems, shopInfo }) => {

  const [searchValue, setSearchValue] = useState('');
  const [sortByType, setSortByType] = useState('name');
  const [activePage, setActivePage] = useState(1);
  const [totalRowData, setTotalRowData] = useState(0);
  const [hasData, setHasData] = useState(false);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [sortByDir, setSortByDir] = useState('asc');
  let initFilter = FilterAction.getFilterObject();
  const [filterVal, setFilterVal] = useState(initFilter);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const toggle = () => setDropdownOpen(prevState => !prevState);
  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);
  const handleGridData = () => {
    let sortField = `${sortByType},${sortByDir}`;
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
  }, [customerEntities]);
  const handlePagination = activePage => {
    setActivePage(activePage);
  };
  const onApplyFilter = (filter) => {
    setFilterVal(filter)
    setActivePage(1)
    FilterAction.setFilterObject(filter);
    toggle();
  }
  const hasFilter = () => {
    return (_.size(filterVal) > 0) || hasData;
  }

  const handleOnSync = (customerId) => {
    const requestUrl = `/api/subscription-customers/sync-info/${customerId}`;
    axios.get(requestUrl).then(res => {
      handleGridData();
    });
  }

  const intitialState = {
    search: ''
  };
  return (
    <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}>
        <PageTitle
          heading="Customers"
          subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/4928299-how-can-i-keep-track-of-customer-subscriptions' target='blank'> Click here to know more about easily accessing subscribed customer details.</a>"
          icon="pe-7s-users icon-gradient"
          sticky={false}
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
                title: "How Can I Keep Track of Customer Subscriptions?",
                url: "https://intercom.help/appstle/en/articles/4928299-how-can-i-keep-track-of-customer-subscriptions"
              }
            ]
          }}
        />
        {
          (loading && !hasFilter()) ?
            (<div style={{ margin: "10% 0 0 43%" }} className="loader-wrapper d-flex justify-content-center align-items-center">
              <Loader type="line-scale" />
            </div>)
            :
            (customerEntities && (customerEntities?.length > 0 || hasFilter())) ?
              <Fragment>
                {/* <Alert className="mb-2" color="warning">
                  <h6>Need assistance?</h6>
                  <p>Watch our tutorial videos or read our help documentation to get a better understanding.</p>
                  <span>
                    <Button color="warning" onClick={toggleShowModal}>Video Tutorials</Button>
                    <a
                      href='https://intercom.help/appstle/en/articles/4928299-how-to-keep-track-of-customer-subscriptions'
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
                        <h6>Customer Section</h6>
                        <YoutubeVideoPlayer
                          url="https://www.youtube.com/watch?v=e2k77lyHjZM"
                          iframeHeight="100%"
                          divClassName="video-container"
                          iframeClassName="responsive-iframe"
                        />
                      </div>
                      <div className="py-4 border-bottom">
                      <h6>Syncing Customer Emails</h6>
                        <YoutubeVideoPlayer
                          url="https://www.youtube.com/watch?v=OR-2FVvO-q4"
                          iframeHeight="100%"
                          divClassName="video-container"
                          iframeClassName="responsive-iframe"
                        />
                      </div>
                      <div className="py-4 border-bottom">
                        <h6>How to Access the Customer Portal</h6>
                        <YoutubeVideoPlayer
                          url="https://www.youtube.com/watch?v=aIslGRp83cY"
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
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
                      <div>
                        <Dropdown isOpen={dropdownOpen} toggle={toggle}>
                          <DropdownToggle className="" color="default" className="btn-lg" style={{
                            border: "1px solid #ced4da"
                          }}>
                            Filters <FontAwesomeIcon icon={faFilter} />
                          </DropdownToggle>
                          <DropdownMenu right style={{ width: '530px', padding: 0 }}>
                            <CustomerFilter filterVal={filterVal}
                              onCancel={() => {
                                toggle();
                              }}
                              onApply={onApplyFilter} />
                          </DropdownMenu>
                        </Dropdown>
                      </div>
                    </div>
                    <BlockUi
                      tag="div"
                      blocking={loading}
                      loader={<Loader active type="line-scale" />}
                    >
                      <div>
                        <Table className="mb-0 mt-4 text-left">
                          <thead>
                            <tr>
                              <th>Name</th>
                              <th>Active subscriptions</th>
                              <th>Inactive subscriptions</th>
                              <th>Next order date</th>
                              <th>Shopify Link</th>
                              <th>Actions</th>
                              {/*<th>Total orders</th>
                        <th>Actions</th>*/}
                            </tr>
                          </thead>
                          <tbody>
                            {
                              (customerEntities?.map((item) => {
                                return (
                                  <tr key={item.customerId}>
                                    <td><Link to={`/dashboards/customers/${item.customerId}/edit`}>{item.name}</Link><br></br>{item.email}</td>
                                    <td>{item.activeSubscriptions} Subscription</td>
                                    <td>{item.inActiveSubscriptions} Subscription</td>
                                    <td>{item.activeSubscriptions > 0 && item.nextOrderDate != null ? (convertToShopTimeZoneDate(item.nextOrderDate, shopInfo.ianaTimeZone)) : "-"}</td>
                                    <td onClick={() =>  window.open(`https://${shopInfo.shop}/admin/customers/${item.customerId}`)} style={{color: "#545cd8", textDecoration: "underline", cursor: "pointer"}}>View in Shopify</td>
                                    <td>
                                      <ButtonGroup>
                                        <Button onClick={() => handleOnSync(item.customerId)} color="primary" title="Sync customer info from Shopify">
                                          <FontAwesomeIcon icon={faSync} />
                                        </Button>
                                      </ButtonGroup>
                                    </td>
                                  </tr>
                                )
                              }))
                            }
                          </tbody>
                        </Table>
                        {
                          customerEntities?.length == 0 &&
                          <div className="text-center m-3">No data available</div>
                        }
                        <Row style={{ textAlign: 'center' }}>

                          <Col md={12}>
                            <br />
                            <div style={{ display: 'flex', justifyContent: 'center' }}>
                              <Pagination
                                activePage={activePage}
                                itemsCountPerPage={itemsPerPage}
                                totalItemsCount={totalRowData}
                                // pageRangeDisplayed={5}
                                onChange={handlePagination}
                              />
                            </div>
                            <JhiItemCount page={activePage} total={totalRowData} itemsPerPage={itemsPerPage} />
                          </Col>
                        </Row>
                      </div>
                    </BlockUi>
                  </CardBody>
                </Card>
              </Fragment>
              :
              (<Row className="align-items-center welcome-page">
                <Col sm='5'>
                  <div>
                    <h4>Welcome to Customers Page</h4>
                    <p className='text-muted'>
                      This page will give you the complete list of all the customers who have or had a subscription in your store. If you want to learn more about the subscriptions of a particular customer, click on their name, and you will see a detailed view.
                 </p>
                    <p className='text-muted' >Read this <a href="https://intercom.help/appstle/en/articles/4928299-how-can-i-keep-track-of-customer-sub" target="_blank">doc</a> to know more about keeping track of customer subscriptions. </p>
                    <p className='text-muted' >If you have any questions at any time, just reach out to us on <a
                      href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>
                  </div>
                </Col>
              </Row>)
        }
        {/* <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
              <h6>Customer Section</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=e2k77lyHjZM"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />
            </div>
            <div className="py-4 border-bottom">
            <h6>Syncing Customer Emails</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=OR-2FVvO-q4"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />
            </div>
            <div className="py-4 border-bottom">
              <h6>How to Access the Customer Portal</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=aIslGRp83cY"
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
  customerEntities: state.userSubscriptionContractDetails.entities,
  totalItems: state.userSubscriptionContractDetails.totalItems,
  loading: state.userSubscriptionContractDetails.loading,
  shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
  getEntities
};

export default connect(mapStateToProps, mapDispatchToProps)(CustomerList);
