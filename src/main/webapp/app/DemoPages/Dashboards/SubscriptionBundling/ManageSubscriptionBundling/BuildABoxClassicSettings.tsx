import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { Field, useFormState, useForm, FieldInputProps } from 'react-final-form';
import Select from 'react-select';
import { Button, Card, CardBody, FormGroup, FormText, Input, InputGroup, InputGroupAddon, Label, Row, Col, Table } from 'reactstrap';
import { faPlus, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ProductViewSettings } from 'app/shared/model/enumerations/product-view-settings.model';
import { BuildABoxRedirect } from 'app/shared/model/enumerations/build-a-box-redirect.model';
import { IRootState } from 'app/shared/reducers';
import { BuildBoxVersion } from 'app/shared/model/enumerations/build-box-version.model';
import { FieldArray } from 'react-final-form-arrays';
import HelpTooltip from 'app/DemoPages/Dashboards/Shared/HelpTooltip';
import { ISubscriptionBundling } from 'app/shared/model/subscription-bundling.model';

type TieredDiscount = {
  discountBasedOn: 'QUANTITY' | 'AMOUNT';
  quantity: number;
  discount: number;
};

const defaultTieredDiscount: TieredDiscount = {
  discountBasedOn: 'QUANTITY',
  quantity: null,
  discount: null,
};

