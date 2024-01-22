import PageTitle from 'app/Layout/AppMain/PageTitle';
import axios from 'axios';
import * as moment from 'moment';
import React, {useState, Component, Fragment} from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {Bar, Chart, Line} from 'react-chartjs-2';
import { HelpCircleOutline } from 'react-ionicons';
import Loader from 'react-loaders';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import {Help} from '@mui/icons-material';
import SweetAlert from 'sweetalert-react';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import { convertToShopTimeZoneDate } from '../Shared/SuportedShopifyTImeZone';
import {
  getProductRevenueFromSubscriptionContracts
} from 'app/entities/subscription-contract-details/subscription-contract-details.reducer'
import {
  Card,
  CardBody,
  CardTitle,
  Col,
  Container,
  Input,
  FormGroup,
  Row,
  Table,
  Modal,
  Button,
  ModalBody,
  ModalFooter,
  ListGroup,
  ListGroupItem,
  Progress,
  ModalHeader,
  Label,
  FormFeedback
} from 'reactstrap';
import {getPaginationItemsNumber, JhiPagination} from 'react-jhipster';
import Select from 'react-select';
import DatePickerComponent from 'app/DemoPages/Components/DatePicker/DatePicker';
import {connect} from 'react-redux';
import Axios from 'axios';
import BlockUi from '@availity/block-ui';
import {Slide, toast} from 'react-toastify';
import onetimepurchaseText from '../../../../static/theme/assets/utils/images/widget-setting/onetimepurchaseText.png';
import ProductRevenueReport from '../Report/ProductRevenueReport';
import HelpTooltip from '../Shared/HelpTooltip';

var momentTZ = require('moment-timezone');

const selectCustomStyles = {
  dropdownIndicator: (provided, state) => ({
    ...provided,
    marginTop: '5px !important'
  })
};

const options = [
  {value: 'CUSTOM_DATE', label: 'Custom Date'},
  {value: 0, label: 'Today'},
  {value: 1, label: 'Yesterday'},
  {value: 7, label: 'Last 7 Days'},
  {value: 30, label: 'Last 30 Days'},
  {value: 90, label: 'Last 3 Months'},
  {value: 180, label: 'Last 6 Months'},
  {value: 365, label: 'Last Year'},
  {value: 730, label: 'Last 2 Year'},
  {value: 1095, label: 'Last 3 Year'},
  {value: 1460, label: 'Last 4 Year'},
  {value: 1825, label: 'Last 5 Year'}
];

const DAYS = 'days';
const RANGE = 'range';

export class Analytics extends Component {
  constructor(props) {
    super(props);
    this.state = {
      deliveredArray: [],
      openedArray: [],
      clickedArray: [],
      totalOrderAmount: 0,
      totalSubscribedAmount: 0,
      churnRate: 0,
      subscriptionGrowthMonthOverMonth: 0,
      revenueGrowthMonthOverMonth: 0,
      totalOrderCount: 0,
      totalCanceledSubscriptionCount: 0,
      totalPausedSubscriptionCount: 0,
      totalCustomerCount: 0,
      averageOrderValue: 0,
      api_complete_progress: 0,
      moneyFormat: '',
      orderSumByWeek: [],
      subscriptionsTotalByWeek: [],
      mostDemandProductList: [],
      demandProductLoading: false,
      itemsPerPage: 5,
      activePage: 1,
      totalItems: 0,
      selectedDays: 90,
      fromDay: new Date(),
      toDay: new Date(),
      filterBy: DAYS,
      products: [],
      productLoading: false,
      selectedProduct: null,
      mostWantedProductLoading: false,
      showToast: false,
      showRecipientModal: false,
      toEmail: '',
      weekIntervalSubscription: 0,
      yearIntervalSubscription: 0,
      dayIntervalSubscription: 0,
      monthIntervalSubscription: 0,
      newSubscription: 0,
      estimatedVsHistoricalRevenue: [],
      productDeliveryAnalyticsList: [],
      // productRevenueAnalyticsList: [],
      nextNinetyDayEstimatedRevenueTotal: 0,
      nextThirtyDayEstimatedRevenueTotal: 0,
      nextSevenDayEstimatedRevenueTotal: 0,
      totalSubscriptionCount: 0,
      totalActiveSubscriptionCount: 0,
      totalFailedPaymentsCount: 0,
      averageSubscriptionValue: 0,
      subscriptionVsUnsubscription: [],
      customDatePickerToggle: false,
      fromDate: null,
      toDate: null,
      emailValidity: true,
      emailSendingProgress: false,
      blurred: false,
      inputValueForTestEmailId: '',
      emailSuccessAlert: false,
      emailFailAlert: false,
      isModalOpen: false,
      totalRecurringOrderCount: 0,
      totalSkippedOrders: 0,
      approvalRate: 0,
      cancellationRate: 0,
      averageOrdersBeforeCancellation: 0
    };
  }

