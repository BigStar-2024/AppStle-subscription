import React, { Fragment, useEffect, useState } from 'react';
import { connect } from "react-redux";
import PageTitle from "app/Layout/AppMain/PageTitle";
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {
  getEntities
} from "./bulk-automation.reducer";
import { Card, CardBody, Col, Row, Table } from "reactstrap";
import Loader from "react-loaders";
import PerfectScrollbar from "react-perfect-scrollbar";
import CustomHtmlToolTip from "app/DemoPages/Dashboards/SubscriptionGroups/CustomHtmlToolTip";
import { Help } from "@mui/icons-material";
import { ActivityLogEventSource, ActivityLogEventType } from "app/DemoPages/Dashboards/ActivityLog/activityEnum";
import { aryIannaTimeZones } from "app/DemoPages/Dashboards/Shared/SuportedShopifyTImeZone";
import momentTZ from "moment-timezone";
import moment from "moment";
import _ from "lodash";
import BlockUi from '@availity/block-ui';

export const BulkAutomationsActivityLog = ({ loading, totalItems, shopInfo, bulkAutomationEntity, getEntities }) => {
  const [activePage, setActivePage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(1000);
  const [bulkEntityList, setBulkEntityList] = useState([])

  const handleGridData = (activePage) => {
    getEntities(activePage - 1, itemsPerPage, null)
  }

  useEffect(() => {
    handleGridData(activePage);
  }, [activePage])

  useEffect(() => {
    setBulkEntityList(bulkAutomationEntity);
  }, [bulkAutomationEntity]);

  const formatJSONData = value => {
    try {
      let str = JSON.stringify(JSON.parse(value), undefined, 4);
      if (str.includes('class MessageOut')) {
        str = str.replaceAll('\\n', '\n');
      }
      return str;
    } catch (error) {
      return value;
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
          heading={'Bulk Automations Activity Logs'}
          icon="lnr-gift icon-gradient bg-mean-fruit"
        />
        {
          (loading) ? <div style={{ margin: "10% 0 0 43%" }}
            className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div> : <>
            {
              (bulkEntityList && (bulkEntityList?.length > 0)) ? <>
                <Card className="main-card">
                  <CardBody>

                    <PerfectScrollbar>
                      <BlockUi
                        tag="div"
                        blocking={loading}
                        loader={<Loader active type="line-scale" />}
                      >
                        <div>
                          <Table className="mb-0 mt-4 text-left" responsive>
                            <thead>
                              <tr>

                                <th>Automation Type</th>
                                <th>Running</th>
                                <th>Request Info</th>
                                <th>Error Info</th>
                                <th>Start Time<div
                                  style={{ fontSize: '10px', color: '#767672' }}>(MM/DD/YYYY
                                  HH:MM)</div></th>
                                <th>End Time<div
                                  style={{ fontSize: '10px', color: '#767672' }}>(MM/DD/YYYY
                                  HH:MM)</div></th>
                              </tr>
                            </thead>
                            <tbody>
                              {
                                bulkEntityList && bulkEntityList.map((item, index) => {
                                  return (
                                    <tr key={item?.id}>
                                      <td>{item.automationType}</td>
                                      <td>{item.running ? 'Yes' : 'No'}</td>
                                      <td>{
                                        <p>
                                          <CustomHtmlToolTip
                                            interactive
                                            placement="right"
                                            arrow
                                            enterDelay={0}
                                            title={
                                              <div style={{ padding: '8px' }}>
                                                <div
                                                  style={{ textAlign: 'center' }}>
                                                  <b>Shopify Response</b>
                                                </div>
                                                <hr style={{ borderColor: '#fff' }} />
                                                <pre className='font-size-md text-white'> {formatJSONData(item?.requestInfo)}</pre>
                                              </div>
                                            }
                                          >
                                            <Help style={{ fontSize: '1rem' }} />
                                          </CustomHtmlToolTip>
                                        </p>
                                      }</td>
                                      <td>{
                                        <p>
                                          <CustomHtmlToolTip
                                            interactive
                                            placement="right"
                                            arrow
                                            enterDelay={0}
                                            title={
                                              <div style={{ padding: '8px' }}>
                                                <div
                                                  style={{ textAlign: 'center' }}>
                                                  <b>Shopify Response</b>
                                                </div>
                                                <hr style={{ borderColor: '#fff' }} />
                                                {item?.errorInfo}
                                              </div>
                                            }
                                          >
                                            <Help style={{ fontSize: '1rem' }} />
                                          </CustomHtmlToolTip>
                                        </p>
                                      }</td>

                                      <td style={{ verticalAlign: "top" }}>
                                        {item?.startTime != null ? (aryIannaTimeZones.includes(shopInfo?.shopTimeZone.substring(12))
                                          ? momentTZ(item?.startTime)
                                            .tz(shopInfo?.shopTimeZone.substring(12))
                                            .format('MMMM DD, YYYY hh:mm A')
                                          : moment(item?.startTime)?.format('MMMM DD, YYYY hh:mm A')) : "-"}
                                      </td>

                                      <td style={{ verticalAlign: "top" }}>
                                        {item?.endTime != null ? (aryIannaTimeZones.includes(shopInfo?.shopTimeZone.substring(12))
                                          ? momentTZ(item?.endTime)
                                            .tz(shopInfo?.shopTimeZone.substring(12))
                                            .format('MMMM DD, YYYY hh:mm A')
                                          : moment(item?.endTime)?.format('MMMM DD, YYYY hh:mm A')) : "-"}
                                      </td>
                                    </tr>
                                  )
                                })
                              }
                            </tbody>
                          </Table>
                          {
                            bulkEntityList?.length === 0 &&
                            <div className="text-center m-3">No data available</div>
                          }
                        </div>
                      </BlockUi>
                    </PerfectScrollbar>
                  </CardBody>
                </Card>
              </> : <Row className="align-items-center welcome-page">
                <Col sm='5'>
                  <div>
                    <h4>Welcome to Activity Log Section</h4>
                    <p className='text-muted'>
                      This section give you the complete list of subscription activity and time.
                      Currently there is no activity log available.
                    </p>
                    <p className='text-muted'>If you have any questions at any time, just reach out
                      to us on <a href="javascript:window.Intercom('showNewMessage')">our chat widget</a>
                    </p>
                  </div>
                </Col>
              </Row>




            }</>}
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = (storeState) => ({
  bulkAutomationEntity: storeState.bulkAutomation.entities,
  loading: storeState.bulkAutomation.loading,
  updating: storeState.bulkAutomation.updating,
  updateSuccess: storeState.bulkAutomation.updateSuccess
});

const mapDispatchToProps = {
  getEntities
};

export default connect(mapStateToProps, mapDispatchToProps)(BulkAutomationsActivityLog);

