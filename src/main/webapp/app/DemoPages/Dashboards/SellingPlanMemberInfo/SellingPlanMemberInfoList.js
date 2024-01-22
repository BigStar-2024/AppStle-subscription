import React, { useState, useEffect, Fragment, useCallback } from 'react';
import { connect } from 'react-redux';
import Loader from 'react-loaders';
import {
    Button,
    Table,
    InputGroup,
    Input,
    Label,
    FormGroup,
    Row,
    Col,
    Card,
    CardBody,
    ListGroup,
    UncontrolledButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle,
    ListGroupItem,
    Pagination,
    PaginationItem,
    PaginationLink
} from 'reactstrap';
import { Link } from "react-router-dom";
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { getEntities } from 'app/entities/selling-plan-member-info/selling-plan-member-info.reducer'
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";


const SellingPlanMemberInfoList = ({ sellingPlanMemberInfoEntities, getEntities, loading, history }) => {
    useEffect(() => {
        getEntities()
    }, [])

    return (
      <FeatureAccessCheck
        hasAnyAuthorities={'enableAdvancedSellingPlans'}
        upgradeButtonText="Upgrade to enable advanced selling plans"
      >
        <Fragment>
            <ReactCSSTransitionGroup
                component="div"
                transitionName="TabsAnimation"
                transitionAppear
                transitionAppearTimeout={0}
                transitionEnter={false}
                transitionLeave={false}>
                <PageTitle heading="Segment Based Plans - Manage"
                    icon="pe-7s-network icon-gradient"
                />
                {
                    loading && !sellingPlanMemberInfoEntities.length > 0 ?
                        (<div style={{ margin: "10% 0 0 43%" }} className="loader-wrapper d-flex justify-content-center align-items-center">
                            <Loader type="line-scale" />
                        </div>)
                        :
                        sellingPlanMemberInfoEntities && sellingPlanMemberInfoEntities.length > 0 ?
                            <Fragment>
                                <ListGroup>
                                    <ListGroupItem>
                                        <Table className="mb-0">
                                            <thead>
                                                <tr>
                                                    <th>Name</th>
                                                    <th>Order Frequency</th>
                                                    <th>Include customers with a specific tag</th>
                                                    <th>Customers with this tag CAN subscribe to products in this plan:</th>
                                                    <th>Exclude customers with a specific tag</th>
                                                    <th>Customers with this tag CANNOT subscribe to products in this plan:</th>
                                                    <th style={{ textAlign: "center" }}>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {
                                                    sellingPlanMemberInfoEntities?.map((item) => {
                                                        return (
                                                            <tr key={item.sellingPlanId}>
                                                                <td>{item.frequencyName} - {item.groupName}</td>
                                                                <td>{item.frequencyCount} {item.frequencyInterval}</td>
                                                                <td>{item.enableMemberInclusiveTag ? "Yes" : "No"}</td>
                                                                <td>{item.memberInclusiveTags ? _.replace(item.memberInclusiveTags, new RegExp(",","g"), ", ") : "-"}</td>
                                                                <td>{item.enableMemberExclusiveTag ? "Yes" : "No"}</td>
                                                                <td>{item.memberExclusiveTags ? _.replace(item.memberExclusiveTags, new RegExp(",","g"), ", ") : "-"}</td>
                                                                <td style={{ textAlign: "center" }}>
                                                                    <Link to={`/dashboards/selling-plans/${item.subscriptionId}/${item.sellingPlanId}/edit`}>
                                                                        <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill" title="Edit Selling Plan" color="info">
                                                                            <i className="lnr-pencil btn-icon-wrapper"> </i>
                                                                        </Button>
                                                                    </Link>


                                                                </td>
                                                            </tr>
                                                        )
                                                    })
                                                }
                                            </tbody>
                                        </Table>
                                    </ListGroupItem>
                                </ListGroup>
                            </Fragment>
                            :
                            (<Row className="align-items-center welcome-page">
                                <Col sm='5'>
                                    <div>
                                        <h4>Welcome to Advanced Selling Plan or Customer Segment Based Plan</h4>
                                        <p className='text-muted'>
                                            This section would allow you to configure subscriptions plans for member only or non-member only.
                 </p>
                                        <p className='text-muted'>
                                            Read the <a href="https://intercom.help/appstle/en/articles/4924892-how-do-i-create-a-subscription-plan" target="blank">doc</a> to know more about creating selling plans, and learn how other merchants are using selling plan!

                 </p>
                                        <p className='text-muted'>If you have any questions at any time, just reach out to us on <a
                                            href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>

                                        <br /> <br />
                                    </div>
                                </Col>
                            </Row>)
                }
            </ReactCSSTransitionGroup>

        </Fragment>
      </FeatureAccessCheck>
    )
}

const mapStateToProps = state => ({
    sellingPlanMemberInfoEntities: state.sellingPlanMemberInfo.entities,
    loading: state.sellingPlanMemberInfo.loading,
});

const mapDispatchToProps = {
    getEntities
};

export default connect(mapStateToProps, mapDispatchToProps)(SellingPlanMemberInfoList);