const MainSettings = () => {
  const { values } = useFormState();

  const shopInfo = useSelector((state: IRootState) => state.shopInfo.entity);
  const otherEnabledBundleEntities = useSelector((state: IRootState) =>
    state.subscriptionBundling.entities.filter(
      bundle => bundle.subscriptionBundlingEnabled && bundle?.id != state.subscriptionBundling.entity?.id
    )
  );

  const enabledBundleOptions = otherEnabledBundleEntities.map(getBundleOption);

  function getBundleOption(bundle: ISubscriptionBundling) {
    return {
      value: bundle?.id,
      label: `${bundle?.name ?? 'Unnamed box'} - ${bundle?.groupName}`,
    };
  }

  function getBundleOptionFromUrl(bundles: ISubscriptionBundling[], url: string) {
    const bundle = bundles.find((bundle: ISubscriptionBundling) => bundle.subscriptionBundleLink == url);
    if (!bundle) return undefined;
    return getBundleOption(bundle);
  }

  function handleSelectOtherBuildABox(selectedOption: { value: number }, input: FieldInputProps<string>, bundles: ISubscriptionBundling[]) {
    const selectedId = selectedOption.value;
    const customRedirectUrl = bundles.find(bundle => bundle.id == selectedId).subscriptionBundleLink;
    input.onChange(customRedirectUrl);
  }

  return (
    <Row>
      <Col xs={12} sm={6}>
        <FormGroup>
          <Label for="minProductCount">
            <strong>Minimum Product Count</strong>
          </Label>
          <Field
            name="minProductCount"
            id="minProductCount"
            type="number"
            validate={value => (value < 0 ? 'Please provide a valid value for Min Amount.' : undefined)}
          >
            {({ input, meta }) => (
              <Input
                {...input}
                invalid={meta.error && meta.touched}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => input.onChange(event.target.value.replace(/\./g, ''))}
              />
            )}
          </Field>
          {shopInfo?.buildBoxVersion === BuildBoxVersion.V1 && (
            <FormText>Please note, discount will not be applied if the minimum product count is set to zero.</FormText>
          )}
        </FormGroup>
      </Col>
      <Col xs={12} sm={6}>
        <FormGroup>
          <Label for="maxProductCount" className="d-flex align-items-center" style={{ gap: '.25rem' }}>
            <strong>Maximum Product Count</strong>
            <HelpTooltip>Customer cannot order more than maximum product count limit.</HelpTooltip>
          </Label>
          <Field
            name="maxProductCount"
            id="maxProductCount"
            type="number"
            validate={value => {
              if (value < 0) {
                return 'Please provide a valid value for maximum product count.';
              } else if (values?.['minProductCount'] && parseInt(value) < parseInt(values?.['minProductCount'])) {
                return 'Max product count must be equal or greater than the min product count.';
              } else {
                return undefined;
              }
            }}
          >
            {({ input, meta }) => (
              <Input
                {...input}
                invalid={meta.error && meta.touched ? true : null}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) => input.onChange(event.target.value.replace(/\./g, ''))}
              />
            )}
          </Field>
        </FormGroup>
      </Col>

      <Col xs={12} sm={6}>
        <FormGroup>
          <Label for="minOrderAmount">
            <strong>Minimum Order Amount</strong>
          </Label>

          <InputGroup>
            <Field
              name="minOrderAmount"
              id="minOrderAmount"
              type="number"
              validate={value => (value < 0 ? 'Please provide a valid value for Min Amount.' : undefined)}
            >
              {({ input, meta }) => (
                <Input
                  invalid={meta.error && meta.touched ? true : null}
                  {...input}
                  onChange={(event: React.ChangeEvent<HTMLInputElement>) => input.onChange(event.target.value.replace(/\./g, ''))}
                />
              )}
            </Field>
            <InputGroupAddon addonType='append'>{shopInfo?.currency}</InputGroupAddon>
          </InputGroup>
          {shopInfo?.buildBoxVersion === BuildBoxVersion.V1 && (
            <FormText>To get the discount, the order must meet the minimum product and minimum order price.</FormText>
          )}
        </FormGroup>
      </Col>
      {shopInfo?.buildBoxVersion === BuildBoxVersion.V1 && (
        <Col xs={12} sm={6}>
          <FormGroup>
            <Label for="discount">
              <strong>Discount Percentage</strong>
            </Label>
            <Field
              name="discount"
              id="discount"
              type="number"
              validate={value => ((!value && value !== 0) || value < 0 ? 'Please provide a valid value for discount.' : undefined)}
            >
              {({ input, meta }) => <Input {...input} invalid={meta.error && meta.touched} />}
            </Field>
          </FormGroup>
        </Col>
      )}
      <Col xs={12} md={6}>
        <FormGroup>
          <Label for="bundleRedirect" className="d-flex align-items-center" style={{ gap: '.25rem' }}>
            Build-A-Box Redirect
            <HelpTooltip>Build-A-Box can be redirected to cart, checkout, another Build-A-Box, or custom page.</HelpTooltip>
          </Label>
          <Field name="bundleRedirect" type="select" validate={value => (!value ? 'Please Select bundle redirect.' : undefined)}>
            {({ input, meta }) => (
              <Input {...input} invalid={meta.error && meta.touched}>
                <option value={''} disabled>
                  Select Bundle Redirect
                </option>
                <option value={BuildABoxRedirect.CART}>Cart</option>
                <option value={BuildABoxRedirect.CHECKOUT}>Checkout</option>
                <option value="ANOTHER_BOX">Another Build-A-Box</option>
                <option value={BuildABoxRedirect.CUSTOM}>Custom</option>
              </Input>
            )}
          </Field>
          <FormText></FormText>
        </FormGroup>
      </Col>
      {values?.bundleRedirect === BuildABoxRedirect.CUSTOM && (
        <Col xs={12} md={6}>
          <FormGroup>
            <Label for="customRedirectURL">Custom Redirection Url</Label>
            <Field name="customRedirectURL" id="customRedirectURL" type="text">
              {({ input }) => <Input {...input} placeholder="eg. https://google.com" />}
            </Field>
          </FormGroup>
        </Col>
      )}
      {values?.bundleRedirect === 'ANOTHER_BOX' && (
        <Col xs={12} md={6}>
          <FormGroup>
            <Label for="customRedirectURL">Select Build-A-Box to Redirect To</Label>
            <Field name="customRedirectURL" id="customRedirectURL">
              {({ input }) => (
                <Select
                  value={getBundleOptionFromUrl(otherEnabledBundleEntities, input.value)}
                  options={enabledBundleOptions}
                  onChange={(selectedOption: { value: number; label: string }) =>
                    handleSelectOtherBuildABox(selectedOption, input, otherEnabledBundleEntities)
                  }
                  placeholder="Select Build-A-Box"
                />
              )}
            </Field>
          </FormGroup>
        </Col>
      )}
      {shopInfo?.shop === 'cali-grill-meal-prep.myshopify.com' && (
        <Col xs={12} sm={6}>
          <FormGroup>
            <Label for="productViewStyle">
              <strong>Settings</strong>
            </Label>
            <Field
              name="productViewStyle"
              id="productViewStyle"
              validate={value => {
                return (!value && value !== 0) || value < 0 ? 'Please provide a valid value for Product View Style.' : undefined;
              }}
            >
              {({ input, meta }) => (
                <Input {...input} type="select" invalid={meta.error && meta.touched}>
                  <option value={''} disabled>
                    Select view style
                  </option>
                  <option value={ProductViewSettings.QUICK_ADD}>Quick Add</option>
                  <option value={ProductViewSettings.VIEW_DETAILS}>View Details</option>
                </Input>
              )}
            </Field>
          </FormGroup>
        </Col>
      )}
    </Row>
  );
};

