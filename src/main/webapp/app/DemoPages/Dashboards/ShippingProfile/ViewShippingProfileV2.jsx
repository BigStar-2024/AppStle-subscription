import React, {Fragment, useEffect} from 'react';
import {useParams, useHistory} from "react-router-dom";
import {connect} from 'react-redux';
import {getShippingProfileV3} from "app/entities/shipping-profile/shipping-profile.reducer";
import PageTitle from "app/Layout/AppMain/PageTitle";
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import Loader from "react-loaders";
import {Badge, Card, CardBody, Col, Row, Table} from "reactstrap";
import {LocalShipping} from "@mui/icons-material";
import {getEntities} from "app/entities/subscription-group/subscription-group.reducer";
import {getAvailableCarrier, getCountries} from "app/entities/shipping-profile/helper-data.reducer";


const ViewShippingProfileV2 = ({
                                 getShippingProfileV3,
                                 shippingProfileEntity,
                                 loading,
                                 getEntities,
                                 subscriptionGrpEntities,
                                 subscriptionGroupLoading,
                                 getAvailableCarrier,
                                 carriers,
                                 getCountries,
                                 countries,
                                 helperActionsLoading,
                                 ...props
                               }) => {
  const params = useParams();
  const history = useHistory();

  useEffect(() => {
    getShippingProfileV3(params.id);
    getEntities();
    getAvailableCarrier();
    getCountries();
  }, [])

  const populateApplicableProvinces = (countryData) => {
    if (countryData?.availableProvinces) {
      return countryData?.availableProvinces?.map(ele => ({
        value: ele.code,
        label: `${ele.code} - ${ele.name}`,
      }));
    } else {
      if (countryData.value) {
        let country = countries.find((item) => countryData?.value.toString().includes(item.code));
        if (country) {
          return country?.provinces?.map(ele => ({
            value: ele.code,
            label: `${ele.code} - ${ele.name}`,
          }));
        } else {
          return [];
        }
      } else {
        return [];
      }
    }
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
        <PageTitle
          heading={'Shipping Profile Details'}
          icon="lnr-gift icon-gradient bg-mean-fruit"
          enablePageTitleAction={false}
          actionTitle="Edit Shipping Profile"
          onActionClick={() => {
            history.push(`/dashboards/shipping-profile-v2/${params.id}/edit`);
          }}
          enableSecondaryPageTitleAction={true}
          secondaryActionTitle="Back"
          onSecondaryActionClick={() => {
            history.push(`/dashboards/shipping-profile`);
          }}
        />
        {
          (loading || subscriptionGroupLoading || helperActionsLoading) ? (
            <div style={{margin: '10% 0 0 43%'}}
                 className="loader-wrapper d-flex justify-content-center align-items-center">
              <Loader type="line-scale"/>
            </div>
          ) : <>
            <Row>
              <Col md={4}>
                <h6 style={{fontWeight: 'bold'}}>
                  <i className="header-icon lnr-book icon-gradient bg-plum-plate"> </i> Shipping Profile Name
                </h6>
              </Col>
              <Col md={8}>
                <Card>
                  <CardBody>
                    <h6>
                      <b>
                        <LocalShipping/> {shippingProfileEntity?.name}
                      </b>
                    </h6>
                  </CardBody>
                </Card>
              </Col>
            </Row>
            <hr/>

            <Row>
              <Col md={4}>
                <h6 style={{fontWeight: 'bold'}}>
                  <i className="header-icon lnr-book icon-gradient bg-plum-plate"> </i> Associated Selling Plan Groups
                </h6>
              </Col>
              <Col md={8}>
                <Card>
                  <CardBody>
                    <p>
                      <b>Subscription Plan Groups</b>
                    </p>
                    {
                      shippingProfileEntity?.sellingPlanGroups.length > 0 ? (
                        <Table style={{textAlign: 'center'}}>
                          <thead>
                          <tr>
                            <th>No.</th>
                            <th>Subscription Plans Name</th>
                          </tr>
                          </thead>
                          <tbody>
                          {
                            shippingProfileEntity && shippingProfileEntity?.sellingPlanGroups.map((sellPlan, index) => (
                              <tr key={index}>
                                <th scope="row">{index + 1}</th>
                                <td>
                                  {
                                    subscriptionGrpEntities && subscriptionGrpEntities.map((item, index) => {
                                      if (item?.id === parseInt(sellPlan?.value?.split('/').pop())) {
                                        return (
                                          <span key={item?.id}>{item?.groupName}</span>
                                        )
                                      }
                                    })
                                  }
                                </td>
                              </tr>
                            ))}
                          </tbody>
                        </Table>
                      ) : (
                        <h6 style={{textAlign: 'center'}}> There is no selling plan group associated</h6>
                      )}
                  </CardBody>
                </Card>
              </Col>
            </Row>
            <hr/>

            <Row>
              <Col md={4}>
                <h6 style={{fontWeight: 'bold'}}>
                  {' '}
                  <i className="header-icon lnr-book icon-gradient bg-plum-plate"> </i>
                  Shipping Profile Locations
                </h6>
              </Col>
              <Col md={8}>
                <Card>
                  <CardBody>
                    {
                      (shippingProfileEntity?.locations && shippingProfileEntity?.locations.length > 0) ? <>
                        <p><b>Locations</b></p>
                        <Table>
                          <thead>
                          <tr>
                            <th>No.</th>
                            <th>Location Name</th>
                          </tr>
                          </thead>
                          <tbody>
                          {shippingProfileEntity?.locations.map((loc, index) => (
                            <tr key={index}>
                              <th scope="row">{index + 1}</th>
                              <td>{loc?.label}</td>
                            </tr>
                          ))}
                          </tbody>
                        </Table>
                      </> : <></>
                    }
                  </CardBody>
                </Card>
              </Col>
            </Row>
            <hr/>

            {
              (shippingProfileEntity?.zones && shippingProfileEntity?.zones.length > 0) ? <>

                {
                  shippingProfileEntity?.zones.map((zone, zoneIndex) => {
                    return (
                      <Row key={zoneIndex}>
                        <Col md={4}>
                          <h6 style={{fontWeight: 'bold'}}>
                            {' '}
                            <i className="header-icon lnr-book icon-gradient bg-plum-plate"> </i>
                            Shipping Profile Delivery Zones #{zoneIndex + 1}
                          </h6>
                        </Col>
                        <Col md={8}>
                          <Card className="mb-3">
                            <CardBody>
                              <div className="countryInfo">
                                {
                                  (zone?.countries.length > 0 && zone?.restOfWorld === false) ? (
                                    <>
                                      <p>
                                        <b>{zone?.countries.length > 1 ? "Delivery Countries" : "Delivery Country"}</b>
                                      </p>
                                      {
                                        zone?.countries.map((country, countryIndex) => {
                                          const availableProvinces = populateApplicableProvinces(country);
                                          return (
                                            <div key={countryIndex}>
                                              <Table responsive>
                                                <thead>
                                                <tr>
                                                  <th>Country No.</th>
                                                  <th>Country Name</th>
                                                  <th>Country Code</th>
                                                  {
                                                    zone?.countries[countryIndex]?.provinces?.length > 0 ?
                                                      <th>Provinces</th> : <></>
                                                  }
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                  <th scope="row">{countryIndex + 1}</th>
                                                  <td className="align-top">{country?.label}</td>
                                                  <td className="align-top"><b>{country?.value}</b></td>
                                                  {
                                                    (zone?.countries[countryIndex]?.provinces?.length) > 0 ?
                                                      <td className="align-top">
                                                        {
                                                          (availableProvinces.length === zone?.countries[countryIndex]?.provinces?.length) ? <>
                                                            <Badge color="secondary" style={{padding: '7px', fontSize: '13px'}}>
                                                              Included All Provinces
                                                            </Badge>
                                                          </> : <>
                                                            {
                                                              zone?.countries[countryIndex]?.provinces && zone?.countries[countryIndex]?.provinces.map((province, provinceIndex) => (
                                                                <Fragment key={provinceIndex}>
                                                                  <b>{provinceIndex + 1}.</b> {province?.value}<br/>
                                                                </Fragment>
                                                              ))
                                                            }
                                                          </>
                                                        }
                                                      </td> : <></>
                                                  }
                                                </tr>
                                                </tbody>
                                              </Table>


                                            </div>
                                          )
                                        })
                                      }
                                    </>
                                  ) : <>
                                    <p><b>Delivery Countries</b></p>
                                    <Badge color="primary" style={{padding: '7px', fontSize: '13px'}}>
                                      All Countries
                                    </Badge>
                                  </>
                                }
                              </div>
                              <hr/>
                              {
                                (zone?.deliveryMethods.length > 0) ? <>
                                  {
                                    zone?.deliveryMethods.map((method, methodIndex) => (
                                      <div className="deliveryMethod mb-3" key={methodIndex}>
                                        <p className="text-center">
                                          <b>Delivery Service #{methodIndex + 1}</b>
                                        </p>
                                        <div className="mb-4">
                                          {(method?.carrierServiceId && method?.definitionType === "CARRIER") ? (
                                            <div>
                                              <Row>
                                                <Col xs={12} sm={12} md={12} lg={12}>
                                                  {carriers?.availableCarrierServices && carriers?.availableCarrierServices?.map(({carrierService}, index) => {
                                                    if (carrierService?.id === method?.carrierServiceId) {
                                                      return (
                                                        <div key={index}>
                                                          <b>Carrier Service:{' '}</b> {carrierService?.formattedName}
                                                        </div>
                                                      )
                                                    }
                                                  })}
                                                </Col>
                                                <Col xs={12} sm={12} md={6} lg={6}>
                                                  <div>
                                                    <b>Percentage of Rate:{' '}</b>
                                                    <Badge color="info" style={{padding: '5px', fontSize: '14px'}}>
                                                      {' '} {method?.carrierPercentageFee}%
                                                    </Badge>
                                                  </div>
                                                </Col>
                                                <Col xs={12} sm={12} md={6} lg={6}>
                                                  <div>
                                                    <b>Flat Amount:{' '}</b>
                                                    <Badge color="info" style={{padding: '5px', fontSize: '14px'}}>
                                                      {' '} {method?.carrierFixedFee} {method?.currencyCode}
                                                    </Badge>
                                                  </div>
                                                </Col>
                                              </Row>
                                            </div>
                                          ) : (
                                            <div>
                                              <Row>
                                                <Col xs={12} sm={12} mg={12} lg={6}>
                                                  <div>
                                                    <b>Rate Name:</b> {method?.name}
                                                  </div>
                                                </Col>
                                                <Col xs={12} sm={12} mg={12} lg={6}>
                                                  <div>
                                                    <b>Amount:{' '}</b>
                                                    <Badge color="info" style={{padding: '5px', fontSize: '14px'}}>
                                                      {' '}
                                                      {method?.amount} {method?.currencyCode}
                                                    </Badge>
                                                  </div>
                                                </Col>

                                                {
                                                  (method?.deliveryConditionType === "PRICE") ? <>
                                                    {
                                                      (method?.minValue) ? <>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <div>
                                                            <b>Minimum Price:{' '}</b>
                                                            <Badge color="info" style={{padding: '5px', fontSize: '14px'}}>
                                                              {' '}
                                                              {method?.minValue} {method?.currencyCode}
                                                            </Badge>
                                                          </div>
                                                        </Col>
                                                      </> : <></>
                                                    }
                                                    {
                                                      (method?.maxValue) ? <>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <div>
                                                            <b>Maximum Price:{' '}</b>
                                                            <Badge color="info" style={{padding: '5px', fontSize: '14px'}}>
                                                              {' '}
                                                              {method?.maxValue} {method?.currencyCode}
                                                            </Badge>
                                                          </div>
                                                        </Col>
                                                      </> : <></>
                                                    }
                                                  </> : <>
                                                    {
                                                      (method?.minValue) ? <>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <div>
                                                            <b>Minimum Weight:{' '}</b>
                                                            <Badge color="info" style={{padding: '5px', fontSize: '14px'}}>
                                                              {' '}
                                                              {method?.minValue} {method?.weightUnit}
                                                            </Badge>
                                                          </div>
                                                        </Col>
                                                      </> : <></>
                                                    }
                                                    {
                                                      (method?.maxValue) ? <>
                                                        <Col xs={12} sm={12} md={6} lg={6}>
                                                          <div>
                                                            <b>Maximum Weight:{' '}</b>
                                                            <Badge color="info" style={{padding: '5px', fontSize: '14px'}}>
                                                              {' '}
                                                              {method?.maxValue} {method?.weightUnit}
                                                            </Badge>
                                                          </div>
                                                        </Col>
                                                      </> : <></>
                                                    }
                                                  </>
                                                }
                                              </Row>
                                            </div>
                                          )}
                                        </div>
                                      </div>
                                    ))
                                  }
                                </> : <></>
                              }
                            </CardBody>
                          </Card>
                        </Col>
                      </Row>
                    )
                  })
                }
                <hr/>
              </> : <></>
            }
          </>
        }
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = (state) => ({
  shippingProfileEntity: state.shippingProfile.entity,
  loading: state.shippingProfile.loading,
  subscriptionGrpEntities: state.subscriptionGroup.entities,
  subscriptionGroupLoading: state.subscriptionGroup.loading,
  carriers: state.helperActions.carriers,
  countries: state.helperActions.countries,
  helperActionsLoading: state.helperActions.loading,
})

const mapDispatchToProps = {
  getShippingProfileV3,
  getEntities,
  getAvailableCarrier,
  getCountries,
}
export default connect(mapStateToProps, mapDispatchToProps)(ViewShippingProfileV2);
