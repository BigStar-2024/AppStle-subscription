import React, {useEffect, useState} from 'react';
import {connect} from 'react-redux';
import BlockUi from '@availity/block-ui';
import {Link} from 'react-router-dom';
import {getPaymentPlanByShop, getPaymentPlanLimitInformation} from 'app/entities/payment-plan/payment-plan.reducer';
import {Lock} from '@mui/icons-material';
import {
  Button
} from 'reactstrap';
import "./feature-access.scss";
import {getBillingUrl} from 'app/entities/payment-plan/payment-billing.reducer';
import Loader from 'react-loaders';

export const featureAccessCheck = (props) => {

  const {
    getPaymentPlanByShop,
    getPaymentPlanLimitInformation,
    paymentPlanData,
    planInfoList,
    hasAnyAuthorities,
    getBillingUrl,
    planInfoLoading,
    setIsPageAccessible
  } = props
  useEffect(() => {
    if (paymentPlanData.isValidData === false) {
      getPaymentPlanByShop();
      getPaymentPlanLimitInformation();
    }
  }, [])
  const [planName, setPlanName] = useState("");
  const [basePlan, setBasePlan] = useState("");
  const [planId, setPlanId] = useState(null);

  useEffect(() => {
    if (!props.isAuthorized) {
      const sortedPlanInfoList = [].concat(planInfoList).sort((a, b) => a.id > b.id ? 1 : -1);
      let avalableAccessPlanInfoList = sortedPlanInfoList?.filter(planInfo => JSON.parse(planInfo?.additionalDetails)[hasAnyAuthorities]);
      if (avalableAccessPlanInfoList.length > 0) {
        let suggestionPlanIndex = getPlanInfoIndex(avalableAccessPlanInfoList[0]?.basePlan);
        let currantPlanIndex = getPlanInfoIndex(paymentPlanData?.basePlan);
        if (currantPlanIndex < suggestionPlanIndex) {
          setPlanName(avalableAccessPlanInfoList[0]?.name);
          setPlanId(avalableAccessPlanInfoList[0]?.id);
          setBasePlan(avalableAccessPlanInfoList[0]?.basePlan);
          console.log('basePlan=' + sortedPlanInfoList[0]?.basePlan);
        } else {
          if (sortedPlanInfoList.length > (currantPlanIndex + 2)) {
            currantPlanIndex++;
            setPlanName(sortedPlanInfoList[currantPlanIndex]?.name);
            setPlanId(sortedPlanInfoList[currantPlanIndex]?.id);
            setBasePlan(sortedPlanInfoList[currantPlanIndex]?.basePlan);
            console.log('basePlan=' + sortedPlanInfoList[currantPlanIndex]?.basePlan);
          }
        }
      }

      if (setIsPageAccessible) {
        setIsPageAccessible(false);
      }

    }
  }, [planInfoList, props.isAuthorized]);

  const getPlanInfoIndex = (planBaseName) => {
    const plans = [].concat(planInfoList).sort((a, b) => a.id > b.id ? 1 : -1);
    return plans?.findIndex((e) => e.basePlan == planBaseName);
  }

  const gotoUpgradePlanPage = () => {
    if (basePlan === 'ENTERPRISE_PLUS' || basePlan === 'ENTERPRISE_PLUS_ANNUAL') {
      Intercom('showNewMessage', 'I would like to upgrade to ' + planName + ' plan');
    } else if (planId) {
      getBillingUrl(planId, "", window.__SHOPIFY_DEV_HOST)
    }
  }

  useEffect(() => {
    if (props.billingUrl != null) {
      if (!window['app'] && window.__SHOPIFY_DEV_HOST) {
        window['app'] = createApp({
          apiKey: API_KEY,
          host: window.__SHOPIFY_DEV_HOST
        });
      }
      if (window.app) {
        Redirect.create(app).dispatch(Redirect.Action.REMOTE, props.billingUrl.billingUrl);
      } else {
        window.location = props.billingUrl.billingUrl;
      }
    }
  }, [props.billingUrl]);


  return (
    !props.isAuthorized ?
      <BlockUi
        style={{zIndex: 3}}
        tag="div"
        blocking
        title={`Upgrade your plan to ${planName} to enable this feature.`}
        className="custom-feature"
        loader={
          !planId ?
            <Link className="" to={`/dashboards/billing`}>
              <Button>
                <Lock title={props?.upgradeButtonText} color='error' fontSize='small' style={{fontSize: '16px'}}/>
                <span className="ml-2">{props?.upgradeButtonText}</span>
              </Button>
            </Link> :
            !planInfoLoading ?
              <Link className="" onClick={() => gotoUpgradePlanPage()}>
                <Button>
                  <Lock title={props?.upgradeButtonText} color='error' fontSize='small' style={{fontSize: '16px'}}/>

                  {basePlan === 'ENTERPRISE_PLUS' || basePlan === 'ENTERPRISE_PLUS_ANNUAL' ? (
                    <span className="ml-2">Please talk to support for the {planName} plan.</span>) : (
                    <span className="ml-2">Upgrade your plan to {planName} to enable this feature.</span>)}
                </Button>
              </Link> : <Loader type="line-scale"/>
        }
      >
        {props.children}
      </BlockUi>
      : props.children
  )
}

const hasAnyAuthority = (paymentPlanData, hasAnyAuthorities) => {
  if (hasAnyAuthorities === 'enableSmsAlert') {
    let paymentPlanDataObj = JSON.parse(paymentPlanData.additionalDetails);
    // Here it's check if hasAnyAuthorities has any key or key value is true
    if (Object.keys(paymentPlanDataObj).includes('enableSmsAlert')) {
      return true;
    }
  }

  if (paymentPlanData?.additionalDetails) {
    let paymentPlanDataObj = JSON.parse(paymentPlanData.additionalDetails);
    // Here it's check if hasAnyAuthorities has any key or key value is true
    let keyExist = Object.keys(paymentPlanDataObj).some(key => key === hasAnyAuthorities);
    if (!keyExist) {
      return true;
    }

    return paymentPlanDataObj[hasAnyAuthorities] == true
  }
  return false;
}


const mapStateToProps = (state, props) => ({
  paymentPlanData: state.paymentPlan.entity,
  isAuthorized: hasAnyAuthority(state.paymentPlan.entity, props.hasAnyAuthorities),
  planInfoList: state.planInfo.entities,
  billingUrl: state.billingUrl.confirmationUrl,
  planInfoLoading: state.billingUrl.loading
})

const mapDispatchToProps = {
  getPaymentPlanByShop,
  getPaymentPlanLimitInformation,
  getBillingUrl
}

export default connect(mapStateToProps, mapDispatchToProps)(featureAccessCheck)
