import React, {useEffect, useState} from 'react';
import {Button, Col, FormGroup, Input, InputGroup, InputGroupAddon, InputGroupText, Label, Row} from 'reactstrap';
import {Field, FormSpy} from 'react-final-form';
import {connect} from 'react-redux';
import {
  createEntity,
  getEntity,
  getUsedProducts,
  reset,
  updateEntity
} from 'app/entities/subscription-group/subscription-group.reducer';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faHashtag} from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import {toast} from 'react-toastify';

const QuickCheckout = props => {
  const { name, values, errors, idx, fields, setCheckoutFormState } = props;

  const options = {
    autoClose: 500,
    position: toast.POSITION.BOTTOM_CENTER
  };

  const [sellingPlans, setSellingPlans] = useState([]);
  const [formState, setFormState] = useState(null);

  useEffect(() => {
    if (formState && formState?.['lines']?.[idx]?.variantId && formState?.['lines']?.[idx]?.productId) {
      axios
        .get(
          `/api/subscription-groups/selling-plans-for-product-variant?variantId=${formState?.['lines']?.[idx]?.variantId}&productId=${formState?.['lines']?.[idx]?.productId}`
        )
        .then(res => {
          setSellingPlans(res.data);
          console.log(res?.data);
        });
    }
  }, [formState]);

  return (
    <Row style={{ marginBottom: '10px' }}>
      <Col sm={5}>
        <Field
          render={({ input, meta }) => (
            <Input
              {...input}
              className=""
              style={{ flexGrow: 1, width: '48%', marginRight: '4%' }}
              type="hidden"
              invalid={meta.error && meta.touched ? true : null}
            />
          )}
          type="hidden"
          id={`variantId`}
          className="mr-2"
          name={`${name}.variantId`}
        />
        <Field
          render={({ input, meta }) => (
            <Input
              {...input}
              className=""
              style={{ flexGrow: 1, width: '48%', marginRight: '4%' }}
              type="hidden"
              invalid={meta.error && meta.touched ? true : null}
            />
          )}
          type="hidden"
          id={`productId`}
          className="mr-2"
          name={`${name}.productId`}
        />
        <div style={{ display: 'flex' }}>
          <div style={{ width: '55px' }}>
            <img src={values?.['lines']?.[idx]?.imageSrc} style={{ width: '100%' }}></img>
          </div>
          <div style={{ marginLeft: '10px' }}>
            <div>{values?.['lines']?.[idx]?.title}</div>
            <div style={{ fontSize: '12px', opacity: '0.8' }}>Variant Id: {values?.['lines']?.[idx]?.variantId}</div>
          </div>
        </div>
      </Col>
      <FormSpy subscription={{ values: true }}>
        {({ values }) => {
          setFormState(values);
          setCheckoutFormState(values);
          return <></>;
        }}
      </FormSpy>
      <Col sm={6}>
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <FormGroup style={{ width: '49%' }}>
            <Label for={`${name}.quantity`}>Quantity</Label>
            <Field
              render={({ input, meta }) => (
                <InputGroup style={{ flexGrow: 1 }}>
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <FontAwesomeIcon icon={faHashtag} />
                    </InputGroupText>
                  </InputGroupAddon>
                  <>
                    <Input {...input} className="" type="number" invalid={meta.error && meta.touched ? true : null} />
                    {meta.error && (
                      <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                        {meta.error}
                      </div>
                    )}
                  </>
                </InputGroup>
              )}
              type="number"
              id={`quantity`}
              className="mr-2"
              name={`${name}.quantity`}
              validate={value => {
                if (!value || Number(value) < 1) {
                  return 'Please provide a valid value for Quantity';
                } else {
                  return undefined;
                }
              }}
            />
          </FormGroup>
          <FormGroup style={{ width: '49%' }}>
            <Label for={`${name}.sellingPlanId`}>Selling Plan Id</Label>
            <Field
              render={({ input, meta }) => (
                <>
                  <Input {...input} className="" type="select" invalid={meta.error && meta.touched ? true : null}>
                    <option value="">-- One Time Purchase --</option>
                    {sellingPlans.map(item => {
                      return <option value={item?.id.split('/').pop()}>{item?.frequencyName} ({item?.groupName})</option>;
                    })}
                  </Input>
                  {!input.value && values?.['lines']?.[idx]?.requiresSellingPlan ? (
                    <div style={{ fontSize: '9px', opacity: '0.8', color: 'red' }}>
                      Note: Selling plan selection is required for this product.
                    </div>
                  ) : (
                    ''
                  )}
                  {meta.error && (
                    <div class="invalid-feedback" style={{ display: meta.error && meta.touched ? 'block' : 'none' }}>
                      {meta.error}
                    </div>
                  )}
                </>
              )}
              type="select"
              id={`sellingPlanId`}
              className="mr-2"
              name={`${name}.sellingPlanId`}
            />
          </FormGroup>
        </div>
      </Col>
      <Col sm={1}>
        <FormGroup>
          <Label>&nbsp;</Label>
          <Button
            size="sm"
            className="btn-shadow-primary"
            color="danger"
            style={{ height: '38px', flexGrow: '1', display: 'block', width: '100%' }}
            onClick={() => {
              fields.remove(idx);
            }}
          >
            Delete
          </Button>
        </FormGroup>
      </Col>
    </Row>
  );
};

const mapStateToProps = storeState => ({
  subscriptionGroupEntity: storeState.subscriptionGroup.entity,
  usedProductIds: storeState.subscriptionGroup.usedProductIds,
  loading: storeState.subscriptionGroup.loading,
  updating: storeState.subscriptionGroup.updating,
  updateSuccess: storeState.subscriptionGroup.updateSuccess,
  shopInfo: storeState.shopInfo.entity
});

const mapDispatchToProps = {
  getEntity,
  getUsedProducts,
  updateEntity,
  createEntity,
  reset
};

export default connect(mapStateToProps, mapDispatchToProps)(QuickCheckout);