  componentDidMount() {
    this.getDemandProductsList();
    this.getAnalyticsCounters();
    this.getEstimatedHistoricalRevenue();
    this.getProductDeliveryAnalytics();
    // this.getProductRevenueAnalytics();
    this.getFutureUpcomingRevenue();
    this.getEmailStatus();
    this.getSubscriptionVsUnsubscription();
    this.productRevenueAnalyticsData();
  }

  formatDateString(string) {
    const date = Date.parse(string);
    const formattedDateString = moment(date).format('MMM D');
    return formattedDateString;
  }

  productRevenueAnalyticsData() {
    const {filterBy, selectedDays, fromDay, toDay} = this.state;
    this.props.getProductRevenueFromSubscriptionContracts(filterBy, selectedDays, fromDay, toDay);
  }

  getDemandProductsList() {
    const {activePage, itemsPerPage, filterBy, selectedDays, fromDay, toDay, selectedProduct} = this.state;
    const productId = selectedProduct != null ? selectedProduct.value : '';
    /*this.setState({ mostWantedProductLoading: true }, () => {
      axios
        .get(`api/stock-notifications/most-demand-product-list?${`page=${activePage - 1}&size=${itemsPerPage}`}`, {
          params: { filterBy, days: selectedDays, fromDay, toDay, productId: productId }
        })
        .then(response => {
          const { data, headers } = response;
          const total = headers['x-total-count'];
          this.setState({ mostDemandProductList: data, totalItems: total, demandProductLoading: false, mostWantedProductLoading: false });
        })
        .catch(error => {
          this.setState({ mostWantedProductLoading: false });
        });
    });*/
  }

  extractTextFromHtml = text => {
    let span = document.createElement('span');
    span.innerHTML = text;
    return span.textContent || span.innerText;
  };

  getAnalyticsCounters() {
    const {filterBy, selectedDays, fromDay, toDay} = this.state;
    axios
      .get('api/conversion-analytics/total-order-amount', {
        params: {filterBy, days: selectedDays, fromDay, toDay}
      })
      .then(response => {
        const {data} = response;
        this.setState({
          api_complete_progress: 100,
          totalOrderAmount: data.totalSold,
          totalSubscribedAmount: data.totalSubscribed,
          totalOrderCount: data.orderCount,
          totalCanceledSubscriptionCount: data.canceledSubscriptionCount,
          totalPausedSubscriptionCount: data.pausedSubscriptionCount,
          totalCustomerCount: data.customerCount,
          churnRate: data.churnRate,
          subscriptionGrowthMonthOverMonth: data.subscriptionGrowthMonthOverMonth,
          revenueGrowthMonthOverMonth: data.revenueGrowthMonthOverMonth,
          averageOrderValue: data.averageOrderValue,
          orderSumByWeek: data.ordersByWeek.sort((a, b) => (new Date(a["orderCreatedAt"]) - new Date(b["orderCreatedAt"]))),
          subscriptionsTotalByWeek: data.subscriptionsTotalByWeek,
          moneyFormat: data.currency,
          weekIntervalSubscription: data.weekIntervalSubscription,
          yearIntervalSubscription: data.yearIntervalSubscription,
          dayIntervalSubscription: data.dayIntervalSubscription,
          monthIntervalSubscription: data.monthIntervalSubscription,
          newSubscription: data.newSubscription,
          totalSubscriptionCount: data.totalSubscriptionCount,
          totalActiveSubscriptionCount: data.totalActiveSubscriptionCount,
          totalFailedPaymentsCount: data.totalFailedPaymentsCount,
          averageSubscriptionValue: data.averageSubscriptionValue,
          fromDate: data.fromDate,
          toDate: data.toDate,
          totalRecurringOrderCount: data.totalRecurringOrderCount,
          totalSkippedOrders: data.totalSkippedOrders,
          approvalRate: data.approvalRate,
          cancellationRate: data.cancellationRate,
          averageOrdersBeforeCancellation: data.averageOrdersBeforeCancellation
        });
      });
  }

  getEstimatedHistoricalRevenue() {
    axios.get('api/conversion-analytics/estimated-historical-order-amount-total').then(response => {
      const {data} = response;
      this.setState({
        estimatedVsHistoricalRevenue: data.estimatedVsHistoricalRevenue,
        currencyCode: data.currencyCode
      });
    });
  }

  getSubscriptionVsUnsubscription() {
    axios.get('api/conversion-analytics/subscription-unsubscription').then(response => {
      const {data} = response;
      this.setState({
        subscriptionVsUnsubscription: data.subscriptionVsUnsubscription
      });
    });
  }

  getProductDeliveryAnalytics() {
    const {filterBy, selectedDays, fromDay, toDay} = this.state;
    axios.get(`api/subscription-contract-details/productDelivery-analytics`, {
      params: { filterBy, days: selectedDays, fromDay, toDay }
    }).then(response => {
      const {data} = response;
      this.setState({
        productDeliveryAnalyticsList: data
      });
    });
  }

