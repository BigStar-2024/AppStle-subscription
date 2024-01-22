// @ts-expect-error ts(5097) An import path can only end with a '.tsx' extension when 'allowImportingTsExtensions' is enabled.
import { AnalyticsContext, extractTextFromHtml } from 'app/DemoPages/Dashboards/Analytics/Analytics.tsx';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import React, { useContext } from 'react';
import { Line } from 'react-chartjs-2';
import { HelpCircleOutline } from 'react-ionicons';
import Loader from 'react-loaders';
import { Card, CardBody, CardTitle, Col, Row } from 'reactstrap';
import DateRangeText from 'app/DemoPages/Dashboards/Analytics/DateRangeText';

export default function Revenue() {
  const {
    revenueGrowthMonthOverMonth,
    averageOrderValue,
    nextSevenDayEstimatedRevenueTotal,
    nextThirtyDayEstimatedRevenueTotal,
    nextNinetyDayEstimatedRevenueTotal,
    currencyCode,
    moneyFormat,
    estimatedVsHistoricalRevenue,
    orderSumByWeek,
    loading,
  } = useContext(AnalyticsContext);

  const historicalData = {
    labels: ['7 Days', '30 Days', '90 Days'],
    datasets: [
      {
        label: `Estimated Revenue in ${
          Number()
            .toLocaleString(undefined, {
              style: 'currency',
              currency: currencyCode || moneyFormat || 'USD',
            })
            .split('')[0]
        }`,
        data: [
          estimatedVsHistoricalRevenue[0]?.estimatedRevenueTotalNumerical,
          estimatedVsHistoricalRevenue[1]?.estimatedRevenueTotalNumerical,
          estimatedVsHistoricalRevenue[2]?.estimatedRevenueTotalNumerical,
        ],
        fill: true,
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderColor: 'rgba(75,192,192,1)',
      },
      {
        label: `Historical Revenue in ${
          Number()
            .toLocaleString(undefined, {
              style: 'currency',
              currency: currencyCode || moneyFormat || 'USD',
            })
            .split('')[0]
        }`,
        data: [
          estimatedVsHistoricalRevenue[0]?.historicalRevenueTotalNumerical,
          estimatedVsHistoricalRevenue[1]?.historicalRevenueTotalNumerical,
          estimatedVsHistoricalRevenue[2]?.historicalRevenueTotalNumerical,
        ],
        fill: false,
        backgroundColor: 'rgb(75, 192, 192)',
        borderColor: '#742774',
      },
    ],
  };

  const historicalOptions = {
    responsive: true,
    scales: {
      yAxes: [
        {
          ticks: {
            beginAtZero: true,
            callback: function (value, index, values) {
              return value;
            },
          },
        },
      ],
    },
    tooltips: {
      callbacks: {
        label: function (tooltipItem, data) {
          var label = data.datasets[tooltipItem.datasetIndex].label || '';

          if (label) {
            label += ' = ';
          }
          label += tooltipItem.yLabel.toLocaleString('en-US', {
            style: 'currency',
            currency: currencyCode || moneyFormat || 'USD',
          });
          return label;
        },
      },
    },
  };

  const orderSumData = {
    labels: orderSumByWeek?.map(ele => {
      return ele.orderCreatedAt;
    }),
    datasets: [
      {
        label: `Total Amount in ${
          Number()
            .toLocaleString(undefined, {
              style: 'currency',
              currency: currencyCode || moneyFormat || 'USD',
            })
            .split('')[0]
        }`,
        fill: true,
        lineTension: 0.1,
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderColor: 'rgba(75,192,192,1)',
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
        data: orderSumByWeek?.map(ele => {
          return ele.sum;
        }),
      },
    ],
  };

  return (
    <>
      {loading?.analyticsCountersLoading || loading?.revenueLoading ? (
        <div className="text-center">
          <Loader type="line-scale" active />
        </div>
      ) : (
        <>
          <Row>
            <Col lg={6} sm={6}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{ width: '100%' }}>
                  <CardBody>
                    <div>
                      <b>
                        {/* "growth" is correct, do not change */}
                        Monthly Revenue Growth Rate (%)
                        <HelpTooltip>
                          Shows how much revenue has grown in percentage terms, from the past month to the current month
                        </HelpTooltip>
                      </b>
                    </div>
                    <div className="widget-numbers text-success" style={{ fontSize: '2.0rem' }}>
                      {extractTextFromHtml(revenueGrowthMonthOverMonth?.toString())?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
            <Col lg={6} sm={6}>
              <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                <Card className="main-card mb-3" style={{ width: '100%' }}>
                  <CardBody>
                    <div>
                      <b>Average Order Value</b>
                      <HelpTooltip>
                        For subscriptions created within the specified timeframe, average order value of a subscription.
                      </HelpTooltip>
                      <DateRangeText />
                    </div>
                    <div className="widget-numbers text-secondary" style={{ fontSize: '2.0rem' }}>
                      {extractTextFromHtml(averageOrderValue?.toString())?.toLocaleString()}
                    </div>
                  </CardBody>
                </Card>
              </div>
            </Col>
          </Row>
          <Row>
            <Col md={8} sm={12} xs={12}>
              <Card className="main-card" style={{ marginBottom: '20px' }}>
                <CardBody>
                  <CardTitle>
                    Historical vs Estimated Revenue
                    <DateRangeText />
                  </CardTitle>
                  <Line data={historicalData} options={historicalOptions} />
                </CardBody>
              </Card>
            </Col>
            <Col md={4} sm={12} xs={12}>
              <div className="card mb-3 widget-chart widget-chart2 text-left card-btm-border card-shadow-success border-success">
                <div className="widget-chat-wrapper-outer">
                  <div className="widget-chart-content pt-3 pl-3 pb-1">
                    <div className="widget-chart-flex">
                      <div className="widget-numbers">
                        <div className="widget-chart-flex">
                          <div className="fsize-4" style={{ display: 'flex', alignItems: 'center' }}>
                            <div
                              className="widget-numbers text-primary"
                              style={{
                                fontSize: '35px',
                                marginLeft: '7%',
                              }}
                            >
                              {extractTextFromHtml(nextSevenDayEstimatedRevenueTotal?.toString())?.toLocaleString()}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <h6 className="widget-subheading mb-0 opacity-5">
                      <span id="activeUsersToolTip"></span> Next 7 Day Estimated Revenue{' '}
                      <HelpTooltip>Based on your next 7 days queued orders.</HelpTooltip>
                    </h6>
                  </div>
                </div>
              </div>

              <div className="card mb-3 widget-chart widget-chart2 text-left card-btm-border card-shadow-success border-success">
                <div className="widget-chat-wrapper-outer">
                  <div className="widget-chart-content pt-3 pl-3 pb-1">
                    <div className="widget-chart-flex">
                      <div className="widget-numbers">
                        <div className="widget-chart-flex">
                          <div className="fsize-4" style={{ display: 'flex', alignItems: 'center' }}>
                            <div
                              className="widget-numbers text-primary"
                              style={{
                                fontSize: '35px',
                                marginLeft: '7%',
                              }}
                            >
                              {extractTextFromHtml(nextThirtyDayEstimatedRevenueTotal?.toString())?.toLocaleString()}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <h6 className="widget-subheading mb-0 opacity-5">
                      <span id="activeUsersToolTip"></span> Next 30 Days Estimated Revenue{' '}
                      <HelpTooltip>Based on your next 30 days queued orders.</HelpTooltip>
                    </h6>
                  </div>
                </div>
              </div>
              <div className="card mb-3 widget-chart widget-chart2 text-left card-btm-border card-shadow-success border-success">
                <div className="widget-chat-wrapper-outer">
                  <div className="widget-chart-content pt-3 pl-3 pb-1">
                    <div className="widget-chart-flex">
                      <div className="widget-numbers">
                        <div className="widget-chart-flex">
                          <div className="fsize-4" style={{ display: 'flex', alignItems: 'center' }}>
                            <div
                              className="widget-numbers text-primary"
                              style={{
                                fontSize: '35px',
                                marginLeft: '7%',
                              }}
                            >
                              {extractTextFromHtml(nextNinetyDayEstimatedRevenueTotal?.toString())?.toLocaleString()}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <h6 className="widget-subheading mb-0 opacity-5">
                      <span id="activeUsersToolTip"></span> Next 90 Day Estimated Revenue{' '}
                      <HelpTooltip>Based on your next 90 days queued orders.</HelpTooltip>
                    </h6>
                  </div>
                </div>
              </div>
            </Col>
          </Row>
          <Row>
            <Col md={12} sm={12} xs={12}>
              <Card className="main-card" style={{ marginBottom: '20px' }}>
                <CardBody>
                  <CardTitle>
                    Order Amount Per Week <HelpTooltip>First Time + Recurring Orders</HelpTooltip>
                    <DateRangeText />
                  </CardTitle>
                  <Line type="line" data={orderSumData} options={historicalOptions} />
                </CardBody>
              </Card>
            </Col>
          </Row>
        </>
      )}
    </>
  );
}
