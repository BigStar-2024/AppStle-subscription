// @ts-expect-error ts(5097) An import path can only end with a '.tsx' extension when 'allowImportingTsExtensions' is enabled.
import { AnalyticsContext } from 'app/DemoPages/Dashboards/Analytics/Analytics.tsx';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import React, { useContext } from 'react';
import { Card, CardBody, Col, Row } from 'reactstrap';
import ProductDeliveryForecasting from 'app/DemoPages/Dashboards/Analytics/ProductDeliveryForecasting';
import ProductRevenueReport from 'app/DemoPages/Dashboards/Analytics/ProductRevenueReport';
import DateRangeText from 'app/DemoPages/Dashboards/Analytics/DateRangeText';
import Loader from 'react-loaders';

export default function Overview() {
  const {
    totalOrderCount,
    totalSubscriptionCount,
    totalCustomerCount,
    newSubscription,
    productRevenueData,
    productRevenueLoading,
    loading,
  } = useContext(AnalyticsContext);

  return (
    <>
      {loading?.analyticsCountersLoading || loading?.overviewLoading ? (
        <div className="text-center">
          <Loader type="line-scale" active />
        </div>
      ) : (
        <>
          <Row>
            <Col lg={3} sm={6}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{ width: '100%' }}>
                  <CardBody>
                    <div>
                      <b>
                        Total Orders{' '}
                        <HelpTooltip>
                          Total number of orders created within the specified time period. This includes both first time and recurring
                          orders.
                        </HelpTooltip>
                      </b>
                      <DateRangeText />
                    </div>
                    <div className="widget-numbers text-info" style={{ fontSize: '2.0rem' }}>
                      {totalOrderCount?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={3} sm={6}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{ width: '100%' }}>
                  <CardBody>
                    <div>
                      <b>
                        Total Subscriptions Created{' '}
                        <HelpTooltip>Total number of subscriptions created within the specified time period.</HelpTooltip>
                      </b>
                      <DateRangeText />
                    </div>
                    <div className="widget-numbers text-info" style={{ fontSize: '2.0rem' }}>
                      {totalSubscriptionCount?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={3} sm={6}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{ width: '100%' }}>
                  <CardBody>
                    <div>
                      <b>
                        Total Active Customers{' '}
                        <HelpTooltip>Number of customers added with active subscriptions within the specified time period.</HelpTooltip>
                      </b>
                      <DateRangeText />
                    </div>
                    <div className="widget-numbers text-info" style={{ fontSize: '2.0rem' }}>
                      {totalCustomerCount?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={3} sm={6}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{ width: '100%' }}>
                  <CardBody>
                    <div>
                      <b>
                        Total Active Subscriptions{' '}
                        <HelpTooltip>Total number of active subscriptions created within the specified time period.</HelpTooltip>
                      </b>
                      <DateRangeText />
                    </div>
                    <div className="widget-numbers text-info" style={{ fontSize: '2.0rem' }}>
                      {newSubscription?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
          </Row>
          <hr />
          <ProductRevenueReport productRevenueAnalyticsListData={productRevenueData} productRevenueLoading={productRevenueLoading} />
          <hr />
          <ProductDeliveryForecasting />
        </>
      )}
    </>
  );
}
