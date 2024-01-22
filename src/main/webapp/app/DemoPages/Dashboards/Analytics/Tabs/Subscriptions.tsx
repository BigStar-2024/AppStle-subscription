// @ts-expect-error ts(5097) An import path can only end with a '.tsx' extension when 'allowImportingTsExtensions' is enabled.
import {AnalyticsContext, extractTextFromHtml} from 'app/DemoPages/Dashboards/Analytics/Analytics.tsx';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import React, {useContext} from 'react';
import {Line} from 'react-chartjs-2';
import {HelpCircleOutline} from 'react-ionicons';
import {Card, CardBody, CardTitle, Col, Row} from 'reactstrap';
import 'katex/dist/katex.min.css';
import Latex from 'react-latex-next';
import Loader from 'react-loaders';
import DateRangeText from 'app/DemoPages/Dashboards/Analytics/DateRangeText';

export default function Subscriptions() {
  const {
    totalOrderAmount,
    totalSubscribedAmount,
    averageSubscriptionValue,
    subscriptionGrowthMonthOverMonth,
    subscriptionsTotalByWeek,
    newSubscription,
    churnRate,
    approvalRate,
    cancellationRate,
    totalRecurringOrderCount,
    totalFailedPaymentsCount,
    totalSkippedOrders,
    totalActiveSubscriptionCount,
    totalPausedSubscriptionCount,
    totalCanceledSubscriptionCount,
    subscribedVsUnsubscribed,
    loading,
  } = useContext(AnalyticsContext);

  const subscriptionTotalData = {
    labels: subscriptionsTotalByWeek?.map(ele => {
      return ele.orderCreatedAt;
    }),
    datasets: [
      {
        label: 'Total Subscriptions',
        fill: true,
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderColor: 'rgba(75,192,192,1)',
        lineTension: 0.1,
        borderCapStyle: 'butt',
        borderDash: [],
        borderDashOffset: 0.0,
        borderJoinStyle: 'miter',
        pointBorderColor: 'rgba(75,192,192,1)',
        pointBackgroundColor: '#fff',
        pointBorderWidth: 1,
        pointHoverRadius: 5,
        pointHoverBackgroundColor: 'rgba(75,192,192,1)',
        pointHoverBorderColor: 'rgba(220,220,220,1)',
        pointHoverBorderWidth: 2,
        pointRadius: 1,
        pointHitRadius: 10,
        data: subscriptionsTotalByWeek?.map(ele => {
          return ele.total;
        }),
      },
    ],
  };

  const subscriptionOptions = {
    scales: {
      yAxes: [
        {
          ticks: {
            beginAtZero: true,
          },
        },
      ],
    },
  };

  const subscribedVsUnsubscribedData = {
    labels: ['7 Days', '30 Days', '90 Days'],
    datasets: [
      {
        label: 'Subscribed',
        data: [
          subscribedVsUnsubscribed[0]?.subscriptionCount,
          subscribedVsUnsubscribed[1]?.subscriptionCount,
          subscribedVsUnsubscribed[2]?.subscriptionCount,
        ],
        fill: true,
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderColor: 'rgba(75,192,192,1)',
      },
      {
        label: 'Unsubscribed',
        data: [
          subscribedVsUnsubscribed[0]?.unsubscriptionCount,
          subscribedVsUnsubscribed[1]?.unsubscriptionCount,
          subscribedVsUnsubscribed[2]?.unsubscriptionCount,
        ],
        fill: false,
        backgroundColor: 'rgb(75, 192, 192)',
        borderColor: '#742774',
      },
    ],
  };

  return (
    <>
      {loading?.analyticsCountersLoading || loading?.overviewLoading ? (
        <div className="text-center">
          <Loader type="line-scale" active/>
        </div>
      ) : (
        <>
          <Row>
            <Col lg={4} sm={8}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        First Time Order Revenue{' '}
                        <HelpTooltip>Total revenue from first time orders within the specified time
                          period.</HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                      {extractTextFromHtml(totalSubscribedAmount?.toString())?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={4} sm={8}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Recurring Order Revenue{' '}
                        <HelpTooltip>Total revenue from recurring orders within the specified time period.</HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                      {extractTextFromHtml(totalOrderAmount.toString())?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            {/*<Col lg={3} sm={6}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Average Subscription Time (Days)
                        <HelpTooltip>
                          For subscriptions created within the specified timeframe, average number of days a
                          subscription remains active.If
                          there have been no cancellations, this value will be infinity.
                        </HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-info" style={{fontSize: '2.0rem'}}>
                      {averageSubscriptionValue == null ? 'Infinity' : averageSubscriptionValue.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>*/}
            <Col lg={4} sm={8}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        {/* "growth" is correct, do not change */}
                        Monthly Subscription Growth Rate (%)
                        <HelpTooltip>
                          The percentage growth of subscriptions from the past month to the current month.
                        </HelpTooltip>
                      </b>
                    </div>
                    <div className="widget-numbers text-info" style={{fontSize: '2.0rem'}}>
                      {subscriptionGrowthMonthOverMonth?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
          </Row>
          <Row>
            <Col md={12} sm={12} xs={12}>
              <Card className="main-card" style={{marginBottom: '20px'}}>
                <CardBody>
                  <CardTitle>
                    Subscriptions Per Week
                    <DateRangeText/>
                  </CardTitle>
                  <Line data={subscriptionTotalData} options={subscriptionOptions}/>
                </CardBody>
              </Card>
            </Col>
          </Row>
          <Row>
            {/*<Col lg={3} sm={6}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        New Active Subscriptions
                        <HelpTooltip>Number of active subscriptions created within the specified time
                          period.</HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                      {newSubscription?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>*/}
            <Col lg={4} sm={8}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Churn Rate
                        <HelpTooltip maxWidth={3000}>

                          <Latex>
                            {String.raw`$$\frac{\text{Cancelled subscriptions during a given period that were created prior to the start of the period}}{\text{Total subscriptions to be renewed at the start of that period}} * 100$$`}
                          </Latex>
                        </HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-primary" style={{fontSize: '2.0rem'}}>
                      {churnRate?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={4} sm={8}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Approval Rate
                        <HelpTooltip maxWidth={3000}>
                          <Latex>
                            {String.raw`$$\frac{\text{Total successful recurring orders in given period}}{\text{Total orders including failed and successful orders}} * 100$$`}
                          </Latex>
                        </HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-primary" style={{fontSize: '2.0rem'}}>
                      {approvalRate?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={4} sm={8}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Cancellation Rate
                        <HelpTooltip maxWidth={3000}>
                          For subscriptions created within a specified timeframe, percentage of subscriptions that are not active.
                        </HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-primary" style={{fontSize: '2.0rem'}}>
                      {cancellationRate?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
          </Row>
          <Row>
            <Col lg={4} sm={8}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Total Recurring Orders
                        <HelpTooltip>Total number of recurring orders within the specified time period.</HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-primary" style={{fontSize: '2.0rem'}}>
                      {totalRecurringOrderCount?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={4} sm={8}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Total Failed Recurring Orders
                        <HelpTooltip>Total number of failed recurring orders within the specified time
                          period.</HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-primary" style={{fontSize: '2.0rem'}}>
                      {totalFailedPaymentsCount?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={4} sm={8}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Total Skipped Orders
                        <HelpTooltip>Total number of skipped orders within the specified time period.</HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-primary" style={{fontSize: '2.0rem'}}>
                      {totalSkippedOrders?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={4} sm={8}></Col>
          </Row>
          <Row>
            <Col md={8} sm={12} xs={12}>
              <Card className="main-card" style={{marginBottom: '20px'}}>
                <CardBody>
                  <CardTitle>
                    Subscribed vs. Unsubscribed
                    <DateRangeText/>
                  </CardTitle>
                  <Line data={subscribedVsUnsubscribedData} options={subscriptionOptions}/>
                </CardBody>
              </Card>
            </Col>
            <Col md={4} sm={12} xs={12}>
              {/*<div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Total Active Subscriptions
                        <HelpTooltip>Total number of active subscriptions within the specified time
                          period.</HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                      {totalActiveSubscriptionCount?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>*/}
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Total Paused Subscriptions
                        <HelpTooltip>Total number of paused subscriptions within the specified time
                          period.</HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers  text-secondary" style={{fontSize: '2.0rem'}}>
                      {totalPausedSubscriptionCount?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{width: '100%'}}>
                  <CardBody>
                    <div>
                      <b>
                        Total Cancelled Subscriptions
                        <HelpTooltip>Total number of cancelled subscriptions within the specified time
                          period.</HelpTooltip>
                      </b>
                      <DateRangeText/>
                    </div>
                    <div className="widget-numbers text-primary" style={{fontSize: '2.0rem'}}>
                      {totalCanceledSubscriptionCount?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
          </Row>
        </>
      )}
    </>
  );
}
