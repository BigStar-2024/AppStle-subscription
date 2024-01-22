import AnalyticsTabs from 'app/DemoPages/Dashboards/Analytics/AnalyticsTabs';
import { AnalyticsContextProps } from 'app/DemoPages/Dashboards/Analytics/types';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { getProductRevenueFromSubscriptionContracts } from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import { default as axios } from 'axios';
import React, { createContext, useEffect, useState } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect } from 'react-redux';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import { Container } from 'reactstrap';

const selectCustomStyles = {
  dropdownIndicator: (provided, state) => ({
    ...provided,
    marginTop: '5px !important',
  }),
};

export const options = [
  { value: 'CUSTOM_DATE', label: 'Custom Date' },
  { value: 0, label: 'Today' },
  { value: 1, label: 'Yesterday' },
  { value: 7, label: 'Last 7 Days' },
  { value: 30, label: 'Last 30 Days' },
  { value: 90, label: 'Last 3 Months' },
  { value: 180, label: 'Last 6 Months' },
  { value: 365, label: 'Last Year' },
  { value: 730, label: 'Last 2 Years' },
  { value: 1095, label: 'Last 3 Years' },
  { value: 1460, label: 'Last 4 Years' },
  { value: 1825, label: 'Last 5 Years' },
] as const;

type SelectedOption = (typeof options)[number];

const DAYS = 'days';
const RANGE = 'range';

export function extractTextFromHtml(text: string) {
  let span = document.createElement('span');
  span.innerHTML = text;
  return span.textContent || span.innerText;
}

export const AnalyticsContext = createContext<AnalyticsContextProps>(null);