const TieredDiscountSettings = () => {
  const [newDiscount, setNewDiscount] = useState(defaultTieredDiscount);

  const {
    mutators: { sortDiscounts },
  } = useForm();

  const { values } = useFormState();

  const isDiscountValid = newDiscount.discount && newDiscount.discount > 0 && newDiscount.discount <= 100;
  const isQuantityValid = getIsQuantityValid();

  function getIsQuantityValid() {
    if (!newDiscount.quantity) return false;

    if (newDiscount.discountBasedOn === 'QUANTITY') {
      if (values.minProductCount > 0) {
        return newDiscount.quantity >= values.minProductCount;
      }
    } else if (newDiscount.discountBasedOn === 'AMOUNT') {
      if (values.minProductCount > 0) {
        return newDiscount.quantity >= values.minOrderAmount;
      }
    }
    return newDiscount.quantity >= 0;
  }

  return (
    <FieldArray name="tieredDiscount">
      {({ fields }) => (
        <>
          <h5 className="my-3">Tiered Discounts</h5>
          <Row>
            <Col md={5}>
              <InputGroup>
                <Input
                  type="number"
                  value={newDiscount.quantity ?? ''}
                  onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
                    const newQuantity = parseInt(event.target.value.replace(/\./g, ''));
                    setNewDiscount({
                      ...newDiscount,
                      quantity: !isNaN(newQuantity) ? newQuantity : undefined,
                    });
                  }}
                  placeholder={newDiscount.discountBasedOn === 'QUANTITY' ? 'Min Quantity' : 'Min Amount'}
                />
                <InputGroupAddon addonType='append'>
                  <Input
                    type="select"
                    value={newDiscount.discountBasedOn}
                    onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
                      setNewDiscount({
                        ...newDiscount,
                        discountBasedOn: event.target.value as 'QUANTITY' | 'AMOUNT',
                      });
                    }}
                  >
                    <option value="QUANTITY">Quantity</option>
                    <option value="AMOUNT">Amount</option>
                  </Input>
                </InputGroupAddon>
              </InputGroup>

              <FormText className={'text-danger'}>
                {newDiscount.discountBasedOn === 'QUANTITY' &&
                  (!!values.minProductCount && !!newDiscount.quantity && newDiscount.quantity < values.minProductCount
                    ? `Quantity should greater then or equal ${values.minProductCount}`
                    : !!values.maxProductCount && !!newDiscount.quantity && newDiscount.quantity > values.maxProductCount
                    ? `Quantity should less then or equal ${values.maxProductCount}`
                    : newDiscount.quantity && !isQuantityValid
                    ? 'Please enter a valid quantity'
                    : '')}
                {newDiscount.discountBasedOn === 'AMOUNT' &&
                  (newDiscount.quantity > 0 && newDiscount.quantity < values.minOrderAmount
                    ? `Spend amount should be greater then or equal ${values.minOrderAmount}`
                    : newDiscount.quantity && !isQuantityValid
                    ? 'Please enter a valid quantity'
                    : '')}
              </FormText>
            </Col>
            <Col md={5}>
              <InputGroup>
                <Input
                  type="number"
                  value={newDiscount.discount ?? ''}
                  onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
                    const discount = parseFloat(event.target.value);
                    setNewDiscount({
                      ...newDiscount,
                      discount: !isNaN(discount) ? discount : undefined,
                    });
                  }}
                  placeholder={'Discount(%)'}
                />
                <InputGroupAddon addonType='append'>%</InputGroupAddon>
              </InputGroup>
              {newDiscount.discount && !isDiscountValid && <div className="invalid-feedback d-block">Please enter a valid discount</div>}
            </Col>
            <Col md={2}>
              <Button
                color={'primary'}
                size="lg"
                icon="plus"
                disabled={!isDiscountValid || !isQuantityValid}
                onClick={() => {
                  fields.push(newDiscount);
                  sortDiscounts();
                  setNewDiscount(defaultTieredDiscount);
                }}
              >
                <FontAwesomeIcon icon={faPlus} />
                {'  '}Add
              </Button>
            </Col>
          </Row>
          <Table className="mb-2 mt-4">
            <thead>
              <tr>
                <th />
                <th>When the customer...</th>
                <th>They get a...</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fields.value?.map((item, index) => {
                return (
                  <tr key={index}>
                    <td>{index + 1}</td>
                    <td>{item?.discountBasedOn === 'QUANTITY' ? `Buys ${item.quantity} products` : `Spends ${item.quantity}`}</td>
                    <td>{item.discount || '-'}% Discount</td>
                    <td style={{ textAlign: 'center' }}>
                      <Button
                        color={'danger'}
                        icon="plus"
                        onClick={() => {
                          fields.remove(index);
                        }}
                      >
                        <FontAwesomeIcon icon={faTrash} />
                      </Button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </Table>
        </>
      )}
    </FieldArray>
  );
};

const BuildABoxClassicSettings = () => {
  return (
    <Card>
      <CardBody>
        <MainSettings />
        <TieredDiscountSettings />
      </CardBody>
    </Card>
  );
};

export default BuildABoxClassicSettings;
