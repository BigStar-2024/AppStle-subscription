import React, {useEffect} from 'react';
import {Button, Table} from 'reactstrap';
import './PlanTable.scss'

export default function PlanTable(props) {
  const {planInfoList, isAnnual, onActivate, paymentPlanEntity} = props;

  const priceArray = planInfoList.map(plan => {

    let monthlyPriceOfYearlPlan = plan.price / 12;
    // if(plan?.discountPrice)
    // {
    //   monthlyPriceOfYearlPlan = parseFloat(plan?.discountPrice) / 12;
    // }

    var word = plan.planType === 'MONTHLY' ? 'month' : 'year (' + monthlyPriceOfYearlPlan + '/month)';

    if (plan.basePlan === 'ENTERPRISE_PLUS' || plan.basePlan === 'ENTERPRISE_PLUS_ANNUAL') {
      word = 'Custom'
    }


    return {
      planId: plan?.id,
      price: plan?.price,
      word: word,
      discountPrice: plan?.discountPrice
    };
    // `${plan.price}/${word}`
  });

  useEffect(() => {

  }, [planInfoList])

  const noOfSubscriptionsArray = planInfoList.map(plan => {
    return JSON.parse(plan.additionalDetails).subscriptionCount;
  });

  const orderAmountArray = planInfoList.map(plan => {
    return JSON.parse(plan.additionalDetails).subscriptionOrderAmount;
  });

  const plansInSeq = ['FREE STARTER', 'BUSINESS', 'BUSINESS PREMIUM', 'ENTERPRISE', 'ENTERPRISE_PLUS']

  const isFeatureAvailableForPlan = (feature, basePlan) => {
    var minPlan = featureMinPlanMap[feature]
    basePlan = basePlan.replace('_ANNUAL', '');

    return (plansInSeq.indexOf(minPlan) <= plansInSeq.indexOf(basePlan)) ? true : false
  }

  var featureMinPlanMap = {
    "0% Transaction Fee": 'FREE',
    "Dedicated 24*7*365 Support from Merchant Success Team": 'FREE',
    "Regular & Prepaid Plans": 'FREE',
    "Robust Dashboard Analytics": 'FREE',
    "Unlimited Email Notifications": 'FREE',

    "Weekly Summary Reports": 'FREE',
    "Customer-Segment Based Subscription Plans": 'FREE',
    "Order & Dunning Management": 'FREE',
    "Manage & Modify Subscriptions": 'FREE',
    "Third-party App Integrations": 'FREE',
    "Widget Display Mode": 'FREE',

    "Customer Portal Settings": 'FREE',
    "Subscription Widget on Collection/Landing and HomePage": 'FREE',
    "Custom Shipping Profiles for Subscriptions": 'FREE',
    "Custom Email HTML": 'FREE',
    "Cancellation Management": 'FREE',
    "Cart Widget Placement": 'FREE',
    "Product Swap Automation": 'FREE',

    "Custom Email Domain": 'FREE',
    "Advanced Customer Portal": 'FREE',
    "Build-a-Box": 'FREE',
    "Targeted Upselling": 'FREE',
    "Custom Development (with custom pricing)": 'FREE',
    "Bulk Automations": 'FREE',
    "Activity Log": 'FREE',
    "Robust APIs & Webhooks": 'FREE',
    "Cart Widget Display": 'FREE',
    "Quick View Widget Display": 'FREE',
    "Quick Actions": 'FREE',

    "Dedicated Cloud and Storage Infrastructure": 'ENTERPRISE_PLUS',
    "1:1 On-boarding Consultatation": 'ENTERPRISE_PLUS',
    "Quick Action Settings To Control Churn": 'ENTERPRISE_PLUS',
    "Advance Analytics for In-depth Insights": 'ENTERPRISE_PLUS',
    "Appstle Loyalty & Wallet Share App Portfolio": 'ENTERPRISE_PLUS',
    "Custom Integration with Internal Systems (e.g. internal ERP/CRM systems)": 'ENTERPRISE_PLUS',
    "Dedicated Merchant Success Manager": 'ENTERPRISE_PLUS',
    "Quarterly Review Calls with Lead Developer": 'ENTERPRISE_PLUS',
    "1:1 Quarterly Strategy Discussion": 'ENTERPRISE_PLUS',
    "Phone Support (coming soon)": 'ENTERPRISE_PLUS'
  }

  return (
    <Table bordered className="mb-0">
      <thead>
      <tr>
        <th style={{width:"20%"}}></th>
        {planInfoList.map((plan, index) => {
          return (
            <th key={index} style={{width:"16%"}}>
              {plan.name}
            </th>
          );
        })}
      </tr>
      </thead>
      <tbody>
      <tr>
        <th className='p-0'></th>
        {priceArray.map((data, index) => {
          if (data?.word === 'Custom') {
            return <td className='p-0' key={index}><span>Custom</span></td>
          } else if (data?.discountPrice && !(paymentPlanEntity !== null && paymentPlanEntity.planInfo !== null && paymentPlanEntity.planInfo.id === data?.planId)) {
            return <td className='p-0' key={index}>
              <div>${data?.discountPrice}/{data?.word}</div>
              <div className='strike-discount-price'>${data?.price}/{data?.word}</div>
            </td>
          } else {
            return <td className='p-0' key={index}><span>${data?.price}</span>
              <span>/{data?.word}</span></td>
          }

        })}
      </tr>
      <tr>
        <th className='p-0'></th>
        <td className='p-0' hidden={isAnnual}>0% Transaction Fee</td>
        <td className='p-0'>0% Transaction Fee</td>
        <td className='p-0'>0% Transaction Fee</td>
        <td className='p-0'>0% Transaction Fee</td>
        <td className='p-0'>0% Transaction Fee</td>
      </tr>
      <tr>
      <th className='p-0'></th>
        {/* <th style={{verticalAlign: 'middle'}}>START YOUR FREE TRIAL</th> */}
        {planInfoList.map((plan, index) => {
          return (
            <td className='p-0' key={index}>
              {(plan.basePlan === 'ENTERPRISE_PLUS' || plan.basePlan === 'ENTERPRISE_PLUS_ANNUAL')
                ? <Button className="my-2" color={'primary'}
                          onClick={() => {
                            Intercom('showNewMessage', 'I need some help with Enterprise Plus plan. Can you please help?');
                          }}>Contact Us for Price</Button>
                : <Button
                  disabled={
                    paymentPlanEntity !== null && paymentPlanEntity.planInfo !== null && paymentPlanEntity.planInfo.id === plan.id && !props?.trialExpired && !paymentPlanEntity?.testCharge
                  }
                  key={index}
                  color={
                    paymentPlanEntity !== null && paymentPlanEntity.planInfo !== null && paymentPlanEntity.planInfo.id === plan.id && !props?.trialExpired
                      ? 'success'
                      : 'primary'
                  }
                  className="my-2"
                  onClick={() => {
                    onActivate(plan);
                  }}
                >
                  {props?.trialExpired ? "ACTIVATE" : (paymentPlanEntity !== null && paymentPlanEntity.planInfo !== null && paymentPlanEntity.planInfo.id === plan.id
                      ? 'Activated'
                      : <div>
                        {plan.price > 0
                          ? <span>START FREE TRIAL<br/>(For {plan.trialDays} Days)</span>
                          : 'ACTIVATE'}
                      </div>
                  )}
                </Button>
              }
            </td>
          );
        })}
      </tr>
      </tbody>
    </Table>
  );
}