export function Analytics(props) {
  const [totalOrderAmount, setTotalOrderAmount] = useState(0);
  const [totalSubscribedAmount, setTotalSubscribedAmount] = useState(0);
  const [churnRate, setChurnRate] = useState(0);
  const [subscriptionGrowthMonthOverMonth, setSubscriptionGrowthMonthOverMonth] = useState(0);
  const [revenueGrowthMonthOverMonth, setRevenueGrowthMonthOverMonth] = useState(0);
  const [totalOrderCount, setTotalOrderCount] = useState(0);
  const [totalCanceledSubscriptionCount, setTotalCanceledSubscriptionCount] = useState(0);
  const [totalPausedSubscriptionCount, setTotalPausedSubscriptionCount] = useState(0);
  const [totalCustomerCount, setTotalCustomerCount] = useState(0);
  const [averageOrderValue, setAverageOrderValue] = useState(0);
  const [moneyFormat, setMoneyFormat] = useState('');
  const [orderSumByWeek, setOrderSumByWeek] = useState([]);
  const [subscriptionsTotalByWeek, setSubscriptionsTotalByWeek] = useState([]);
  const [selectedDays, setSelectedDays] = useState(90);
  const [fromDay, setFromDay] = useState(new Date());
  const [toDay, setToDay] = useState(new Date());
  const [filterBy, setFilterBy] = useState(DAYS);
  const [newSubscription, setNewSubscription] = useState(0);
  const [estimatedVsHistoricalRevenue, setEstimatedVsHistoricalRevenue] = useState([]);
  const [productDeliveryAnalyticsList, setProductDeliveryAnalyticsList] = useState([]);
  const [nextNinetyDayEstimatedRevenueTotal, setNextNinetyDayEstimatedRevenueTotal] = useState(0);
  const [nextThirtyDayEstimatedRevenueTotal, setNextThirtyDayEstimatedRevenueTotal] = useState(0);
  const [nextSevenDayEstimatedRevenueTotal, setNextSevenDayEstimatedRevenueTotal] = useState(0);
  const [totalSubscriptionCount, setTotalSubscriptionCount] = useState(0);
  const [totalActiveSubscriptionCount, setTotalActiveSubscriptionCount] = useState(0);
  const [totalFailedPaymentsCount, setTotalFailedPaymentsCount] = useState(0);
  const [averageSubscriptionValue, setAverageSubscriptionValue] = useState(0);
  const [subscribedVsUnsubscribed, setSubscribedVsUnsubscribed] = useState([]);
  const [customDatePickerToggle, setCustomDatePickerToggle] = useState(false);
  const [fromDate, setFromDate] = useState(null);
  const [toDate, setToDate] = useState(null);
  const [emailValidity, setEmailValidity] = useState(true);
  const [emailSendingProgress, setEmailSendingProgress] = useState(false);
  const [blurred, setBlurred] = useState(false);
  const [inputValueForTestEmailId, setInputValueForTestEmailId] = useState('');
  const [emailSuccessAlert, setEmailSuccessAlert] = useState(false);
  const [emailFailAlert, setEmailFailAlert] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [totalRecurringOrderCount, setTotalRecurringOrderCount] = useState(0);
  const [totalSkippedOrders, setTotalSkippedOrders] = useState(0);
  const [approvalRate, setApprovalRate] = useState(0);
  const [cancellationRate, setCancellationRate] = useState(0);
  const [currencyCode, setCurrencyCode] = useState('');
  const [loading, setLoading] = useState({
    analyticsCountersLoading: false,
    revenueLoading: false,
    subscriptionsLoading: false,
    overviewLoading: false,
  });

  // necessary for AnalyticsContext provider
  const context = {
    totalOrderAmount,
    setTotalOrderAmount,
    totalSubscribedAmount,
    setTotalSubscribedAmount,
    churnRate,
    setChurnRate,
    subscriptionGrowthMonthOverMonth,
    setSubscriptionGrowthMonthOverMonth,
    revenueGrowthMonthOverMonth,
    setRevenueGrowthMonthOverMonth,
    totalOrderCount,
    setTotalOrderCount,
    totalCanceledSubscriptionCount,
    setTotalCanceledSubscriptionCount,
    totalPausedSubscriptionCount,
    setTotalPausedSubscriptionCount,
    totalCustomerCount,
    setTotalCustomerCount,
    averageOrderValue,
    setAverageOrderValue,
    moneyFormat,
    setMoneyFormat,
    orderSumByWeek,
    setOrderSumByWeek,
    subscriptionsTotalByWeek,
    setSubscriptionsTotalByWeek,
    selectedDays,
    setSelectedDays,
    fromDay,
    setFromDay,
    toDay,
    setToDay,
    filterBy,
    setFilterBy,
    newSubscription,
    setNewSubscription,
    estimatedVsHistoricalRevenue,
    setEstimatedVsHistoricalRevenue,
    productDeliveryAnalyticsList,
    setProductDeliveryAnalyticsList,
    nextNinetyDayEstimatedRevenueTotal,
    setNextNinetyDayEstimatedRevenueTotal,
    nextThirtyDayEstimatedRevenueTotal,
    setNextThirtyDayEstimatedRevenueTotal,
    nextSevenDayEstimatedRevenueTotal,
    setNextSevenDayEstimatedRevenueTotal,
    totalSubscriptionCount,
    setTotalSubscriptionCount,
    totalActiveSubscriptionCount,
    setTotalActiveSubscriptionCount,
    totalFailedPaymentsCount,
    setTotalFailedPaymentsCount,
    averageSubscriptionValue,
    setAverageSubscriptionValue,
    subscribedVsUnsubscribed,
    setSubscribedVsUnsubscribed,
    customDatePickerToggle,
    setCustomDatePickerToggle,
    fromDate,
    setFromDate,
    toDate,
    setToDate,
    emailValidity,
    setEmailValidity,
    emailSendingProgress,
    setEmailSendingProgress,
    blurred,
    setBlurred,
    inputValueForTestEmailId,
    setInputValueForTestEmailId,
    emailSuccessAlert,
    setEmailSuccessAlert,
    emailFailAlert,
    setEmailFailAlert,
    isModalOpen,
    setIsModalOpen,
    totalRecurringOrderCount,
    setTotalRecurringOrderCount,
    totalSkippedOrders,
    setTotalSkippedOrders,
    approvalRate,
    setApprovalRate,
    cancellationRate,
    setCancellationRate,
    currencyCode,
    setCurrencyCode,
    handleFromDays,
    handleToDays,
    handleChangeStatisticsDays,
    selectCustomStyles,
    loading,
    setLoading,
    shopInfo: props.shopInfo,
    productRevenueData: props.productRevenueData,
    productRevenueLoading: props.productRevenueLoading,
  };

  useEffect(() => {
    getAnalyticsCounters();
    getOverviewTotalOrderAmountAnalyticsForShop();
    getSubscriptionsTotalOrderAmountAnalyticsForShop();
    getRevenueTotalOrderAmountAnalyticsForShop();
    getEstimatedHistoricalRevenue();
    getProductDeliveryAnalytics();
    getFutureUpcomingRevenue();
    getSubscribedVsUnsubscribed();
    productRevenueAnalyticsData();
  }, []);

  useEffect(() => {
    if (!customDatePickerToggle) {
      getAnalyticsCounters();
      getOverviewTotalOrderAmountAnalyticsForShop();
      getSubscriptionsTotalOrderAmountAnalyticsForShop();
      getRevenueTotalOrderAmountAnalyticsForShop();
      productRevenueAnalyticsData();
    }
  }, [customDatePickerToggle, selectedDays]);

  function productRevenueAnalyticsData() {
    props.getProductRevenueFromSubscriptionContracts(filterBy, selectedDays, fromDay, toDay);
  }

  async function getAnalyticsCounters() {
    setLoading(load => ({ ...load, analyticsCountersLoading: true }));
    try {
      const res = await axios.get('api/conversion-analytics/total-order-amount', {
        params: { filterBy, days: selectedDays, fromDay, toDay },
      });
      const { data } = res;

      setTotalCanceledSubscriptionCount(data?.canceledSubscriptionCount);
      setTotalPausedSubscriptionCount(data?.pausedSubscriptionCount);
      setChurnRate(data?.churnRate);
      setSubscriptionsTotalByWeek(data?.subscriptionsTotalByWeek);
      setMoneyFormat(data?.currency);
      setTotalActiveSubscriptionCount(data?.totalActiveSubscriptionCount);
      setTotalFailedPaymentsCount(data?.totalFailedPaymentsCount);
      setFromDate(data?.fromDate);
      setToDate(data?.toDate);
      setTotalRecurringOrderCount(data?.totalRecurringOrderCount);
      setTotalSkippedOrders(data?.totalSkippedOrders);
      setApprovalRate(data?.approvalRate);
      setCancellationRate(data?.cancellationRate);

      // setTotalOrderAmount(data.totalSold);
      // setTotalSubscribedAmount(data.totalSubscribed);
      // setTotalOrderCount(data.orderCount);
      // setSubscriptionGrowthMonthOverMonth(data.subscriptionGrowthMonthOverMonth);
      // setRevenueGrowthMonthOverMonth(data.revenueGrowthMonthOverMonth);
      // setAverageOrderValue(data.averageOrderValue);
      // setOrderSumByWeek(data.ordersByWeek.sort((a, b) => new Date(a.orderCreatedAt).getTime() - new Date(b.orderCreatedAt).getTime()));
      // setNewSubscription(data.newSubscription);
      // setTotalSubscriptionCount(data.totalSubscriptionCount);
      // setAverageSubscriptionValue(data.averageSubscriptionValue);
      // setTotalCustomerCount(data.customerCount);
    } catch (error) {
      // Handle errors here
      console.error('Error fetching analytics data:', error);
    } finally {
      setLoading(load => ({ ...load, analyticsCountersLoading: false }));
    }
  }

  async function getOverviewTotalOrderAmountAnalyticsForShop() {
    setLoading(load => ({ ...load, overviewLoading: true }));
    try {
      const response = await axios.get('api/conversion-analytics/overview/total-order-amount', {
        params: { filterBy, days: selectedDays, fromDay, toDay },
      });

      const { data } = response;
      setTotalOrderCount(data?.orderCount || 0);
      setTotalSubscriptionCount(data?.totalSubscriptionCount || 0);
      setTotalCustomerCount(data?.customerCount || 0);
      setNewSubscription(data?.newSubscription || 0);
    } catch (error) {
      // Handle error appropriately (e.g., show an error message)
      console.error('Error fetching data:', error);
    } finally {
      setLoading(load => ({ ...load, overviewLoading: false }));
    }
  }

  async function getRevenueTotalOrderAmountAnalyticsForShop() {
    setLoading(load => ({ ...load, revenueLoading: true }));
    try {
      const response = await axios.get('api/conversion-analytics/revenue/total-order-amount', {
        params: { filterBy, days: selectedDays, fromDay, toDay },
      });

      const { data } = response;
      setRevenueGrowthMonthOverMonth(data?.revenueGrowthMonthOverMonth || 0);
      setAverageOrderValue(data?.averageOrderValue || 0);

      // Sort orders by orderCreatedAt date
      const sortedOrdersByWeek = (data?.ordersByWeek || [])?.sort(
        (a, b) => new Date(a?.orderCreatedAt).getTime() - new Date(b?.orderCreatedAt).getTime(),
      );
      setOrderSumByWeek(sortedOrdersByWeek);
    } catch (error) {
      // Handle error appropriately (e.g., show an error message)
      console.error('Error fetching revenue analytics:', error);
    } finally {
      setLoading(load => ({ ...load, revenueLoading: false }));
    }
  }

  async function getSubscriptionsTotalOrderAmountAnalyticsForShop() {
    setLoading(load => ({ ...load, subscriptionsLoading: true }));
    try {
      const response = await axios.get('api/conversion-analytics/subscriptions/total-order-amount', {
        params: { filterBy, days: selectedDays, fromDay, toDay },
      });

      const data = response.data;

      setTotalSubscribedAmount(data?.totalSubscribed || 0);
      setTotalOrderAmount(data?.totalSold || 0);
      setAverageSubscriptionValue(data?.averageSubscriptionValue);
      setSubscriptionGrowthMonthOverMonth(data?.subscriptionGrowthMonthOverMonth || 0);
    } catch (error) {
      // Handle the error here (e.g., log it or show an error message to the user).
      console.error('Error fetching data:', error);
    } finally {
      setLoading(load => ({ ...load, subscriptionsLoading: false }));
    }
  }

  function getEstimatedHistoricalRevenue() {
    axios.get('api/conversion-analytics/estimated-historical-order-amount-total').then(response => {
      const { data } = response;
      setEstimatedVsHistoricalRevenue(data.estimatedVsHistoricalRevenue);
      setCurrencyCode(data.currencyCode);
    });
  }

  function getSubscribedVsUnsubscribed() {
    axios.get('api/conversion-analytics/subscription-unsubscription').then(response => {
      const { data } = response;
      setSubscribedVsUnsubscribed(data.subscriptionVsUnsubscription);
    });
  }

  function getProductDeliveryAnalytics() {
    axios
      .get(`api/subscription-contract-details/productDelivery-analytics`, {
        params: { filterBy, days: selectedDays, fromDay, toDay },
      })
      .then(response => {
        const { data } = response;
        setProductDeliveryAnalyticsList(data);
      });
  }

  function getFutureUpcomingRevenue() {
    axios.get('api/conversion-analytics/estimated-total-order-amount').then(response => {
      const { data } = response;
      setNextNinetyDayEstimatedRevenueTotal(data.nextNinetyDayEstimatedRevenueTotal);
      setNextThirtyDayEstimatedRevenueTotal(data.nextThirtyDayEstimatedRevenueTotal);
      setNextSevenDayEstimatedRevenueTotal(data.nextSevenDayEstimatedRevenueTotal);
    });
  }

  function handleChangeStatisticsDays(selectedDays: SelectedOption) {
    const { value } = selectedDays;
    if (value === 'CUSTOM_DATE') {
      setCustomDatePickerToggle(true);
      setSelectedDays(0);
    } else {
      setCustomDatePickerToggle(false);
      setSelectedDays(value);
      setFilterBy(DAYS);
    }
  }

  function handleFromDays(fromDay: Date) {
    if (fromDay <= toDay) {
      setFromDay(fromDay);
      setFilterBy(RANGE);
      getAnalyticsCounters();
      getOverviewTotalOrderAmountAnalyticsForShop();
      getRevenueTotalOrderAmountAnalyticsForShop();
      getSubscriptionsTotalOrderAmountAnalyticsForShop();
      productRevenueAnalyticsData();
    }
  }

  function handleToDays(toDay: Date) {
    if (toDay >= fromDay) {
      setToDay(toDay);
      setFilterBy(RANGE);
      getAnalyticsCounters();
      getOverviewTotalOrderAmountAnalyticsForShop();
      getRevenueTotalOrderAmountAnalyticsForShop();
      getSubscriptionsTotalOrderAmountAnalyticsForShop();
      productRevenueAnalyticsData();
    }
  }

  return (
    <AnalyticsContext.Provider value={context}>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <ReactTooltip place={'top'} />
        <PageTitle
          heading="Analytics"
          subheading="Key performance metrics and analytics, for products, customers, revenue, and growth."
          icon="pe-7s-graph3 icon-gradient bg-mean-fruit"
          sticky={true}
          tutorialButton={{
            show: true,
            videos: [
              {
                title: 'Analytics and Reports',
                url: 'https://www.youtube.com/watch?v=RKH1t3-wZCc',
              },
            ],
            docs: [
              {
                title: 'Analytics and Reports',
                url: 'https://intercom.help/appstle/en/articles/8392402-analytics-and-reports',
              },
            ],
          }}
        />
        <Container fluid>
          <AnalyticsTabs {...props} />
        </Container>
      </ReactCSSTransitionGroup>
    </AnalyticsContext.Provider>
  );
}

const mapStateToProps = state => ({
  account: state.authentication.account,
  productRevenueData: state.subscriptionContractDetails.productRevenueData,
  productRevenueLoading: state.subscriptionContractDetails.productRevenueLoading,
  shopInfo: state.shopInfo.entity,
});

const mapDispatchToProps = {
  getProductRevenueFromSubscriptionContracts,
};

export default connect(mapStateToProps, mapDispatchToProps)(Analytics);
