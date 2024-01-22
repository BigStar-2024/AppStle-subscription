import React, {Fragment, useEffect, useState} from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  Card,
  CardBody,
  CardHeader,
  Col,
  FormGroup,
  FormText,
  Input,
  InputGroup,
  Label,
  Row
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import {convertDateTimeFromServer, convertDateTimeToServer} from 'app/shared/util/date-utils';
import {createEntity, getEntity, reset, updateEntity} from 'app/entities/bundle-rule/bundle-rule.reducer';
import {useHistory} from 'react-router';
import {BundleDiscountType} from 'app/shared/model/enumerations/bundle-discount-type.model';
import {BundleStatus} from 'app/shared/model/enumerations/bundle-status.model';
// import ProductVariantModel from 'app/DemoPages/Dashboards/Bundling/Bundles/ProductVariantModel';
import { Tooltip as ReactTooltip } from 'react-tooltip';
import ActiveProductAlert from '../../Subscriptions/ActiveProductAlert';
import ProductVariantModel from './ProductVariantModel';

const subscriptionBundleSetting = props => {
  const history = useHistory();
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const editId = props?.match?.params?.id;
  const [startDate, setStartDate] = useState();
  const [endDate, setEndDate] = useState();
  const [selectedProducts, setSelectedProducts] = useState([]);
  const [selectedVariants, setSelectedVariants] = useState([]);
  const [enableDateTime, setEnableDateTime] = useState(false);
  const [tags, setTags] = useState([]);
  const [modal, setModal] = useState(false);
  const [checkProductStatusDraft ,setCheckProductStatusDraft]= useState(false);
  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  const checkProductStatus = (products) => {
    if(products?.filter((product) => product.status !== "ACTIVE").length > 0) {
       setCheckProductStatusDraft(true);
    } else {
      setCheckProductStatusDraft(false)
    }
  }

  const saveEntity = values => {
    if (enableDateTime && startDate != null) {
      values.startDate = convertDateTimeToServer(startDate);
    } else {
      values.startDate = null;
    }
    if (enableDateTime && endDate != null) {
      values.endDate = convertDateTimeToServer(endDate);
    } else {
      values.endDate = null;
    }
    const entity = {
      ...props.bundleRuleEntity,
      ...values,
      id: isNew ? null : props.bundleRuleEntity.id
    };

    let products = [];

    if (selectedVariants.length > 0) {
      products = [...products, ...selectedVariants];
    }
    if (selectedProducts.length > 0) {
      products = [...products, ...selectedProducts];
    }

    if (values.bundleLevel === 'VARIANT') {
      entity.variants = JSON.stringify(products);
      entity.products = null;
    } else if (values.bundleLevel === 'PRODUCT') {
      entity.products = JSON.stringify(products);
      entity.variants = null;
    }

    if (isNew) {
      props.createEntity(entity);
    } else {
      props.updateEntity(entity);
    }
  };

  useEffect(() => {
    if (props.bundleRuleEntity.id !== undefined && props.bundleRuleEntity.id !== null) {
      if (props.bundleRuleEntity.startDate && props.bundleRuleEntity.endDate) {
        setEnableDateTime(true);
        setStartDate(new Date(convertDateTimeFromServer(props.bundleRuleEntity.startDate)));
        setEndDate(new Date(convertDateTimeFromServer(props.bundleRuleEntity.endDate)));
      }

      if (props.bundleRuleEntity.id && props.bundleRuleEntity.variants != null) {
        const items = JSON.parse(props.bundleRuleEntity.variants);
        setSelectedVariants(items.filter(item => item.type === 'VARIANT'));
        setSelectedProducts(items.filter(item => item.type === 'PRODUCT'));
      }
      if (props.bundleRuleEntity.id && props.bundleRuleEntity.products != null) {
        const items = JSON.parse(props.bundleRuleEntity.products);
        setSelectedProducts(items.filter(item => item.type === 'PRODUCT'));
        setSelectedVariants(items.filter(item => item.type === 'VARIANT'));
      }
    }
  }, [props.bundleRuleEntity]);

  useEffect(() => {
    if (props.updateSuccess) {
      history.push('/dashboards/bundles');
    }
  }, [props.updateSuccess]);
  const identity = value => value;

  let submit;

  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);

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
        <PageTitle
          heading="Add or edit bundle"
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle={isNew ? 'Save' : 'Update'}
          enablePageTitleAction
          onActionClick={() => {
            submit();
          }}
          formErrors={formErrors}
          errorsVisibilityToggle={errorsVisibilityToggle}
          onActionUpdating={props.updating}
          updatingText={isNew ? 'Saving' : 'Updating'}
          sticky={true}
          tutorialButton={{
            show: true,
            videos: [
              {
                title: "Bundling",
                url: "https://www.youtube.com/watch?v=63BM5YIJh70",
              },
            ],
            docs: [
              {
                title: "How to Setup and Configure Bundling with Appstle Subscriptions",
                url: "https://intercom.help/appstle/en/articles/6756487-how-to-setup-and-configure-bundling-with-appstle-subscriptions"
              }
            ]
          }}
        />

        <Form
          initialValues={props.bundleRuleEntity}
          onSubmit={saveEntity}
          render={({handleSubmit, form, submitting, pristine, values, errors}) => {
            submit =
              Object.keys(errors).length === 0 && errors.constructor === Object
                ? handleSubmit
                : () => {
                  if (Object.keys(errors).length) handleSubmit();
                  setFormErrors(errors);
                  setErrorsVisibilityToggle(!errorsVisibilityToggle);
                };
            return (
              <Row className={'d-flex justify-content-center align-items-center'}>
                <Col md={8}>
                  {/* <Alert className="mb-2" color="warning">
                    <h6>Need assistance?</h6>
                    <p>Watch our tutorial videos or read our help documentation to get a better understanding.</p>
                    <span>
                      <Button color="warning" onClick={toggleShowModal}>Video Tutorials</Button>
                      <a
                        href='https://intercom.help/appstle/en/articles/6756487-how-to-setup-and-configure-bundling-with-appstle-subscriptions'
                        target='_blank'
                        rel="noopener noreferrer"
                        style={{marginLeft: '10px'}}
                      >
                        Help Documentation
                      </a>
                    </span>
                </Alert>
                  <Modal isOpen={showModal} toggle={toggleShowModal}>
                    <ModalHeader toggle={toggleShowModal}>Tutorial Videos</ModalHeader>
                    <ModalBody>
                      <div className="mt-4 border-bottom pb-4">
                        <h6>Bundling</h6>
                        <YoutubeVideoPlayer
                          url="https://www.youtube.com/watch?v=63BM5YIJh70"
                          iframeHeight="100%"
                          divClassName="video-container"
                          iframeClassName="responsive-iframe"
                        />
                      </div>
                    </ModalBody>
                    <ModalFooter>
                      <Button color="link" onClick={toggleShowModal}>Cancel</Button>
                    </ModalFooter>
                  </Modal> */}
                  <Card className="main-card mb-3 mt-3">
                    <CardBody>
                      <Row>
                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup>
                            <Label for="bundleName">Bundle name</Label>
                            <Field
                              render={({input, meta}) => (
                                <>
                                  <Input {...input} placeholder="Enter Bundle Name"
                                         invalid={meta.error && meta.touched ? true : null}/>

                                  <FormText>
                                    <span>Bundle name will be visible on invoices.</span>
                                  </FormText>
                                </>
                              )}
                              validate={value => {
                                return !value ? 'Please provide bundle name.' : undefined;
                              }}
                              id="bundleName"
                              className="form-control"
                              type="text"
                              name="name"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>
                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup>
                            <Label for="title">Title</Label>
                            <Field
                              render={({input, meta}) => (
                                <>
                                  <Input {...input} placeholder="Enter Title"
                                         invalid={meta.error && meta.touched ? true : null}/>
                                  <FormText>
                                    <span>Title will be displayed on product pages.</span>
                                  </FormText>
                                </>
                              )}
                              validate={value => {
                                return !value ? 'Please provide title.' : undefined;
                              }}
                              id="title"
                              className="form-control"
                              type="text"
                              name="title"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>
                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup>
                            <Label for="description">Description</Label>
                            <Field
                              render={({input, meta}) => (
                                <>
                                  <Input {...input} placeholder="Enter Description"
                                         invalid={meta.error && meta.touched ? true : null}/>
                                  <FormText>
                                    <span>Description will be displayed on product pages under bundle title.</span>
                                  </FormText>
                                </>
                              )}
                              id="description"
                              className="form-control"
                              type="text"
                              name="description"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>
                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup>
                            <Label for="actionButtonText">Action Button Text</Label>
                            <Field
                              render={({input, meta}) => (
                                <>
                                  <Input {...input} placeholder="Enter Call to action"
                                         invalid={meta.error && meta.touched ? true : null}/>
                                  <FormText>
                                    <span>Text displayed in add to cart button in the bundle.</span>
                                  </FormText>
                                </>
                              )}
                              id="actionButtonText"
                              className="form-control"
                              type="text"
                              name="actionButtonText"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>

                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup>
                            <Label for="actionButtonDescription">Text under button</Label>
                            <Field
                              render={({input, meta}) => (
                                <>
                                  <Input {...input} placeholder="Text under button"
                                         invalid={meta.error && meta.touched ? true : null}/>
                                  <FormText>
                                    <span>Displayed under the add to cart button.</span>
                                  </FormText>
                                </>
                              )}
                              id="actionButtonDescription"
                              className="form-control"
                              type="text"
                              name="actionButtonDescription"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>

                        {/* <Col xs={6} sm={6} md={6} lg={6}>
                          <FormGroup>
                            <Label for="startDate">Start Date</Label>
                            <FormGroup>
                              <DatePicker
                                id="startDate"
                                type="datetime-local"
                                className="form-control"
                                name="startDate"
                                placeholder={'YYYY-MM-DD HH:mm'}
                                selected={startDate}
                                onChange={date => setStartDate(date)}
                                showTimeSelect
                                timeFormat="HH:mm"
                                timeIntervals={15}
                                timeCaption="time"
                                dateFormat="MMMM d, yyyy h:mm aa"
                                autoComplete="off"
                              />
                            </FormGroup>
                          </FormGroup>
                        </Col>
                        <Col xs={6} sm={6} md={6} lg={6}>
                          <FormGroup>
                            <Label for="endDate">End time</Label>
                            <FormGroup>
                              <DatePicker
                                id="endDate"
                                type="datetime-local"
                                className="form-control"
                                name="endTime"
                                placeholder={'YYYY-MM-DD HH:mm'}
                                selected={endDate}
                                onChange={date => setEndDate(date)}
                                showTimeSelect
                                timeFormat="HH:mm"
                                timeIntervals={15}
                                timeCaption="time"
                                dateFormat="MMMM d, yyyy h:mm aa"
                                autoComplete="off"
                              />
                            </FormGroup>
                          </FormGroup>
                        </Col> */}
                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup>
                            <Label for="status">Status</Label>
                            <Field
                              render={({input, meta}) => (
                                <>
                                  <Input {...input} type={'select'} invalid={meta.error && meta.touched ? true : null}>
                                    <option value={BundleStatus.ACTIVE}>Active</option>
                                    <option value={BundleStatus.PAUSED}>Paused</option>
                                  </Input>
                                </>
                              )}
                              id="status"
                              className="form-control"
                              type="text"
                              name="status"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>
                      </Row>
                    </CardBody>
                  </Card>

                  <Card className="main-card mb-3 mt-3">
                    <CardHeader>Bundle Type</CardHeader>
                    <CardBody>
                      <Row>
                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup check className="mb-2 mt-2">
                            <Field
                              render={({input, meta}) => (
                                <Label check>
                                  <Input
                                    {...input}
                                    checked={input.checked}
                                    onChange={(value) => input.onChange(value)}
                                    name="selectSubscriptionByDefault"
                                  />{' '}
                                  Select Subscription By Default
                                  {/*<FormText>*/}
                                  {/*  <span>Selected variants will be displayed as a standalone products in bundle.</span>*/}
                                  {/*</FormText>*/}
                                </Label>
                              )}
                              type="checkbox"
                              className="form-control"
                              name="selectSubscriptionByDefault"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>

                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup check className="mb-2">
                            <Field
                              render={({input, meta}) => (
                                <Label check>
                                  <Input
                                    {...input}
                                    checked={input.checked}
                                    onChange={(value) => input.onChange(value)}
                                    name="showCombinedSellingPlan"
                                  />{' '}
                                  Show Combined Selling Plan
                                </Label>
                              )}
                              type="checkbox"
                              className="form-control"
                              name="showCombinedSellingPlan"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>

                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup className="mb-2">
                            <Label for="bundleType">
                              Bundle Type
                            </Label>
                            <Field
                              render={({input, meta}) => (
                                <>
                                  <Input
                                    {...input}
                                    type={'select'}
                                    invalid={meta.error && meta.touched ? true : null}
                                  >
                                    <option value="CLASSIC">CLASSIC</option>
                                    <option value="MIX_AND_MATCH">Mix and Match</option>
                                  </Input>
                                </>
                              )}
                              id="bundleType"
                              className="form-control"
                              name="bundleType"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>

                        {
                          values.bundleType === "MIX_AND_MATCH" ? <>
                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label for="minimumNumberOfItems">Minimum Number Of Items</Label>
                                <Field
                                  render={({input, meta}) => (
                                    <>
                                      <Input {...input} placeholder={"Enter Minimum Number Of Items"} invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  )}
                                  id="minimumNumberOfItems"
                                  className="form-control"
                                  type="number"
                                  name="minimumNumberOfItems"
                                  parse={identity}
                                />
                              </FormGroup>
                            </Col>

                            <Col xs={12} sm={12} md={6} lg={6}>
                              <FormGroup>
                                <Label for="maximumNumberOfItems">Maximum Number Of Items</Label>
                                <Field
                                  render={({input, meta}) => (
                                    <>
                                      <Input {...input} placeholder={"Enter Maximum Number Of Items"} invalid={meta.error && meta.touched ? true : null}/>
                                    </>
                                  )}
                                  id="maximumNumberOfItems"
                                  className="form-control"
                                  type="number"
                                  name="maximumNumberOfItems"
                                  parse={identity}
                                />
                              </FormGroup>
                            </Col>
                          </> : null
                        }

                      </Row>
                    </CardBody>
                  </Card>

                  <Card className="main-card mb-3 mt-3">
                    <CardHeader>Discounts</CardHeader>
                    <CardBody>
                      <Row>
                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup>
                            <Label for="discountValue">
                              {values.discountType === BundleDiscountType.PERCENTAGE
                                ? 'Discount Percentage'
                                : values.discountType === BundleDiscountType.FIXED_AMOUNT
                                  ? 'Discount Amount'
                                  : values.discountType === BundleDiscountType.FIXED_BUNDLE_AMOUNT
                                    ? 'Discount Amount'
                                    : ''}
                            </Label>

                            <InputGroup>
                              <Field
                                render={({input, meta}) => (
                                  <div>
                                    <Input
                                      {...input}
                                      placeholder="Enter Discount Percentage"
                                      invalid={meta.error && meta.touched ? true : null}
                                    />

                                    <FormText>
                                      {values.discountType === BundleDiscountType.PERCENTAGE ? (
                                        <span>
                                          Set the percentage discount which will be applied to each product in the bundle. You can set it to
                                          0% if you want to create a bundle without a discount.
                                        </span>
                                      ) : values.discountType === BundleDiscountType.FIXED_AMOUNT ? (
                                        <span>
                                          Set the total discount amount which will be proportionally divided between bundled products. The
                                          total applied discount amount might differ for a few cents because each product must have a
                                          rounded price. If you are using multiple currencies in your shop, then the discount amount will be
                                          converted and rounded based on the default rounding rules.
                                        </span>
                                      ) : values.discountType === BundleDiscountType.FIXED_BUNDLE_AMOUNT ? (
                                        <span>
                                          Set the final price for the bundle. The discount required to reduce the price to this amount will
                                          be calculated and proportionally divided between products in the bundle. If the original value of
                                          the bundle is less then the desired price, then the bundle will keep the original price. The total
                                          applied discount amount might differ for a few cents because each product must have a rounded
                                          price.
                                        </span>
                                      ) : (
                                        ''
                                      )}
                                    </FormText>
                                  </div>
                                )}
                                id="discountValue"
                                className="form-control"
                                type="text"
                                name="discountValue"
                                parse={identity}
                              />

                              <Field
                                render={({input, meta}) => (
                                  <>
                                    <Input
                                      {...input}
                                      type={'select'}
                                      className={'discount-type-width'}
                                      invalid={meta.error && meta.touched ? true : null}
                                    >
                                      <option value={BundleDiscountType.PERCENTAGE}>Percentage</option>
                                      <option value={BundleDiscountType.FIXED_AMOUNT}>Fixed Amount</option>
                                      {/* <option value={BundleDiscountType.FIXED_BUNDLE_AMOUNT}>Fixed Bundle Amount</option> */}
                                    </Input>
                                  </>
                                )}
                                id="discountType"
                                className="form-control"
                                type="text"
                                name="discountType"
                                parse={identity}
                              />
                            </InputGroup>
                          </FormGroup>
                        </Col>

                        {/* <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup>
                            <Label for="exampleSelect">Customer Include Tags</Label>
                            <Field
                              render={({ input, meta }) => (
                                <TagsInput
                                  value={
                                    values.customerIncludeTags != null && values.customerIncludeTags.length > 0
                                      ? values.customerIncludeTags.split(',')
                                      : []
                                  }
                                  addOnBlur={true}
                                  addOnPaste={true}
                                  onChange={values => {
                                    input.onChange(values.toString());
                                  }}
                                  inputProps={{
                                    placeholder: 'Type Customer Include Tags',
                                    className: 'react-tagsinput-input'
                                  }}
                                  onlyUnique={true}
                                />
                              )}
                              id="customerIncludeTags"
                              className="form-control"
                              type="text"
                              name="customerIncludeTags"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col> */}
                      </Row>
                    </CardBody>
                  </Card>

                  <Card className="main-card mb-3 mt-3">
                    <CardHeader>Bundle product level</CardHeader>
                    <CardBody>
                      <Row>
                        <Col xs={12} sm={12} md={12} lg={12}>
                          <FormGroup check>
                            <Field
                              render={({input, meta}) => (
                                <Label check>
                                  <Input
                                    value="PRODUCT"
                                    checked={input.value === 'PRODUCT'}
                                    onClick={event => {
                                      input.onChange(event.target.value);
                                      if (event.target.value === 'PRODUCT') {
                                        setSelectedVariants([]);
                                        setSelectedProducts([]);
                                      }
                                    }}
                                    type="radio"
                                    name="radio1"
                                  />{' '}
                                  Product
                                  <FormText>
                                    <span>Selected variants will be displayed in dropdown under products in bundle.</span>
                                  </FormText>
                                </Label>
                              )}
                              className="form-control"
                              name="bundleLevel"
                              parse={identity}
                            />
                          </FormGroup>
                          <FormGroup check>
                            <Field
                              render={({input, meta}) => (
                                <Label check>
                                  <Input
                                    value="VARIANT"
                                    checked={input.value === 'VARIANT'}
                                    onClick={event => {
                                      input.onChange(event.target.value);
                                      if (event.target.value === 'VARIANT') {
                                        setSelectedProducts([]);
                                        setSelectedVariants([]);
                                      }
                                    }}
                                    type="radio"
                                    name="radio1"
                                  />{' '}
                                  Variant
                                  <FormText>
                                    <span>Selected variants will be displayed as a standalone products in bundle.</span>
                                  </FormText>
                                </Label>
                              )}
                              className="form-control"
                              name="bundleLevel"
                              parse={identity}
                            />
                          </FormGroup>
                        </Col>
                      </Row>
                    </CardBody>
                  </Card>

                  <Card className="main-card mb-3 mt-3">
                    <CardHeader>Discounted products in bundle</CardHeader>
                    <CardBody>
                      <Row>
                        <Col xs={12} sm={12} md={12} lg={12}>
                          <ProductVariantModel       
                            isCollectionButtonEnable={true}
                            selectedProductIds={selectedProducts}
                            selectedProductVarIds={selectedVariants}
                            onChange={(products, variants) => {
                              setSelectedProducts(products);
                              setSelectedVariants(variants);
                              console.log(products, variants);
                            }}
                            totalTitle="Select Variant Product"
                            index={1}
                            methodName="Save"
                            buttonLabel="Select Products"
                            header="Product"
                            bundleLevel={values.bundleLevel}
                            checkProductStatus={checkProductStatus}
                          />
                        </Col>
                      </Row>
                    </CardBody>
                  </Card>
                  <ReactTooltip effect='solid'
                                      delayUpdate={500}
                                      html={true}
                                      place={'right'}
                                      border={true}
                                      type={'info'} multiline="true"/>
                  {checkProductStatusDraft ? <ActiveProductAlert/> : ''}
                </Col>
              </Row>
            );
          }}
        />
        {/* <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
              <h6>Bundling</h6>
              <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
              <iframe width="560" height="315" src="https://www.youtube.com/embed/63BM5YIJh70" title="YouTube video player"
              frame border="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" splashscreen></iframe>
              </div>
            </div>
          </HelpPopUp> */}
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  bundleRuleEntity: state.bundleRule.entity,
  loading: state.bundleRule.loading,
  updating: state.bundleRule.updating,
  updateSuccess: state.bundleRule.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  reset,
  createEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(subscriptionBundleSetting);
