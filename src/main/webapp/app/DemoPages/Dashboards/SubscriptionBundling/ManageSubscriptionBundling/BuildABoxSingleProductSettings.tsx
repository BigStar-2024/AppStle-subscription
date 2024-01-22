import React, { useState, useEffect, useMemo, useRef } from 'react';
import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Col,
  FormGroup,
  Input,
  Label,
  Row,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter
} from 'reactstrap';
import { faClone, faPlus, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ProductVariantModel from 'app/DemoPages/Dashboards/SubscriptionBundling/ProductVariantModel';
import { useSelector, useDispatch } from 'react-redux';
import { getSubscriptionPlanGroupV2 } from 'app/entities/subscription-group-plan/subscription-group-plan.reducer';
import { IRootState } from 'app/shared/reducers';
import Loader from 'react-loaders';
import { useFormState, Field, useForm } from 'react-final-form';
import { FieldArray } from 'react-final-form-arrays';
import { Product, ProductVariant, ProductStatus } from '@shopify/app-bridge/actions/ResourcePicker';
import { Warning } from '@mui/icons-material';
import ActiveProductAlert from '../../Subscriptions/ActiveProductAlert';

type SingleProductSetting = {
  sourceProduct: Product;
  minQuantity: number;
  maxQuantity: number;
  products: any[];
};

const defaultSingleProductSetting: SingleProductSetting = {
  sourceProduct: null,
  minQuantity: 0,
  maxQuantity: null,
  products: []
};

const BuildABoxSingleProductSettings = () => {
  const subscriptionGroupPlan = useSelector((state: IRootState) => state.subscriptionGroupPlan.entity);
  const isLoading = useSelector((state: IRootState) => state.subscriptionGroupPlan.loading);
  const dispatch: (action: any) => Promise<any> = useDispatch();

  const previousSubscriptionId = useRef(null);
  const [selectedNewSourceProduct, setSelectedNewSourceProduct] = useState();
  const [productsWithDrafts, setProductsWithDrafts] = useState<string[]>([]);
  const [isDraftModalOpen, setIsDraftModalOpen] = useState(false);

  const [cloneIndex, setCloneIndex] = useState(null);
  const [cloneFields, setCloneFields] = useState([]);
  const [isOpen, setIsOpen] = useState(false);
  const toggle = () => {
    if (isOpen) {
      setSelectedNewSourceProduct({ id: '' });
      setCloneIndex(null);
    }
    setIsOpen(!isOpen)
  };

  const {
    mutators: { setValue: setFormValue }
  } = useForm();
  const { values } = useFormState();

  useEffect(() => {
    if (values.subscriptionId) {
      dispatch(getSubscriptionPlanGroupV2(values.subscriptionId));
    } else {
      setFormValue('singleProductSettings', []);
    }

    if (previousSubscriptionId.current === null) {
      previousSubscriptionId.current = values.subscriptionId;
    } else if (previousSubscriptionId.current !== values.subscriptionId) {
      setFormValue('singleProductSettings', []);
      previousSubscriptionId.current = values.subscriptionId;
    }
  }, [values.subscriptionId]);

  const subscriptionGroupProducts = useMemo(() => (values.subscriptionId ? getSubscriptionGroupProducts() : []), [subscriptionGroupPlan]);

  const availableProducts = useMemo(() => {
    if (Array.isArray(subscriptionGroupProducts)) {
      return subscriptionGroupProducts.filter(
        item => !values.singleProductSettings?.some((setting: SingleProductSetting) => setting?.sourceProduct?.id == item?.id)
      );
    } else {
      return [];
    }
  }, [subscriptionGroupProducts, values.singleProductSettings]);

  function getSubscriptionGroupProducts() {
    const products = [];
    try {
      const parse = JSON.parse(subscriptionGroupPlan.productIds);
      if (Array.isArray(parse)) {
        parse.forEach(item => products.push(item));
      }
    } catch (e) {}

    try {
      const parse = JSON.parse(subscriptionGroupPlan.variantIds);
      if (Array.isArray(parse)) {
        parse.forEach(item => products.push(item));
      }
    } catch (e) {}

    return products;
  }

  function checkProductStatus(products: Product[], id: string) {
    const hasDraftProducts = products?.some(product => product.status !== ProductStatus.Active);
    if (hasDraftProducts) {
      if (!productsWithDrafts?.some(prodId => prodId == id)) setProductsWithDrafts([...productsWithDrafts, id]);
    } else if (productsWithDrafts?.some(prodId => prodId == id)) {
      setProductsWithDrafts(productsWithDrafts.filter(prodId => prodId != id));
    }
  }

  const NewSourceProductDropDown = () => (
    <div style={{ flex: 1 }}>
      <Label for="newSourceProduct">Choose Source Product</Label>
      {isLoading && (
        <div id="newSourceProduct" className="ml-2">
          <Loader type="line-scale" active />
        </div>
      )}
      {!isLoading && (
        <Input
          id="newSourceProduct"
          type="select"
          value={selectedNewSourceProduct?.id}
          disabled={isLoading}
          onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
            if (Array.isArray(subscriptionGroupProducts)) {
              setSelectedNewSourceProduct(subscriptionGroupProducts.find(item => item.id === parseInt(event.target.value)));
            }
          }}
        >
          <option value={''}>Select source product</option>
          {availableProducts.map((item, index) => (
            <option key={index} value={item?.id}>
              {item?.title}
            </option>
          ))}
        </Input>
      )}
    </div>
  );

  const cloneProductSettings = (index, fields) => {
    setCloneIndex(index);
    setCloneFields(fields);
    toggle();
  };

  const saveCloneProductSettings = () => {
    if (cloneIndex != null) {
      let data = values?.singleProductSettings[cloneIndex];
      if (data) {
        cloneFields.push({ ...data, sourceProduct: selectedNewSourceProduct });
        setSelectedNewSourceProduct({ id: '' });
        setCloneIndex(null);
      }
      toggle();
    }
  };

  return (
    <>
      <FieldArray
        name="singleProductSettings"
        validate={settings => (!settings?.length ? 'Please create at least one Single Product setting' : undefined)}
      >
        {({ fields, meta: arrayMeta }) => (
          <>
            <Card>
              <CardBody>
                {!values.subscriptionId && <div>Choose a subscription plan above to add settings</div>}

                {!!values.subscriptionId && (
                  <div className="d-flex justify-content-between align-items-end" style={{ gap: '1rem' }}>
                    {!availableProducts?.length && !isLoading && (
                      <div>There are no more available products for creating a new setting.</div>
                    )}

                    {(!!availableProducts?.length || isLoading) && (
                      <>
                        <NewSourceProductDropDown />
                        <Button
                          color="primary"
                          disabled={!selectedNewSourceProduct?.id}
                          onClick={() => {
                            fields.push({ ...defaultSingleProductSetting, sourceProduct: selectedNewSourceProduct });
                            setSelectedNewSourceProduct({ id: '' });
                          }}
                          className="px-4 py-2"
                        >
                          <FontAwesomeIcon icon={faPlus} className="mr-2" />
                          Add Settings
                        </Button>
                      </>
                    )}
                  </div>
                )}

                {!!arrayMeta.error && arrayMeta.touched && (
                  <div className="d-block invalid-feedback">{typeof arrayMeta.error === 'string' && arrayMeta.error}</div>
                )}
              </CardBody>
            </Card>

            {fields.map((name, index) => {
              const setting = fields.value?.[index];
              return (
                <Card className={`mt-3 ${!!arrayMeta.error?.[index] && arrayMeta.submitFailed ? 'border border-danger' : ''}`} key={index}>
                  <CardHeader className="d-flex justify-content-between">
                    <div className="text-primary" style={{ fontSize: '1.25rem', textTransform: 'none' }}>
                      {setting?.sourceProduct?.title || ''}
                    </div>
                    <div>
                      {availableProducts?.length > 0 && !isLoading && (
                        <Button color="primary mr-2" title={'Clone'} onClick={() => cloneProductSettings(index, fields)}>
                          <FontAwesomeIcon icon={faClone} />
                        </Button>
                      )}
                      <Button color="danger" title={'Delete'} onClick={() => fields.remove(index)}>
                        <FontAwesomeIcon icon={faTrash} />
                      </Button>
                    </div>
                  </CardHeader>
                  <CardBody>
                    <Row>
                      <Col md={6}>
                        <Field id={`${name}.minQuantity`} name={`${name}.minQuantity`} type="number">
                          {({ input }) => (
                            <FormGroup>
                              <Label for={`${name}.minQuantity`}>
                                <strong>Min Quantity</strong>
                              </Label>
                              <Input {...input} />
                            </FormGroup>
                          )}
                        </Field>
                        <Field id={`${name}.maxQuanity`} name={`${name}.maxQuantity`} type="number">
                          {({ input }) => (
                            <FormGroup>
                              <Label for={`${name}.maxQuantity`}>
                                <strong>Max Quantity</strong>
                              </Label>
                              <Input {...input} />
                            </FormGroup>
                          )}
                        </Field>
                      </Col>
                      <Col md={6}>
                        <FormGroup>
                          <Label className="d-flex flex-column">
                            Selected Products
                            {productsWithDrafts?.some(prodId => prodId == setting.sourceProduct?.id) && (
                              <span className="d-flex align-items-center" style={{ color: '#806013', fontSize: '12px', gap: '.15rem' }}>
                                <Warning style={{ fontSize: '1rem', color: '#ffc107' }} /> Draft Product Alert!{' '}
                                <Button
                                  color="link"
                                  className="px-0"
                                  style={{ fontSize: '12px' }}
                                  onClick={(e: React.MouseEvent) => {
                                    e.preventDefault();
                                    setIsDraftModalOpen(true);
                                  }}
                                >
                                  More info
                                </Button>
                              </span>
                            )}
                          </Label>
                          <Field
                            name={`${name}.products`}
                            validate={value => (!value?.length ? 'Please add products to this setting' : undefined)}
                          >
                            {({ input }) => (
                              <ProductVariantModel
                                selectedProductIds={input.value?.filter?.((item: any) => item.type === 'PRODUCT') || []}
                                selectedProductVarIds={input.value?.filter?.((item: any) => item.type === 'VARIANT') || []}
                                buttonLabel="Select Products"
                                header="Product"
                                bundleLevel={values.bundleLevel}
                                checkProductStatus={(products: any[]) => checkProductStatus(products, setting?.sourceProduct?.id)}
                                onChange={(products: Product[], variants: ProductVariant[]) => {
                                  input.onChange([...products, ...variants]);
                                }}
                              />
                            )}
                          </Field>
                        </FormGroup>
                      </Col>
                    </Row>
                  </CardBody>
                </Card>
              );
            })}
            <Modal isOpen={isDraftModalOpen} toggle={() => setIsDraftModalOpen(!isDraftModalOpen)}>
              <ModalHeader toggle={() => setIsDraftModalOpen(!isDraftModalOpen)}>Draft Product Alert</ModalHeader>
              <ModalBody>
                <ActiveProductAlert />
              </ModalBody>
            </Modal>
            <hr className="mb-0" />
          </>
        )}
      </FieldArray>
      <Modal isOpen={isOpen} toggle={toggle} size="lg">
        <ModalHeader toggle={toggle}>Clone Product Settings</ModalHeader>
        <ModalBody className="overflow-auto">
          {' '}
          <NewSourceProductDropDown />{' '}
        </ModalBody>
        <ModalFooter className="d-flex">
          <Button
            color="primary"
            disabled={!selectedNewSourceProduct?.id}
            onClick={() => {
              saveCloneProductSettings();
            }}
            className="px-4 py-2"
          >
            <FontAwesomeIcon icon={faClone} className="mr-2" />
            Clone Settings
          </Button>
        </ModalFooter>
      </Modal>
    </>
  );
};

export default BuildABoxSingleProductSettings;