  // getProductRevenueAnalytics() {
  //   axios.get('api/subscription-contract-details/productRevenue-analytics').then(response => {
  //     const { data } = response;
  //     console.log(data);
  //     this.setState({
  //       productRevenueAnalyticsList: data
  //     });
  //   });
  // }

  getFutureUpcomingRevenue() {
    axios.get('api/conversion-analytics/estimated-total-order-amount').then(response => {
      const {data} = response;
      this.setState({
        nextNinetyDayEstimatedRevenueTotal: data.nextNinetyDayEstimatedRevenueTotal,
        nextThirtyDayEstimatedRevenueTotal: data.nextThirtyDayEstimatedRevenueTotal,
        nextSevenDayEstimatedRevenueTotal: data.nextSevenDayEstimatedRevenueTotal
      });
    });
  }

  getEmailStatus() {
    const {filterBy, selectedDays, fromDay, toDay} = this.state;
    /*axios
      .get('api/analytics', {
        params: { event: 'delivered', filterBy, days: selectedDays, fromDay, toDay }
      })
      .then(response => {
        const { data } = response;
        if (data.stats !== undefined) {
          this.setState({
            api_complete_progress: 100,
            deliveredArray: data.stats
          });
        }
      });*/

    /*axios
      .get('api/analytics', {
        params: { event: 'opened', filterBy, days: selectedDays, fromDay, toDay }
      })
      .then(response => {
        const { data } = response;
        if (data.stats !== undefined) {
          this.setState({
            api_complete_progress: 100,
            openedArray: data.stats
          });
        }
      });*/

    /*axios
      .get('api/analytics', {
        params: { event: 'clicked', filterBy, days: selectedDays, fromDay, toDay }
      })
      .then(response => {
        const { data } = response;
        if (data.stats !== undefined) {
          this.setState({
            api_complete_progress: 100,
            clickedArray: data.stats
          });
        }
      });*/
  }

  handleChangeStatisticsDays = selectedDays => {
    const {value} = selectedDays;
    if (value == 'CUSTOM_DATE') {
      this.setState({customDatePickerToggle: true, selectedDays: 0});
    } else {
      this.setState({customDatePickerToggle: false, selectedDays: value, filterBy: DAYS, activePage: 1}, () => {
        //this.getDemandProductsList();
        this.getAnalyticsCounters();
        this.productRevenueAnalyticsData();
        //this.getEmailStatus();
      });
    }
  };

  handleFromDays = fromDay => {
    if (fromDay <= this.state.toDay) {
      this.setState({fromDay: fromDay, filterBy: RANGE, activePage: 1}, () => {
        //this.getDemandProductsList();
        this.getAnalyticsCounters();
        this.productRevenueAnalyticsData();
        //this.getEmailStatus();
      });
    }
  };
  handleToDays = toDay => {
    if (toDay >= this.state.fromDay) {
      this.setState({toDay: toDay, filterBy: RANGE, activePage: 1}, () => {
        //this.getDemandProductsList();
        this.getAnalyticsCounters();
        this.productRevenueAnalyticsData();
        //this.getEmailStatus();
      });
    }
  };

  handlePagination = activePage => this.setState({activePage: activePage}, () => this.getDemandProductsList());

  ifAnyNonZero(array) {
    if (array !== undefined) {
      for (const iterator of array) {
        if (iterator !== 0 && iterator !== '0') {
          return true;
        }
      }
    }
    return false;
  }

  fetchProductList = productName => {
    if (productName.length > 1) {
      this.setState({productLoading: true});
      /*Axios.get(`api/stock-notifications/product-list`, { params: { productName } }).then(response => {
        const { data } = response;
        if (data.length > 0) {
          const products = [];
          for (let i = 0; i < data.length; i++) {
            const item = {
              value: data[i].id,
              label: data[i].title
            };
            products.push(item);
          }
          this.setState({ products: products, productLoading: false });
        }
      });*/
    }
  };

  handleChangeProductsMainSearch = selectedOption => {
    this.setState(
      {
        ...this.state,
        selectedProduct: selectedOption,
        itemsPerPage: selectedOption !== null ? 20 : 5,
        activePage: 1
      },
      () => {
        this.getDemandProductsList();
      }
    );
  };

  exportCsvMostWantedProductsList = () => {
    const {activePage, itemsPerPage, filterBy, selectedDays, fromDay, toDay, selectedProduct} = this.state;
    const productId = selectedProduct != null ? selectedProduct.value : '';
    Axios.get(`api/csv-most-wanted-products?toEmail=${this.state.toEmail}`, {
      params: {
        filterBy,
        days: selectedDays,
        fromDay,
        toDay,
        productId: productId,
        page: activePage - 1,
        size: itemsPerPage
      }
    });
    this.setState({showToast: true}, () => {
      this.setState({showToast: false});
    });

    this.setState({showRecipientModal: false});
  };

