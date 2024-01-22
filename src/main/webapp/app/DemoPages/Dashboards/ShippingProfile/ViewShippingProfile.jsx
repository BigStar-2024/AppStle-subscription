import React, { Fragment, useEffect, useState, useReducer, useRef } from 'react';
import Loader from 'react-loaders';
import axios from 'axios';
//import './setting.scss';0
import { Link } from 'react-router-dom';

import Select, { components } from 'react-select';
import {
  Card,
  Badge,
  CardBody,
  Table,
  CardFooter,
  Col,
  Collapse,
  FormGroup,
  FormFeedback,
  FormText,
  Input,
  Button,
  InputGroup,
  Label,
  Modal,
  CardHeader,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Row
} from 'reactstrap';
import Switch from 'react-switch';
import { Field, Form } from 'react-final-form';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect, useSelector } from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { createShippingEntity, getEntity, reset, updateEntity } from 'app/entities/delivery-profile/delivery-profile.reducer';
import MultiselectModal from 'app/DemoPages/Dashboards/TaggingRules/components/MultiselectModal';
import arrayMutators from 'final-form-arrays';
import { FieldArray } from 'react-final-form-arrays';
import { OnChange } from 'react-final-form-listeners';
import _, { trim } from 'lodash';
import './CreateShippingProfile.scss';
import { LocalShipping } from '@mui/icons-material';
import {getEntities} from "app/entities/subscription-group/subscription-group.reducer";
const ViewShippingProfile = ({ getEntity, shippingProfileEntity,getEntities,subscriptionGrpEntities, subscriptionGroupLoading, ...props }) => {
  const [shippingProfileData, setShippingProfileData] = useState([]);

  useEffect(() => {
    getEntities();
    getEntity(props?.match?.params?.id);
  }, []);

  useEffect(() => {
    setShippingProfileData(shippingProfileEntity);
  }, [shippingProfileEntity]);

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
          heading={'Shipping Profile Details'}
          icon="lnr-gift icon-gradient bg-mean-fruit"
          enablePageTitleAction
            actionTitle="Edit Shipping Profile"
            onActionClick={() => {
              props.history.push(`/dashboards/shipping-profile-v2/${props?.match?.params?.id}/edit`);
            }}
          enableSecondaryPageTitleAction={true}
          secondaryActionTitle="Back"
          onSecondaryActionClick={() => {
            props.history.push(`/dashboards/shipping-profile`);
          }}
          //   formErrors={formErrors}
          //   errorsVisibilityToggle={errorsVisibilityToggle}
          //   onActionUpdating={updating}
        />
        {(props.loading || subscriptionGroupLoading) ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div>
        ) : (
          <>
            <Row>
              <Col md={4}>
                <h6 style={{ fontWeight: 'bold' }}>
                  <i className="header-icon lnr-book icon-gradient bg-plum-plate"> </i> Shipping Profile Name
                </h6>
              </Col>
              <Col md={8}>
                <Card>
                  <CardBody>
                    <h6>
                      <b>
                        <LocalShipping /> {shippingProfileData?.deliveryProfile?.name}
                      </b>
                    </h6>
                  </CardBody>
                </Card>
              </Col>
            </Row>
            <hr />
            <Row>
              <Col md={4}>
                <h6 style={{ fontWeight: 'bold' }}>
                  <i className="header-icon lnr-book icon-gradient bg-plum-plate"> </i> Associated Selling Plan Groups
                </h6>
              </Col>
              <Col md={8}>
                <Card>
                  <CardBody>
                    <p>
                      <b>Subscription Plan Groups</b>
                    </p>
                    {shippingProfileData?.deliveryProfile?.sellingPlanGroups?.edges?.length > 0 ? (
                      <Table style={{ textAlign: 'center' }}>
                        <thead>
                          <tr>
                            <th>No.</th>
                            <th>Subscription Plans Name</th>
                            {/* <th>Last Name</th> */}
                          </tr>
                        </thead>
                        <tbody>
                          {shippingProfileData?.deliveryProfile?.sellingPlanGroups?.edges.map(({ node }, index) => (
                            <tr>
                              <th scope="row">{index + 1}</th>
                              <td>
                                {
                                  subscriptionGrpEntities && subscriptionGrpEntities.map((item, index) => {
                                    if(item?.id === parseInt(node?.id?.split('/').pop())) {
                                      return (
                                        <span key={item?.id}>{item?.groupName}</span>
                                      )
                                    }
                                  })
                                }
                              </td>
                              {/* <td>{}</td> */}
                            </tr>
                          ))}
                        </tbody>
                      </Table>
                    ) : (
                      <h6 style={{ textAlign: 'center' }}> There is no selling plan group associated</h6>
                    )}
                  </CardBody>
                </Card>
              </Col>
            </Row>
            <hr />

            <Row>
              <Col md={4}>
                <h6 style={{ fontWeight: 'bold' }}>
                  {' '}
                  <i className="header-icon lnr-book icon-gradient bg-plum-plate"> </i>
                  Shipping Profile Location Infos
                </h6>
              </Col>
              <Col md={8}>
                <Card>
                  <CardBody>
                    {shippingProfileEntity?.deliveryProfile?.profileLocationGroups.map(({ locationGroup }) => {
                      return (
                        <>
                          <p>
                            <b>Locations</b>
                          </p>
                          <Table>
                            <thead>
                              <tr>
                                <th>No.</th>
                                <th>Location Name</th>
                              </tr>
                            </thead>
                            <tbody>
                              {locationGroup?.locations?.edges?.map((ctr, index) => (
                                <tr>
                                  <th scope="row">{index + 1}</th>
                                  <td>{ctr?.node?.name}</td>
                                </tr>
                              ))}
                            </tbody>
                          </Table>
                        </>
                      );
                    })}
                  </CardBody>
                </Card>
              </Col>
            </Row>
            <hr />

            <Row>
              <Col md={4}>
                <h6 style={{ fontWeight: 'bold' }}>
                  {' '}
                  <i className="header-icon lnr-book icon-gradient bg-plum-plate"> </i>
                  Shipping Profile Location Groups
                </h6>
              </Col>
              <Col md={8}>
                <Card>
                  <CardBody>
                    {shippingProfileEntity?.deliveryProfile?.profileLocationGroups.map(({ locationGroupZones }) => {
                      return locationGroupZones?.edges?.map(({ node }) => {
                        return (
                          <>
                            <>
                              {node?.zone?.countries?.length > 0 ? (
                                node?.zone?.countries[0]?.code.restOfWorld == true ? (
                                  <>
                                    <p>
                                      <b>Delivery Zone</b>
                                    </p>
                                    <Badge color="primary" style={{ padding: '7px', fontSize: '13px' }}>
                                      All Countries
                                    </Badge>
                                  </>
                                ) : (
                                  <></>
                                )
                              ) : (
                                <>
                                  <p>
                                    <b>Delivery Zone</b>
                                  </p>
                                  <h6> Country Not available</h6>
                                </>
                              )}
                            </>
                            <>
                              {node?.zone?.countries?.length > 0 ? (
                                node?.zone?.countries[0]?.code.restOfWorld == true ? (
                                  <></>
                                ) : (
                                  <>
                                    {node?.zone?.countries?.map((ctr, index) => (
                                      <div className="countryInfo">
                                        <p>
                                          <b>Delivery Zone</b>
                                        </p>
                                        <Table>
                                          <thead>
                                            <tr>
                                              <th>Country No.</th>
                                              <th>Country Name</th>
                                              <th>Country Code</th>
                                            </tr>
                                          </thead>
                                          <tbody>
                                            <tr>
                                              <th scope="row">{index + 1}</th>
                                              <td>{ctr?.translatedName}</td>
                                              <td>
                                                <b>{ctr?.code?.countryCode}</b>
                                              </td>
                                            </tr>
                                          </tbody>
                                        </Table>

                                        {node?.zone?.countries[index]?.provinces?.length > 0 &&
                                        node?.zone?.countries[index]?.code.restOfWorld == false ? (
                                          <>
                                            <p>
                                              <b>Province List</b>
                                            </p>
                                            <Table>
                                              <thead>
                                                <tr>
                                                  <th>No.</th>
                                                  <th>Province Name</th>
                                                  <th>Province Code</th>
                                                </tr>
                                              </thead>
                                              <tbody>
                                                {node?.zone?.countries[index]?.provinces?.map((ctr, index) => (
                                                  <tr>
                                                    <th scope="row">{index + 1}</th>
                                                    <td>{ctr?.name}</td>
                                                    <td>
                                                      <b>{ctr?.code}</b>
                                                    </td>
                                                  </tr>
                                                ))}
                                              </tbody>
                                            </Table>
                                          </>
                                        ) : (
                                          <></>
                                        )}
                                      </div>
                                    ))}
                                  </>
                                )
                              ) : (
                                <></>
                              )}
                            </>
                            <hr />
                            <>
                              {node?.methodDefinitions?.edges.map(({ node }, index) => {
                                return (
                                  <>
                                    <p>
                                      <b>{index + 1} ). Delivery Service</b>
                                    </p>
                                    {node?.rateProvider?.carrierService ? (
                                      <div className="deliveryMethod">
                                        <div>
                                          <b>{node?.name}</b>
                                        </div>
                                        <div>{node?.rateProvider?.carrierService?.formattedName}</div>
                                      </div>
                                    ) : (
                                      <div className="deliveryMethod">
                                        <div>
                                          <b>{node?.name}</b>
                                        </div>
                                        <div>
                                          Amount:{' '}
                                          <Badge color="info" style={{ padding: '5px', fontSize: '14px' }}>
                                            {' '}
                                            {node?.rateProvider?.price?.amount} {node?.rateProvider?.price?.currencyCode}
                                          </Badge>
                                        </div>
                                      </div>
                                    )}
                                    <div className="deliveryMethod" style={{ marginTop: '5px' }}>
                                      <b>Delivery Conditions </b>
                                      {node?.methodConditions?.length > 0 ? (
                                        <>
                                          <div>
                                            {node?.methodConditions?.map((item, index) => (
                                              <>
                                                <div style={{ fontSize: '15px' }}>
                                                  <b> {index + 1} ).</b>
                                                  &nbsp; If &nbsp;
                                                  <span style={{ textTransform: 'lowercase' }}>{item?.field?.replaceAll('_', ' ')}</span>
                                                  &nbsp; is &nbsp;
                                                  <span style={{ textTransform: 'lowercase' }}>{item?.operator?.replaceAll('_', ' ')}</span>
                                                  &nbsp;
                                                  {item?.conditionCriteria?.value ? (
                                                    item?.conditionCriteria?.value
                                                  ) : (
                                                    <b>{item?.conditionCriteria?.amount}</b>
                                                  )}{' '}
                                                  &nbsp;
                                                  <span>
                                                    {item?.conditionCriteria?.unit
                                                      ? item?.conditionCriteria?.unit
                                                      : item?.conditionCriteria?.currencyCode}
                                                  </span>
                                                </div>
                                              </>
                                            ))}
                                          </div>
                                        </>
                                      ) : (
                                        <p>There is no condition</p>
                                      )}
                                    </div>
                                    <hr />
                                  </>
                                );
                              })}
                            </>
                          </>
                        );
                      });
                    })}
                  </CardBody>
                </Card>
              </Col>
            </Row>
          </>
        )}
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = storeState => ({
  shippingProfileEntity: storeState.deliveryProfile.entity,
  loading: storeState.deliveryProfile.loading,
  updating: storeState.deliveryProfile.updating,
  updateSuccess: storeState.deliveryProfile.updateSuccess,
  subscriptionGrpEntities: storeState.subscriptionGroup.entities,
  subscriptionGroupLoading: storeState.subscriptionGroup.loading,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  reset,
  getEntities
};

export default connect(mapStateToProps, mapDispatchToProps)(ViewShippingProfile);
