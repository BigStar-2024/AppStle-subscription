import React, { useState, useEffect, Fragment, useCallback } from 'react';
import { connect, useSelector } from 'react-redux';
import Loader from 'react-loaders';
import PrdVariantRadioPopup from "../PrdVariantRadioPopup";
import {
  Button,
  Table,
  InputGroup,
  Input,
  Label,
  FormGroup,
  Row,
  Col,
  FormText,
  Card,
  CardBody,
  ListGroup,
  UncontrolledButtonDropdown,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  ListGroupItem,
  Pagination,
  PaginationItem,
  PaginationLink
} from 'reactstrap';
import axios from 'axios';
import Switch from 'react-switch';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faExternalLinkSquareAlt } from '@fortawesome/free-solid-svg-icons';
import { Field, Form } from 'react-final-form';
import { Link } from 'react-router-dom';
import { JhiItemCount, JhiPagination } from 'react-jhipster';
import { useHistory, useLocation, useParams } from 'react-router';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { getEntity, removeProduct } from 'app/entities/subscriptions/subscription.reducer';
import { getEntities } from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import { toast } from 'react-toastify';
import Select, { components } from 'react-select';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import Axios from 'axios';


let subscriptionEntitiesCopy;
let subscriptionCount = 0;

const ActiveAllSubscriptionAutomation = props => {
  const [updating, setUpdating] = useState(false);
  const accountInfo = useSelector(state => state.authentication.account)

  const onSubmit = (values) => {
    setUpdating(true);
    const subscriptionId = values?.subscriptionsType === "SUBSCRIPTION_ID"
    const allSubscriptionIds = values?.subscriptionsType === "ALL_SUBSCRIPTIONS"

    axios.put(`/api/bulk-automations/status?${subscriptionId ? `contractIds=${values?.subscriptionID}&` :  ''}importedOnly=false&status=${values?.status}&allSubscriptions=${subscriptionId && false || allSubscriptionIds && true}`)
      .then(async res => {
        await props?.getAutomationStatus()
        toast.success("Bulk update process triggered !!");
        setUpdating(false);
      })
      .catch(error => {
          console.log(error)
          toast.error(error?.response?.data?.message || 'Something went wrong.');
          setUpdating(false)
        }
      )
  }

  return (
    <Fragment>
      <Form
        onSubmit={onSubmit}
        render={({
                   handleSubmit,
                   form,
                   submitting,
                   pristine,
                   values,
                   errors,
                   valid
                 }) => {
          return (
            <form onSubmit={handleSubmit}>
              <Row>
                <Field
                  type="select"
                  id={`subscriptionsType`}
                  name={`subscriptionsType`}
                  initialValue={`SUBSCRIPTION_ID`}
                  render={({input, meta}) => {
                    return <Col md={6}> <FormGroup>
                      <Label for={`subscriptionsType`}><b>Select Subscriptions Type</b></Label>
                      <Input
                        invalid={meta.error && meta.touched ? true : null}
                        {...input}
                        style={{flexGrow: 1}}

                      >
                        <option value="SUBSCRIPTION_ID">Subscription ID</option>
                        <option value="ALL_SUBSCRIPTIONS">All Subscriptions</option>
                      </Input>
                    </FormGroup>
                    </Col>
                  }}
                  className="form-control"
                />
                <Col md={6}>
                  {
                    values?.subscriptionsType === "SUBSCRIPTION_ID" && <FormGroup style={{marginBottom: '3px'}}>
                      <Label for="subscriptionID">Comma-Separated Subscription Ids</Label>
                      <Field
                        render={({input, meta}) => (
                          <>
                            <Input
                              {...input}
                              invalid={meta.error && meta.touched ? true : null}/>
                            {meta.error && (
                              <div
                                style={{
                                  order: '4',
                                  width: '100%',
                                  display: meta.error && meta.touched ? 'block' : 'none'
                                }}
                                className="invalid-feedback"
                              >
                                {meta.error}
                              </div>
                            )}
                          </>
                        )}
                        validate={value => {
                          return !value ? 'Please provide subscription Id.' : undefined;
                        }}
                        id="subscriptionID"
                        className="form-control"
                        type="text"
                        name="subscriptionID"
                        placeholder="Enter subscription Id"
                      />
                      <FormText color="muted">
                        Please enter comma-separated subscription IDs. eg. 111111, 222222, 333333, ...
                      </FormText>
                    </FormGroup>
                  }
                </Col>
              </Row>
              <Row>
                <Col md={7}>
                  <FormGroup>
                    <Label>Subscription status</Label>
                    <Field
                      type ="select"
                      render={({ input, meta }) => (
                        <>
                          <Input
                            type ="select"
                            {...input}
                            invalid={meta.error && meta.touched ? true : null} >
                            <option value="" selected>Select status</option>
                            <option value="ACTIVE">Active</option>
                            <option value="PAUSED">Paused</option>
                            <option value="CANCELLED">Cancelled</option>
                            {/* <option value="EXPIRED">Expired</option>
                                          <option value="FAILED">Failed</option> */}
                          </Input>
                          {meta.error && (
                            <div
                              style={{
                                order: '4',
                                width: '100%',
                                display: meta.error && meta.touched ? 'block' : 'none'
                              }}
                              className="invalid-feedback"
                            >
                              {meta.error}
                            </div>
                          )}
                        </>
                      )}
                      validate={value => {
                        return !value ? 'Please provide status.' : undefined;
                      }}
                      id="status"
                      name="status"
                    />
                  </FormGroup>
                </Col>
                <Col md={5} style={{marginTop: '2rem'}}>
                  <MySaveButton
                    text="Start Automation"
                    updatingText={'Processing'}
                    updating ={updating}
                    className="btn-primary"
                  >
                    Start Automation
                  </MySaveButton>
                </Col>
              </Row>
            </form>)
        }}
      />
    </Fragment>
  );
};

const mapStateToProps = state => ({
});

const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(ActiveAllSubscriptionAutomation);
