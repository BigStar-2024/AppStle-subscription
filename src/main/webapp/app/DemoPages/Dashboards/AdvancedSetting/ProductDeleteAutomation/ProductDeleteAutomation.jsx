import React, { useState, Fragment } from 'react';
import { connect } from 'react-redux';
import axios from 'axios';
import PrdVariantRadioPopup from "../PrdVariantRadioPopup";
import {
  Input,
  Label,
  FormGroup,
  Row,
  Col, FormText
} from 'reactstrap';
import Switch from 'react-switch';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import { toast } from 'react-toastify';

const ProductDeleteAutomation = props => {

  const [updating, setUpdating] = useState(false);
  const [selectedVariantId, setSelectedVariantId] = useState(false);
  const [removeUnAvailableVariant, setRemoveUnAvailableVariant] = useState(false);
  const [subscriptionsType, setSubscriptionsType] = useState(null)
  const [subscriptionId, setSubscriptionId] = useState(null)


  const onSubmit = () => {
    setUpdating(true);
    const allSubscriptionIds = subscriptionsType === "ALL_SUBSCRIPTIONS"
    axios.put(`/api/bulk-automations/delete-product?variantId=${selectedVariantId || ''}&deleteRemovedProductsFromShopify=${removeUnAvailableVariant}${subscriptionId ? `&contractIds=${subscriptionId}` :  ''}&allSubscriptions=${allSubscriptionIds ? true : false}`)
    .then(async res => {
      await props?.getAutomationStatus()
      toast.success("Bulk update process triggered !!");
      setUpdating(false)})
    .catch(error => {
      console.log(error)
      toast.error(error?.response?.data?.message || 'Something went wrong.');
      setUpdating(false)
      }
    )
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
        <Row>
          <Col md={6}>
            <FormGroup>
              <Label for={`subscriptionsType`}><b>Select Subscriptions Type</b></Label>
              <Input
                type="select"
                id={`subscriptionsType`}
                name={`subscriptionsType`}
                className="form-control"
                onChange={(e) => setSubscriptionsType(e.target.value)}
              >
                <option value="">Please Select</option>
                <option value="SUBSCRIPTION_ID">Subscription ID</option>
                <option value="ALL_SUBSCRIPTIONS">All Subscriptions</option>
              </Input>
            </FormGroup>
          </Col>
          <Col md={6}>
            {
              subscriptionsType === "SUBSCRIPTION_ID" && <FormGroup style={{marginBottom: '3px'}}>
                <Label for="subscriptionID">Comma-Separated Subscription Ids</Label>
                <Input
                  id="subscriptionID"
                  className="form-control"
                  type="text"
                  name="subscriptionID"
                  placeholder="Enter subscription Id"
                  onChange={(e) => setSubscriptionId(e.target.value)}
                />
                <FormText color="muted">
                  Please enter comma-separated subscription IDs. eg. 111111, 222222, 333333, ...
                </FormText>
              </FormGroup>
            }
          </Col>
        </Row>
        <Row className="align-items-center">
          <Col md={5}>
          <FormGroup style={{marginBottom: '2px'}}>
            <Label for="variantId">Variant Id</Label>
              <Input
                value ={selectedVariantId || ''}
                onChange={(event) => setSelectedVariantId(event.target.value)}
                id="variantId"
                className="form-control"
                type="text"
                name="variantId"
                placeholder="Enter Variant Id"
            />
            </FormGroup>
              <PrdVariantRadioPopup
                onChange={(selectData) => { setSelectedVariantId(selectData.id)}}
                isSource={false}
                totalTitle="select product variant"
                index={1}
                methodName="Save"
                buttonLabel="select product variant"
                header="Product Variants"
              />
          </Col>
          <Col md={1}>
            <div style={{fontSize: '18px', fontWeight: 'bold'}}>OR</div>
          </Col>
          <Col md={6}>
            <FormGroup style={{display: "flex", marginTop: '30px'}} >
            <Label for="removeUnAvailableVariant"></Label>
                    <Switch
                      checked={removeUnAvailableVariant}
                      value={removeUnAvailableVariant}
                      onColor="#3ac47d"
                      onChange={(value) => setRemoveUnAvailableVariant(value)}
                      handleDiameter={20}
                      boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                      activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                      name="removeUnAvailableVariant"
                      height={17}
                      width={36}
                      id="material-switch"
                    />
                <Label for="removeUnAvailableVariant"
                        style={{marginBottom: "0rem", marginLeft: "2rem"}}>Want to remove all the products those are removed from shopify store ?</Label>
              </FormGroup>
          </Col>
          <Col md={4} className="mt-2 mb-2">
            <MySaveButton
              text="Start Product Delete Automation"
              updating={updating}
              updatingText={'Processing'}
              className="btn-danger"
              onClick={onSubmit}
              disabled = {!(selectedVariantId || removeUnAvailableVariant)}
            >
              Start Product Delete Automation
            </MySaveButton>
          </Col>
        </Row>
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
});

const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(ProductDeleteAutomation);