  sendDeliveryForcastExportMail = (emailId) => {
    if (this.checkEmailValidity(emailId)) {
      this.setState({emailSendingProgress: true});
      axios
        .get(`api/subscription-contract-details/export-delivery-forecast`, {
          params: {
            emailId: emailId
          }
        })
        .then(response => {
          this.cleanupBeforeModalClose();
          this.setState({emailSuccessAlert: true});
        })
        .catch(error => {
          this.cleanupBeforeModalClose();
          this.setState({emailFailAlert: true});
        });
    }
  };

  checkEmailValidity = (emailId) => {
    if (/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      .test(emailId)) {
        this.setState({emailValidity: true})
      return true
    } else {
      this.setState({emailValidity: false})
      return false;
    }
  };

  cleanupBeforeModalClose = () => {
    this.setState({emailValidity: true, emailSendingProgress: false, emailValidity: false, isModalOpen: !this.state.isModalOpen, blurred: false, inputValueForTestEmailId: ''})
  };

  render() {
    const historicalData = {
      labels: ['7 Days', '30 Days', '90 Days'],
      datasets: [
        {
          label: `Estimated Revenue in ${Number().toLocaleString(undefined, {
            style: "currency",
            currency: this.state.currencyCode || this.state.moneyFormat || "USD"
          }).split("")[0]}`,
          data: [
            this.state?.estimatedVsHistoricalRevenue[0]?.estimatedRevenueTotalNumerical,
            this.state?.estimatedVsHistoricalRevenue[1]?.estimatedRevenueTotalNumerical,
            this.state?.estimatedVsHistoricalRevenue[2]?.estimatedRevenueTotalNumerical
          ],
          fill: true,
          backgroundColor: "rgba(75,192,192,0.2)",
          borderColor: "rgba(75,192,192,1)"
        },
        {
          label: `Historical Revenue in ${Number().toLocaleString(undefined, {
            style: "currency",
            currency: this.state.currencyCode || this.state.moneyFormat || "USD"
          }).split("")[0]}`,
          data: [
            this.state?.estimatedVsHistoricalRevenue[0]?.historicalRevenueTotalNumerical,
            this.state?.estimatedVsHistoricalRevenue[1]?.historicalRevenueTotalNumerical,
            this.state?.estimatedVsHistoricalRevenue[2]?.historicalRevenueTotalNumerical
          ],
          fill: false,
          backgroundColor: 'rgb(75, 192, 192)',
          borderColor: "#742774"
        }
      ]
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
              }
            }
          }
        ]
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
              currency: this.state?.currencyCode || this.state?.moneyFormat || "USD"
            });
            // label += Math.round(tooltipItem.yLabel * 100) / 100;
            return label;
          }
        }
      }
    };

    const subscriptionVsUnsubscriptionData = {
      labels: ['7 Days', '30 Days', '90 Days'],
      datasets: [
        {
          label: 'Subscription',
          data: [
            this.state?.subscriptionVsUnsubscription[0]?.subscriptionCount,
            this.state?.subscriptionVsUnsubscription[1]?.subscriptionCount,
            this.state?.subscriptionVsUnsubscription[2]?.subscriptionCount
          ],
          fill: true,
          backgroundColor: "rgba(75,192,192,0.2)",
          borderColor: "rgba(75,192,192,1)"
        },
        {
          label: 'Unsubscription',
          data: [
            this.state?.subscriptionVsUnsubscription[0]?.unsubscriptionCount,
            this.state?.subscriptionVsUnsubscription[1]?.unsubscriptionCount,
            this.state?.subscriptionVsUnsubscription[2]?.unsubscriptionCount
          ],
          fill: false,
          backgroundColor: 'rgb(75, 192, 192)',
          borderColor: "#742774"
        }
      ]
    };

    const orderSumData = {
      labels: this.state?.orderSumByWeek?.map(ele => {
        return ele.orderCreatedAt;
      }),
      datasets: [
        {
          label: `Total Amount in ${Number().toLocaleString(undefined, {
            style: "currency",
            currency: this.state.currencyCode || this.state.moneyFormat || "USD"
          }).split("")[0]}`,
          fill: true,
          lineTension: 0.1,
          backgroundColor: "rgba(75,192,192,0.2)",
          borderColor: "rgba(75,192,192,1)",
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
          data: this.state?.orderSumByWeek?.map(ele => {
            return ele.sum;
          })
        }
      ]
    };

    const subscriptionOptions = {
      scales: {
        yAxes: [
          {
            ticks: {
              beginAtZero: true
            }
          }
        ]
      }
    };

    const subscriptionTotalData = {
      labels: this.state?.subscriptionsTotalByWeek?.map(ele => {
        return ele.orderCreatedAt;
      }),
      datasets: [
        {
          label: 'Total Subscriptions',
          fill: true,
          backgroundColor: "rgba(75,192,192,0.2)",
          borderColor: "rgba(75,192,192,1)",
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
          data: this.state?.subscriptionsTotalByWeek?.map(ele => {
            return ele.total;
          })
        }
      ]
    };

    let ui;
    ui = (
      <Fragment>
        <ReactCSSTransitionGroup
          component="div"
          transitionName="TabsAnimation"
          transitionAppear
          transitionAppearTimeout={0}
          transitionEnter={false}
          transitionLeave={false}
        >
          <ReactTooltip effect="solid" delayUpdate={500} html={true} place={'top'} border={true} type={'info'}
                        multiline="true"/>
          <PageTitle
            heading="Analytics"
            subheading="Key performance metrics and analytics, for products, customers, revenue, and growth."
            icon="pe-7s-graph3 icon-gradient bg-mean-fruit"
            sticky={true}
            customComponent={
              <>
                <div className="btn-actions-pane-right text-capitalize">
                  <div className='d-flex align-items-center'>
                    <div>
                      {
                        this.state.customDatePickerToggle &&
                        <span className="d-inline-block mb-3">
                        <DatePickerComponent selectedDate={this.state.fromDay} onSelectDay={this.handleFromDays}/>
                      </span>
                      }
                    </div>
                    <div>
                      {
                        this.state.customDatePickerToggle &&
                        <span className="d-inline-block ml-2 mb-3">
                        <DatePickerComponent selectedDate={this.state.toDay} onSelectDay={this.handleToDays}/>
                      </span>
                      }
                    </div>
                    <div style={{width: '230px'}}>
                    <span className="d-inline-block ml-2 mb-3" style={{width: '100%'}}>
                        <Select
                          options={options}
                          value={options.filter(option => option.value === this.state.selectedDays)}
                          onChange={this.handleChangeStatisticsDays}
                          styles={selectCustomStyles}
                        />
                      </span>
                    </div>
                  </div>
                </div>
              </>
            }
          />
          <Container fluid>
              <Card className="main-card mb-3">
              <CardBody>
                <Row>
                <Col lg={6} sm={6}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>From </b>
                          </div>
                          {this.state.fromDate && (
                            <div style={{fontSize: '1.0rem'}}>
                              {convertToShopTimeZoneDate(this.state?.fromDate, this.props.shopInfo.ianaTimeZone)} {"("} {this.props.shopInfo.ianaTimeZone} {")"}
                          </div>
                          )}
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col lg={6} sm={6}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>To </b>
                          </div>
                          {this.state.toDate && (
                            <div style={{fontSize: '1.0rem'}}>
                              {convertToShopTimeZoneDate(this.state?.toDate, this.props.shopInfo.ianaTimeZone)} {"("} {this.props.shopInfo.ianaTimeZone} {")"}
                          </div>
                          )}
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                </Row>
                <Row>
                  <Col lg={3} sm={6}>
                    <div className="card no-shadow bg-transparent widget-chart  p-0   text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>Total Recurring Order Amount</b>
                          </div>
                          <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                            {this.extractTextFromHtml(this.state?.totalOrderAmount)?.toLocaleString()}
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col lg={3} sm={6}>
                    <div className="card no-shadow bg-transparent widget-chart  p-0   text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>
                              Total Subscribed Amount <HelpTooltip>Total First Time Order Amount</HelpTooltip>
                            </b>
                          </div>
                          <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                            {this.extractTextFromHtml(this.state?.totalSubscribedAmount)?.toLocaleString()}
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col lg={3} sm={6}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>
                              Total Orders <HelpTooltip>First Time + Recurring Orders</HelpTooltip>
                            </b>
                          </div>
                          <div className="widget-numbers text-info" style={{fontSize: '2.0rem'}}>
                            {this.state?.totalOrderCount?.toLocaleString()}
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col lg={3} sm={6}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>
                              Total Subscriptions{' '}
                              <HelpTooltip>Total number of Subscription contracts created during selected period</HelpTooltip>

                            </b>
                          </div>
                          <div className="widget-numbers text-info" style={{fontSize: '2.0rem'}}>
                            {this.state?.totalSubscriptionCount?.toLocaleString()}
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                </Row>

                <Row>
                  <Col md={6} sm={12} xs={12}>
                    <Card className="main-card" style={{marginBottom: '20px'}}>
                      <CardBody>
                        <CardTitle>
                          Order Amount Per Week  <HelpTooltip>First Time + Recurring Orders</HelpTooltip>
                          <span id="activeUsersToolTip">
                            <HelpCircleOutline width="20px" height="20px" color="#fff" className="help_icon"/>
                          </span>
                        </CardTitle>
                        <Line type='line' data={orderSumData} options={historicalOptions}/>
                        {/* <Line height={window.innerWidth > 1000 ? 60 : 200} data={orderSumData} /> */}
                      </CardBody>
                    </Card>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <Card className="main-card" style={{marginBottom: '20px'}}>
                      <CardBody>
                        <CardTitle>
                          Subscriptions Per Week
                          <span id="activeUsersToolTip">
                            <HelpCircleOutline width="20px" height="20px" color="#fff" className="help_icon"/>
                          </span>
                        </CardTitle>
                        <Line data={subscriptionTotalData} options={subscriptionOptions}/>
                        {/* <Line height={window.innerWidth > 1000 ? 60 : 200} data={subscriptionTotalData} /> */}
                      </CardBody>
                    </Card>
                  </Col>
                </Row>
                <Row>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart  p-0   text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>Revenue Growth Month Over Month</b>
                          </div>
                          <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                            {this.extractTextFromHtml(this.state?.revenueGrowthMonthOverMonth)?.toLocaleString()}
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>
                              Total Active Customers <HelpTooltip>Customers that currently have active subscriptions</HelpTooltip>

                            </b>
                          </div>
                          <div className="widget-numbers text-secondary" style={{fontSize: '2.0rem'}}>
                            {this.state?.totalCustomerCount?.toLocaleString()}
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>

                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>Average Subscription Time(Days)</b>
                          </div>
                          <div className="widget-numbers text-info" style={{fontSize: '2.0rem'}}>
                            {this.state?.averageSubscriptionValue == null ? 'Infinity' : this.state?.averageSubscriptionValue.toLocaleString()}
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>Subscription Growth Month Over Month</b>
                          </div>
                          <div className="widget-numbers text-info" style={{fontSize: '2.0rem'}}>
                            {this.state?.subscriptionGrowthMonthOverMonth?.toLocaleString()}
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <div>
                            <b>Average Order Value</b>
                          </div>
                          <div className="widget-numbers text-secondary" style={{fontSize: '2.0rem'}}>
                            {this.extractTextFromHtml(this.state?.averageOrderValue)?.toLocaleString()}
                          </div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <CardTitle>New Active Subscriptions</CardTitle>
                          <div
                            className="widget-numbers text-info">{this.state?.newSubscription?.toLocaleString()}</div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <CardTitle><span id="activeUsersToolTip">
                          </span>Total Recurring Orders
                          </CardTitle>
                          <div className="widget-numbers text-primary">{this.state?.totalRecurringOrderCount?.toLocaleString()}</div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <CardTitle><span id="activeUsersToolTip">
                          </span>Approval Rate <HelpTooltip>(Total Successful Orders in given period / Total Orders including failed and successful order) * 100</HelpTooltip>
                          </CardTitle>
                          <div className="widget-numbers text-primary">{this.state?.approvalRate?.toLocaleString()}</div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <CardTitle><span id="activeUsersToolTip">
                          </span>Total Skipped Orders <HelpTooltip>Orders skipped due to manual skipping, dunning management and inventory management</HelpTooltip>
                          </CardTitle>
                          <div className="widget-numbers text-primary">{this.state?.totalSkippedOrders?.toLocaleString()}</div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <CardTitle><span id="activeUsersToolTip">
                          </span>Churn Rate <HelpTooltip>(Subscription lost due to cancellations during a given period/ Total subscription upfront for renewal at the start of that period) * 100</HelpTooltip>
                          </CardTitle>
                          <div className="widget-numbers text-primary">{this.state?.churnRate?.toLocaleString()}</div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                        <CardTitle><span id="activeUsersToolTip">
                          </span>Total Failed Recurring Orders
                          </CardTitle>
                          <div className="widget-numbers text-primary">{this.state?.totalFailedPaymentsCount?.toLocaleString()}</div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <CardTitle><span id="activeUsersToolTip">
                          </span>Cancellation Rate <HelpTooltip>((Total count of Subscription in given period - Active subscriptions given period) / Total count of Subscription in given period)  * 100</HelpTooltip>
                          </CardTitle>
                          <div className="widget-numbers text-primary">{this.state?.cancellationRate?.toLocaleString()}</div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                  <Col md={6} sm={12} xs={12}>
                    <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                      <Card className="main-card mb-3" style={{width: '100%'}}>
                        <CardBody>
                          <CardTitle><span id="activeUsersToolTip">
                          </span>Average Successful Orders Before Cancellation<HelpTooltip>Average successful orders for subscription cancelled during given period</HelpTooltip>
                          </CardTitle>
                          <div className="widget-numbers text-primary">{this.state?.averageOrdersBeforeCancellation?.toLocaleString()}</div>
                        </CardBody>
                      </Card>
                    </div>
                  </Col>
                </Row>
              </CardBody>
            </Card>

            <Row>
              <Col md={8} sm={12} xs={12}>
                <Card className="main-card" style={{marginBottom: '20px'}}>
                  <CardBody>
                    <CardTitle>
                      Subscription Vs Unsubscription
                      <span id="activeUsersToolTip">
                        <HelpCircleOutline width="20px" height="20px" color="#fff" className="help_icon"/>
                      </span>
                    </CardTitle>
                    <Line data={subscriptionVsUnsubscriptionData} options={subscriptionOptions}/>
                    {/* <Line height={window.innerWidth > 1000 ? 60 : 200} data={subscriptionTotalData} /> */}
                  </CardBody>
                </Card>
              </Col>
              <Col md={4} sm={12} xs={12}>
                <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                  <Card className="main-card mb-3" style={{width: '100%'}}>
                    <CardBody>
                      <div>
                        <b>Total Active Subscriptions</b>
                      </div>
                      <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                        {this.state?.totalActiveSubscriptionCount?.toLocaleString()}
                      </div>
                    </CardBody>
                  </Card>
                </div>
                <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                  <Card className="main-card mb-3" style={{width: '100%'}}>
                    <CardBody>
                      <div>
                        <b>Total Paused Subscriptions</b>
                      </div>
                      <div className="widget-numbers  text-secondary" style={{fontSize: '2.0rem'}}>
                        {this.state?.totalPausedSubscriptionCount?.toLocaleString()}
                      </div>
                    </CardBody>
                  </Card>
                </div>
                <div className="card no-shadow bg-transparent widget-chart p-0  text-right">
                  <Card className="main-card mb-3" style={{width: '100%'}}>
                    <CardBody>
                      <div>
                        <b>Total Cancelled Subscriptions</b>
                      </div>
                      <div className="widget-numbers text-primary" style={{fontSize: '2.0rem'}}>
                        {this.state?.totalCanceledSubscriptionCount?.toLocaleString()}
                      </div>
                    </CardBody>
                  </Card>
                </div>
              </Col>
            </Row>

            <Card className="main-card mb-3">
              <CardBody>
                <Row>
                  <Col md={8} sm={12} xs={12}>
                    <Card className="main-card" style={{marginBottom: '20px'}}>
                      <CardBody>
                        <CardTitle>
                          Historical vs Estimated Revenue
                          <span id="activeUsersToolTip">
                            <HelpCircleOutline width="20px" height="20px" color="#fff" className="help_icon"/>
                          </span>
                        </CardTitle>
                        <Line data={historicalData} options={historicalOptions}/>
                      </CardBody>
                    </Card>
                  </Col>
                  <Col md={4} sm={12} xs={12}>
                    <div
                      className="card mb-3 widget-chart widget-chart2 text-left card-btm-border card-shadow-success border-success">
                      <div className="widget-chat-wrapper-outer">
                        <div className="widget-chart-content pt-3 pl-3 pb-1">
                          <div className="widget-chart-flex">
                            <div className="widget-numbers">
                              <div className="widget-chart-flex">
                                <div className="fsize-4" style={{display: 'flex', alignItems: 'center'}}>
                                  {/* <small className="opacity-5">$ </small> */}
                                  <div
                                    className="widget-numbers text-primary"
                                    style={{
                                      fontSize: '35px',
                                      marginLeft: '7%'
                                    }}
                                  >
                                    {this.extractTextFromHtml(this.state?.nextSevenDayEstimatedRevenueTotal)
                                      ?.toLocaleString()
                                    }
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                          <h6 className="widget-subheading mb-0 opacity-5"><span id="activeUsersToolTip">
                          </span> Next 7 Day Estimated Revenue <HelpTooltip>Based on your next 7 days queued orders.</HelpTooltip>
                          </h6>
                        </div>
                      </div>
                    </div>

                    <div
                      className="card mb-3 widget-chart widget-chart2 text-left card-btm-border card-shadow-success border-success">
                      <div className="widget-chat-wrapper-outer">
                        <div className="widget-chart-content pt-3 pl-3 pb-1">
                          <div className="widget-chart-flex">
                            <div className="widget-numbers">
                              <div className="widget-chart-flex">
                                <div className="fsize-4" style={{display: 'flex', alignItems: 'center'}}>
                                  {/* <small className="opacity-5">$ </small> */}
                                  <div
                                    className="widget-numbers text-primary"
                                    style={{
                                      fontSize: '35px',
                                      marginLeft: '7%'
                                    }}
                                  >
                                    {this.extractTextFromHtml(this.state?.nextThirtyDayEstimatedRevenueTotal)
                                      ?.toLocaleString()
                                    }
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                          <h6 className="widget-subheading mb-0 opacity-5"><span id="activeUsersToolTip">
                          </span> Next 30 Days Estimated Revenue <HelpTooltip>Based on your next 30 days queued orders.</HelpTooltip>
                          </h6>
                        </div>
                      </div>
                    </div>
                    <div
                      className="card mb-3 widget-chart widget-chart2 text-left card-btm-border card-shadow-success border-success">
                      <div className="widget-chat-wrapper-outer">
                        <div className="widget-chart-content pt-3 pl-3 pb-1">
                          <div className="widget-chart-flex">
                            <div className="widget-numbers">
                              <div className="widget-chart-flex">
                                <div className="fsize-4" style={{display: 'flex', alignItems: 'center'}}>
                                  {/* <small className="opacity-5">$ </small> */}
                                  <div
                                    className="widget-numbers text-primary"
                                    style={{
                                      fontSize: '35px',
                                      marginLeft: '7%'
                                    }}
                                  >
                                    {this.extractTextFromHtml(this.state?.nextNinetyDayEstimatedRevenueTotal)
                                      ?.toLocaleString()
                                    }
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                          <h6 className="widget-subheading mb-0 opacity-5"><span id="activeUsersToolTip">
                          </span> Next 90 Day Estimated Revenue <HelpTooltip>Based on your next 90 days queued orders.</HelpTooltip>
                          </h6>
                        </div>
                      </div>
                    </div>
                  </Col>
                </Row>
              </CardBody>
            </Card>
            <hr/>
            <ProductRevenueReport
              totalSubscribedAmount={this.state.totalSubscribedAmount}
              moneyFormat={this.state.moneyFormat}
              currencyCode={this.state.currencyCode}
              productRevenueAnalyticsListData={this.props.productRevenueData}
              productRevenueLoading={this.props.productRevenueLoading}
            />
            <hr/>
            <Card>
              <CardBody>
                {this.state.productDeliveryAnalyticsList.length > 0 && (
                  <>
                    <CardTitle>Product Delivery Forecasting
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
                      <Button color="primary" className="ladda-button undefined btn btn-shadow " onClick={() => this.setState({isModalOpen: !this.state.isModalOpen})} >Export</Button>
                    </div>
                    </CardTitle>
                    <Modal isOpen={this.state.isModalOpen} toggle={() => this.setState({isModalOpen: !this.state.isModalOpen})} backdrop>
                      <ModalHeader>Export Delivery Forecast List</ModalHeader>
                      <ModalBody>
                        <Label>Email id</Label>
                        <Input
                          type="email"
                          invalid={!this.state.emailValidity}
                          onBlur={event => {
                            this.setState({inputValueForTestEmailId: event.target.value});
                            this.checkEmailValidity(event.target.value);
                            this.setState({blurred: true})
                          }}
                          onInput={event => {
                            if (this.state.blurred) {
                              this.setState({inputValueForTestEmailId: event.target.value});
                              this.checkEmailValidity(event.target.value);
                            }
                          }}
                          placeholder="Please enter email id here"
                        />
                        <FormFeedback>Please Enter a valid email id</FormFeedback>
                        <br/>
                      </ModalBody>
                      <ModalFooter>
                        <Button color="secondary" onClick={() => {
                          this.cleanupBeforeModalClose()
                        }}>
                          Cancel
                        </Button>
                        <MySaveButton
                          onClick={() => {
                            this.sendDeliveryForcastExportMail(this.state.inputValueForTestEmailId)
                          }}
                          text="Send Email"
                          updating={this.state.emailSendingProgress}
                          updatingText={'Sending'}
                        />
                      </ModalFooter>
                    </Modal>

                    <SweetAlert
                      title="Export Request Submitted"
                      confirmButtonColor=""
                      show={this.state.emailSuccessAlert}
                      text="Export may take time. Rest assured, once it's processed, it will be emailed to you."
                      type="success"
                      onConfirm={() =>this.setState({emailSuccessAlert: false})}
                    />
                    <SweetAlert
                      title="Failed"
                      confirmButtonColor=""
                      show={this.state.emailFailAlert}
                      type="error"
                      onConfirm={() => this.setState({emailFailAlert: false})}
                    />
                    <Table>
                      <thead>
                      <tr>
                        <th>
                          <span>Product</span>
                          <span className="text-primary">{` => Variant`}</span>
                        </th>
                        <th>Next 7 Days</th>
                        <th>Next 30 Days</th>
                        <th>Next 90 Days</th>
                        <th>Next 365 Days</th>
                      </tr>
                      </thead>
                      <tbody>
                      {this.state.productDeliveryAnalyticsList?.map((productDeliveryAnalytics, index) => (
                        <tr>
                          <th scope="row">
                            <span>{productDeliveryAnalytics.title}</span>
                            <span className="text-primary">
                                {productDeliveryAnalytics.variantTitle ? ` => ${productDeliveryAnalytics.variantTitle}` : ``}
                              </span>
                          </th>
                          <td>{productDeliveryAnalytics?.deliveryInNext7Days}</td>
                          <td>{productDeliveryAnalytics?.deliveryInNext30Days}</td>
                          <td>{productDeliveryAnalytics?.deliveryInNext90Days}</td>
                          <td>{productDeliveryAnalytics?.deliveryInNext365Days}</td>
                        </tr>
                      ))}
                      </tbody>
                    </Table>
                  </>
                )}
              </CardBody>
            </Card>
          </Container>
        </ReactCSSTransitionGroup>
      </Fragment>
    );

    return ui;
  }
}

const mapStateToProp = state => ({
  account: state.authentication.account,
  productRevenueData: state.subscriptionContractDetails.productRevenueData,
  productRevenueLoading: state.subscriptionContractDetails.productRevenueLoading,
  shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
  getProductRevenueFromSubscriptionContracts
};

export default connect(mapStateToProp, mapDispatchToProps)(Analytics);
