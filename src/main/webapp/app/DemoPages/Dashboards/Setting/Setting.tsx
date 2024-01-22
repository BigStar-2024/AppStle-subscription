import {IRootState} from 'app/shared/reducers';
import React, {Fragment, useEffect, useState} from 'react';
import {
  Card,
  CardBody,
  Col,
  Collapse,
  FormGroup,
  FormText,
  Input,
  InputGroup,
  InputGroupAddon,
  Label,
  Row
} from 'reactstrap';
import {Field, Form} from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {connect} from 'react-redux';
import {RouteComponentProps} from 'react-router-dom';
import PageTitle from '../../../Layout/AppMain/PageTitle';
//import '../RecentProcessedOrder/RecentProcessedOrder.scss';
import {createEntity, getEntityByShop, reset, updateEntity} from "app/entities/shop-settings/shop-settings.reducer";
import {DelayedTaggingUnit} from "app/shared/model/enumerations/delayed-tagging-unit.model";

export interface ITagginRulesProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
}

export const Setting = (props: ITagginRulesProps) => {
  const [isChecked, setChecked] = useState(false);

  const {shopSettingsEntity, loading, updating} = props;

  const handleCheck = event => {
    setChecked(event.target.checked);
  };

  useEffect(() => {
    props.getEntityByShop();
  }, []);

  const saveEntity = values => {
    const entity = {
      ...shopSettingsEntity,
      ...values
    };
    props.updateEntity(entity);
  };

  let submit;
  const identity = value => value;

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
        {/* <div className="page-title"> */}
        <PageTitle
          heading="Settings"
          subheading=""
          icon="pe-7s-menu icon-gradient bg-mean-fruit"
          enablePageTitleAction
          actionTitle="Save"
          onActionClick={() => {
            submit();
          }}
          onActionUpdating={updating}
        />
        <div>
          <CardBody>
            <Form
              initialValues={shopSettingsEntity}
              onSubmit={saveEntity}
              validate={values => {
                const errors = {} as any;
                return errors;
              }}
              render={({handleSubmit, form, submitting, pristine, values}) => {
                submit = handleSubmit;
                return (
                  <form onSubmit={handleSubmit}>
                    <Row>
                      <Col md="4">
                        <h6>Tagging Status</h6>
                        <FormText>
                          <p>You can choose to disable all tagging. This can be helpful when you are running bulk
                            imports or you
                            need to temporarily disable tagging.</p>
                        </FormText>
                      </Col>
                      <Col md="8">
                        <Card className="card-margin">
                          <CardBody>
                            <Row>
                              <Col xs={12} sm={12} md={12} lg={12} className="md-6">

                                <FormGroup check>
                                  <Field
                                    render={({input, meta}) => (
                                      <Label check>
                                        <Input {...input} type="checkbox" id="taggingEnabled"  />{' '}
                                        Tagging enabled
                                        <FormText>
                                          <span>You can choose to disable all tagging.</span>
                                        </FormText>
                                      </Label>
                                    )}
                                    id="shopSettings-taggingEnabled"
                                    type="checkbox"
                                    className="form-check-input"
                                    name="taggingEnabled"
                                  />

                                </FormGroup>
                              </Col>
                            </Row>
                          </CardBody>
                        </Card>
                      </Col>
                    </Row>
                    <hr/>
                    <Row>
                      <Col md="4">
                        <h6>Delayed Tagging</h6>
                        <FormText>
                          <p>You can delay processing and tagging, this can be helpful when you use other apps to update
                            newly
                            created orders.</p>
                        </FormText>
                      </Col>
                      <Col md="8">
                        <Card className="card-margin">
                          <CardBody>
                            <Row>
                              <Col xs={12} sm={12} md={12} lg={12} className="md-6">

                                <FormGroup check>
                                  <Field
                                    render={({input, meta}) => (
                                      <Label check>
                                        <Input {...input} type="checkbox" id="delayTagging"  />{' '}
                                        Delay Tagging
                                      </Label>
                                    )}
                                    id="shopSettings-delayTagging"
                                    type="checkbox"
                                    className="form-check-input"
                                    name="delayTagging"
                                  />
                                </FormGroup>
                                <Collapse isOpen={values.delayTagging}>
                                  <FormGroup>
                                    <Label className="mt-2" for="delayedTaggingValue">
                                      How long would you like to delay tagging?
                                    </Label>
                                    <InputGroup>

                                      <Field
                                        render={({input, meta}) => (
                                          <Input
                                            {...input}
                                            id="delayedTaggingValue"
                                            type="number"
                                            className="form-control"
                                            name="delayedTaggingValue"
                                          />
                                        )}
                                        id="shopSettings-delayedTaggingValue"
                                        className="form-control"
                                        type="number"
                                        name="delayedTaggingValue"
                                      />

                                      <InputGroupAddon addonType='append'>

                                        <Field
                                          render={({input, meta}) => (
                                            <Input
                                              {...input}
                                              type="select"
                                              name="delayedTaggingUnit"
                                              id="delayedTaggingUnit"
                                              value={(shopSettingsEntity.delayedTaggingUnit) || 'SECONDS'}
                                              >
                                              <option value="SECONDS">Seconds</option>
                                              <option value="MINUTES">Minutes</option>
                                              <option value="HOURS">Hours</option>
                                              <option value="DAYS">Days</option>
                                            </Input>
                                          )}
                                          id="shopSettings-delayedTaggingUnit"
                                          className="form-control"
                                          type="select"
                                          name="delayedTaggingUnit"
                                        />
                                      </InputGroupAddon>
                                    </InputGroup>
                                  </FormGroup>
                                </Collapse>
                              </Col>
                            </Row>
                          </CardBody>
                        </Card>
                      </Col>
                    </Row>
                    <hr/>
                    <Row>
                      <Col md="4">
                        <h6>Global Conditions</h6>
                        <FormText>
                          <p>You can set global conditions to filter which orders are processed. This can be helpful
                            when you
                            only need to tag based on orders which are in a certain state.</p>
                        </FormText>
                      </Col>
                      <Col md="8">
                        <Card className="card-margin">
                          <CardBody>
                            <Row>
                              <Col xs={12} sm={12} md={12} lg={12} className="md-6">

                                <FormGroup>
                                  <Label className="mt-0" for="orderStatus">
                                    Order Status
                                  </Label>
                                  <Field
                                    render={({input, meta}) => (
                                      <Input {...input} type="select" id="orderStatus" >
                                        <option value="ANY">Any</option>
                                        <option value="OPEN">Open</option>
                                        <option value="ARCHIVED">Archived</option>
                                      </Input>
                                    )}
                                    id="shopSettings-orderStatus"
                                    className="form-control"
                                    type="select"
                                    name="orderStatus"
                                  />
                                </FormGroup>
                                <FormGroup>
                                  <Label for="paymentStatus">
                                    Payment Status
                                  </Label>
                                  <Field
                                    render={({input, meta}) => (
                                      <Input {...input} type="select" id="paymentStatus" >
                                        <option value="ANY">Any</option>
                                        <option value="PAID">Paid</option>
                                        <option value="UNPAID">Unpaid</option>
                                      </Input>
                                    )}
                                    id="shopSettings-paymentStatus"
                                    className="form-control"
                                    type="select"
                                    name="paymentStatus"
                                  />
                                </FormGroup>
                                <FormGroup>
                                  <Label for="fulfillmentStatus">
                                    Fullfillment Status
                                  </Label>

                                  <Field
                                    render={({input, meta}) => (
                                      <Input {...input} type="select" id="fulfillmentStatus" >
                                        <option value="ANY">Any</option>
                                        <option value="UNFULFILLED">Unfullfilled</option>
                                        <option value="FULFILLED">Fullfilled</option>
                                      </Input>
                                    )}
                                    id="shopSettings-fulfillmentStatus"
                                    className="form-control"
                                    type="select"
                                    name="fulfillmentStatus"
                                  />
                                  <FormText>
                                    Please note: Fulfillment status will only be checked for rules using the newly
                                    created order
                                    trigger (via the orders/create
                                    webhook)
                                  </FormText>
                                </FormGroup>
                              </Col>
                            </Row>
                          </CardBody>
                        </Card>
                      </Col>
                    </Row>
                    <hr/>
                  </form>
                );
              }}
            />
          </CardBody>
        </div>

      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  shopSettingsEntity: storeState.shopSettings.entity,
  loading: storeState.shopSettings.loading,
  updating: storeState.shopSettings.updating,
  updateSuccess: storeState.shopSettings.updateSuccess
});

const mapDispatchToProps = {
  getEntityByShop,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Setting);
