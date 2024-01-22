import React, {Fragment, useEffect, useState} from 'react';
import {connect} from 'react-redux';
import Loader from 'react-loaders';
import {
  Button,
  Col,
  ListGroup,
  ListGroupItem,
  Row,
  Card,
  CardBody,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Label,
  Input,
  FormFeedback
} from 'reactstrap';
import {Link} from 'react-router-dom';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {deleteEntity, updateShippingEntity} from 'app/entities/delivery-profile/delivery-profile.reducer';
import {Visibility, Edit, Delete} from '@mui/icons-material';
import ConfirmDeletePopup from '../Utilities/ConfirmDeletePopup';
import './loader.scss';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import {
  getDeliveryProfiles,
  saveShippingProfileV4,
  updateShippingProfileV4
} from 'app/entities/shipping-profile/shipping-profile.reducer';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import {getEntities as getSubscriptionGroups} from 'app/entities/subscription-group/subscription-group.reducer';
import Select from 'react-select';
import {completeChecklistItem} from 'app/entities/onboarding-info/onboarding-info.reducer';
import OnboardingChecklistStep from 'app/shared/model/enumerations/onboarding-checklist-step.model';

const ShippingProfileList = (
  {
    shippingProfileEntities,
    getDeliveryProfiles,
    deleteEntity,
    loading,
    history,
    shopInfo,
    saveShippingProfileV4,
    updateShippingProfileV4,
    getSubscriptionGroups,
    subscriptionGroupEntities,
    updating,
    saving,
    deleting,
    completeChecklistItem
  },
  ...props
) => {
  const [isOpenFlag, setIsOpenFlag] = useState(false);
  const [subsctiptionPlanId, setSubsctiptionPlanId] = useState(null);
  const [processingFreeShipping, setProcessingFreeShipping] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [editProfileId, setEditProfileId] = useState(null);
  const [sellingPlanList, setSellingPlanList] = useState([]);
  const [name, setName] = useState('');
  const [updateRunning, setUpdateRunning] = useState(false);
  const [saveRunning, setSaveRunning] = useState(false);
  const [error, setError] = useState(false);

  const toggleModal = () => setIsOpenFlag(!isOpenFlag);
  const toggleShowModal = () => setShowModal(!showModal);
  const toggleShowEditModal = () => setShowEditModal(!showEditModal);

  const openEditPopup = (id, sellingPlanGroups) => {
    toggleShowEditModal();
    setEditProfileId(id);
    let t = sellingPlanGroups?.map(v => ({
      label: v.label,
      value: Number(v.value?.replace('gid://shopify/SellingPlanGroup/', '') || 0)
    }));
    setSellingPlanList(t);
  };

  useEffect(() => {
    getDeliveryProfiles();
    getSubscriptionGroups();
    completeChecklistItem(OnboardingChecklistStep.SHIPPING_PROFILE);
  }, []);

  const save = () => {
    setError(false);
    if (!name) {
      setError(true);
      return;
    }
    setSaveRunning(true);
    saveShippingProfileV4({name: name});
  };

  const update = () => {
    setUpdateRunning(true);
    updateShippingProfileV4({id: editProfileId, sellingPlanGroups: sellingPlanList});
  };

  useEffect(() => {
    if (saveRunning && !saving) {
      setSaveRunning(false);
      toggleShowModal();
      getDeliveryProfiles();
    }
  }, [saving]);

  useEffect(() => {
    if (updateRunning && !updating) {
      setUpdateRunning(false);
      toggleShowEditModal();
      getDeliveryProfiles();
    }
  }, [updating]);

  const deleteSubscriptionPlan = id => {
    deleteEntity(id).then(response => {
      getDeliveryProfiles();
      toggleModal();
    });
  };

  const onDeleteModalClose = () => {
    setSubsctiptionPlanId(null);
    toggleModal();
  };

  const removeSubscriptionGrp = id => {
    setSubsctiptionPlanId(id);
    setIsOpenFlag(true);
  };
  return (
    <FeatureAccessCheck hasAnyAuthorities={'enableShippingProfiles'}
                        upgradeButtonText="Upgrade to enable Shipping Profiles">
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
            heading="Shipping Profile - Add/Manage"
            icon="lnr-cart icon-gradient"
            enablePageTitleAction={true}
            actionTitle="Create Shipping Profile"
            onActionClick={toggleShowModal}
            sticky={true}
            tutorialButton={{
              show: true,
              docs: [
                {
                  title: "How to create custom shipping profiles for subscription orders",
                  url: "https://intercom.help/appstle/en/articles/5212799-how-to-create-custom-shipping-profiles-for-subscription-orders"
                },
                {
                  title: "How to set up a minimum order value",
                  url: "https://intercom.help/appstle/en/articles/8047823-how-to-set-up-a-minimum-order-value"
                }
              ]
            }}
          />

          {loading && !shippingProfileEntities.length > 0 ? (
            <div style={{margin: '10% 0 0 43%'}}
                 className="loader-wrapper d-flex justify-content-center align-items-center">
              <Loader type="line-scale"/>
            </div>
          ) : (
            <Fragment>
              <ListGroup>
                <ListGroupItem>
                  <div className="border-bottom mb-3 pb-3">
                    <div className="d-flex justify-content-between">
                      <h5 className="fw-bold">CustomShipping Profile</h5>
                    </div>
                    <div>
                      <ol>
                        <li>Create shipping profile here.</li>
                        <li>

                          Press 'Edit On Shopify' (which takes you to Shipping and delivery settings on Shopify) and set
                          rate and zone. <strong>DO
                          NOT add any products directly to the profile. (Only subscription plans should be attached to
                          these profiles).</strong>
                        </li>
                        <li>Come back here and add the subscription plans.</li>
                      </ol>
                    </div>
                    {/* <div className="border">
                  <Alert
                    type= 'warning'
                    message="Editing Shipping Profile is only available when shipping profile is not already"
                    banner
                    showIcon
                  />
                </div> */}
                  </div>
                  {shippingProfileEntities?.map(item => (
                    <Card className="mt-2">
                      <CardBody>
                        <div className="d-flex justify-content-between w-100">
                          <div>
                            <h5>
                              <b>{item.name}</b>
                            </h5>
                            <div>
                              Subscription
                              Plans: <b>{item?.sellingPlanGroups?.map(value => value?.label)?.join(', ') || '-'}</b>
                            </div>
                          </div>
                          <div className="d-flex justify-content-end text-right">
                            <Link onClick={() => openEditPopup(item?.id, item?.sellingPlanGroups || [])}>
                              <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill" title="Edit" color="info">
                                <Edit/> Edit Subscription Plans
                              </Button>
                            </Link>
                            <a
                              target="_blank"
                              href={`https://${shopInfo?.shop}/admin/settings/shipping/profiles/${item?.id?.replace(
                                'gid://shopify/DeliveryProfile/',
                                ''
                              )}`}
                            >
                              <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill" title="Edit" color="info">
                                <Edit/> Edit On Shopify
                              </Button>
                            </a>
                            <Link to={`/dashboards/shipping-profile-v2/${item.profileId}/details`}>
                              <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill" title="View Details"
                                      color="info">
                                <Visibility/> View Profile
                              </Button>
                            </Link>
                            <Link onClick={() => removeSubscriptionGrp(item.profileId)}>
                              <Button
                                className="mb-2 mr-2 btn-icon btn-icon-only btn-pill"
                                title="Remove Subscription Plan"
                                color="warning"
                              >
                                <Delete/>
                              </Button>
                            </Link>
                          </div>
                        </div>
                      </CardBody>
                    </Card>
                  ))}
                </ListGroupItem>
              </ListGroup>
            </Fragment>
          )}
          <Modal isOpen={showModal} toggle={toggleShowModal}>
            <ModalHeader toggle={toggleShowModal}>Create Shipping Profile</ModalHeader>
            <ModalBody>
              <div className="pt-3">
                <Label>Shipping Profile Name</Label>
                <Input type="name" placeholder="Please enter profile value" onChange={e => setName(e.target.value)}/>
                <spam style={{color: 'red', fontSize: '12px', paddingLeft: '3px'}} className={!error ? 'd-none' : ''}>
                  Shipping profile name is required.
                </spam>
              </div>
              <div className="pt-3" style={{fontSize: 'small'}}>
                <spam>This name will help you recognize this shipping profile on Shopify.</spam>
              </div>
            </ModalBody>
            <ModalFooter>
              <Button color="link" onClick={toggleShowModal}>
                {' '}
                Cancel{' '}
              </Button>
              <MySaveButton onClick={() => save()} text="Create" updating={saving} updatingText={'Creating'}/>
            </ModalFooter>
          </Modal>

          <Modal isOpen={showEditModal} toggle={toggleShowEditModal}>
            <ModalHeader toggle={toggleShowEditModal}>Manage Subscription Groups</ModalHeader>
            <ModalBody>
              <div className="pt-3">
                <Label>Select Subscription Groups</Label>
                <Select
                  isMulti={true}
                  onChange={e => setSellingPlanList(e)}
                  value={sellingPlanList}
                  options={subscriptionGroupEntities?.map(v => ({label: v?.groupName, value: v?.id}))}
                />
              </div>
            </ModalBody>
            <ModalFooter>
              <Button color="link" onClick={toggleShowEditModal}>
                {' '}
                Cancel{' '}
              </Button>
              <MySaveButton onClick={() => update()} text="Update" updating={updating} updatingText={'Updating'}/>
            </ModalFooter>
          </Modal>
        </ReactCSSTransitionGroup>
        <ConfirmDeletePopup
          buttonLabel="Mybutton"
          modaltitle="Subscription Plan "
          modalMessage="Are you sure to delete ?"
          confirmBtnText="Yes"
          cancelBtnText="No"
          isOpenFlag={isOpenFlag}
          toggleModal={toggleModal}
          onCloseModel={onDeleteModalClose}
          deleteId={subsctiptionPlanId}
          deleteLoading={deleting}
          deleteEntity={id => deleteSubscriptionPlan(id)}
        />
      </Fragment>
    </FeatureAccessCheck>
  );
};

const mapStateToProps = state => ({
  account: state.authentication.account,
  shippingProfileEntities: state.shippingProfile.entities,
  shippingProfileEntity: state.shippingProfile.entity,
  loading: state.shippingProfile.loading,
  updating: state.shippingProfile.updating,
  saving: state.shippingProfile.saving,
  updateSuccess: state.shippingProfile.updateSuccess,
  shopInfo: state.shopInfo.entity,
  subscriptionGroupEntities: state.subscriptionGroup.entities,
  deleting: state.deliveryProfile.deleting
});

const mapDispatchToProps = {
  getDeliveryProfiles,
  updateShippingEntity,
  deleteEntity,
  saveShippingProfileV4,
  updateShippingProfileV4,
  getSubscriptionGroups,
  completeChecklistItem,
};

export default connect(mapStateToProps, mapDispatchToProps)(ShippingProfileList);
