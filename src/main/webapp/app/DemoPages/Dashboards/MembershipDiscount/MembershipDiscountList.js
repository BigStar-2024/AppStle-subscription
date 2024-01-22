import React, { useEffect, Fragment, useState } from 'react';
import { connect } from 'react-redux';
import Loader from 'react-loaders';
import {
    Button,
    Table,
    Row,
    Col,
    Card,
    CardBody
} from 'reactstrap';
import { Link } from "react-router-dom";
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { getEntities, deleteEntity } from 'app/entities/membership-discount/membership-discount.reducer'
import ConfirmDeletePopup from '../Utilities/ConfirmDeletePopup';

function MembershipDiscountList(props) {
    const { membershipDiscountEntities, getEntities, loading, history, deleteEntity } = props;
    const [isOpenFlag, setIsOpenFlag] = useState(false);
    const [entityId, setEntityId] = useState(null)
    const toggleModal = () => setIsOpenFlag(!isOpenFlag);
    const removeEntity = (id) => {
        setEntityId(id);
        setIsOpenFlag(true);
    }
    const deleteCurrentItem = (id) => {
        toggleModal();
        deleteEntity(id);
    }
    const onDeleteModalClose = () => {
        setEntityId(null);
        toggleModal();
    }
    useEffect(() => {
        getEntities()
    }, [])
    return (
        <Fragment>
            <ReactCSSTransitionGroup
                component="div"
                transitionName="TabsAnimation"
                transitionAppear
                transitionAppearTimeout={0}
                transitionEnter={false}
                transitionLeave={false}>
                <PageTitle heading="Membership Discount - Manage"
                    subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/4924892-how-do-i-create-a-subscription-plan' target='blank'> Need help? Follow these instruction to manage membership discount.</a>"
                    icon="pe-7s-network icon-gradient"
                    enablePageTitleAction
                    actionTitle="Create Membership Discount"
                    onActionClick={() => {
                        history.push('/dashboards/membership-discount/new');
                    }}
                />
                {
                    loading && !membershipDiscountEntities.length > 0 ?
                        (<div style={{ margin: "10% 0 0 43%" }} className="loader-wrapper d-flex justify-content-center align-items-center">
                            <Loader type="line-scale" />
                        </div>)
                        :
                        membershipDiscountEntities && membershipDiscountEntities.length > 0 ?
                            <Fragment>
                                <Card>
                                    <CardBody>
                                        <Table className="mb-0">
                                            <thead>
                                                <tr>
                                                    <th>Title</th>
                                                    <th>Discount(%)</th>
                                                    <th>Customer Tags</th>
                                                    <th style={{ textAlign: "center" }}>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {
                                                    membershipDiscountEntities?.map((item) => {
                                                        return (
                                                            <tr key={item.id}>
                                                                <td>{item.title}</td>
                                                                <td>{item.discount || '-'}</td>
                                                                <td>{item.customerTags || '-'}</td>
                                                                <td style={{ textAlign: "center" }}>
                                                                    <Link to={`/dashboards/membership-discount/${item.id}/edit`}>
                                                                        <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill" title="Edit Membership Discount" color="info">
                                                                            <i className="lnr-pencil btn-icon-wrapper"> </i>
                                                                        </Button>
                                                                    </Link>
                                                                    <Link onClick={() => removeEntity(item.id)}>
                                                                        <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill" title="Remove Subscription Plan" color="warning">
                                                                            <i className="lnr-trash btn-icon-wrapper"> </i>
                                                                        </Button>
                                                                    </Link>

                                                                </td>
                                                            </tr>
                                                        )
                                                    })
                                                }
                                            </tbody>
                                        </Table>
                                    </CardBody>
                                </Card>
                            </Fragment>
                            :
                            (<Row className="align-items-center welcome-page">
                                <Col sm='5'>
                                    <div>
                                        <h4>Welcome to Membership Discount</h4>
                                        <p className='text-muted'>
                                            This page will give you a quick drive-through of all the currently available membership discount in your store.
                                        </p>
                                        <p className='text-muted'>
                                            Read the <a href="https://intercom.help/appstle/en/articles/4924892-how-do-i-create-a-subscription-plan" target="blank">doc</a> to know more about creating subscription bundling, and learn how other merchants are using subscription bundling!

                                        </p>
                                        <p className='text-muted'>If you have any questions at any time, just reach out to us on <a
                                            href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>

                                        <br /> <br />
                                    </div>
                                </Col>
                            </Row>)
                }
                {/* DELETE POPUP MODEL */}
                <ConfirmDeletePopup
                    buttonLabel="Mybutton"
                    modaltitle="Membership Discount "
                    modalMessage="Are you sure to delete ?"
                    confirmBtnText="Yes"
                    cancelBtnText="No"
                    isOpenFlag={isOpenFlag}
                    toggleModal={toggleModal}
                    onCloseModel={onDeleteModalClose}
                    deleteId={entityId}
                    deleteEntity={id => deleteCurrentItem(id)}
                />
            </ReactCSSTransitionGroup>

        </Fragment>
    )
}

const mapStateToProps = state => ({
    membershipDiscountEntities: state.membershipDiscount.entities,
    loading: state.membershipDiscount.loading,
});

const mapDispatchToProps = {
    getEntities,
    deleteEntity
};
export default connect(mapStateToProps, mapDispatchToProps)(MembershipDiscountList)
