import React, {Fragment, useEffect, useState} from 'react';
import {FiPlus} from 'react-icons/fi';
import {connect} from 'react-redux';
import {
  Alert,
  Button,
  Card,
  CardBody,
  CardHeader,
  Col,
  FormGroup,
  Input,
  InputGroup,
  InputGroupAddon,
  Label,
  Row,
  Table,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
} from 'reactstrap';
import AppstleMenuModal from './AppstleMenuModal';
import './buildAppstleMenu.scss';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  createEntity,
  getAppstleMenuSettingsByShop,
  updateEntity
} from '../../../entities/appstle-menu-settings/appstle-menu-settings.reducer';
import cx from 'classnames';
import {getEntities as getSubscriptionGroups} from 'app/entities/subscription-group/subscription-group.reducer';
import {getCollections} from 'app/entities/appstle-menu-settings/appstle-menu-admin.reducer';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faExternalLinkAlt} from "@fortawesome/free-solid-svg-icons";
import {Field, Form} from "react-final-form";
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";
import YoutubeVideoPlayer from '../Tutorials/YoutubeVideoPlayer';

const toastOptions = {
  position: 'top-center',
  autoClose: 500,
  hideProgressBar: false,
  closeOnClick: true,
  pauseOnHover: true,
  draggable: true,
  progress: undefined
};
const buttonTextArray = [
  {label: 'One time purchase', value: 'ONE_TIME'},
  {label: 'Subscriptions', value: 'SUBSCRIBE'}
  // {label: 'Bundle collection', value: 'BUNDLE'}
];
const initState = {
  menuType: '',
  menuTitle: '',
  menuIcon: '',
  sourceCollection: '',
  subscriptionGroup: '',
  sourceCollectionTitle: '',
  filterGroups: []
};
const BuildAppstleMenu = ({
                            account,
                            updateEntity,
                            getAppstleMenuSettingsByShop,
                            appstleMenuSettingsEntity,
                            shopInfo,
                            createEntity,
                            updating,
                            getSubscriptionGroups,
                            getCollections,
                            shopifyShopInfo
                          }) => {
  console.log(shopifyShopInfo)
  const [modalTitle, setModalTitle] = useState('');
  const [openModal, setOpenModal] = useState(false);
  const [formErrors, setFormErrors] = useState(null);
  const [errorsVisibilityToggle, setErrorsVisibilityToggle] = useState(false);
  const [filterInputValue, setFilterInputValue] = useState('');
  const [filterInputLabel, setFilterInputLabel] = useState('');
  const [modalConditionCase, setModalConditionCase] = useState('add');
  const [modalCaseIndex, setModalCaseIndex] = useState(null);
  const [openAddFilter, setOpenAddFilter] = useState(false);
  const [menuHeaderList, setMenuHeaderList] = useState([]);
  const [accordion, setAccordion] = useState([]);
  const [isPageAccessible, setIsPageAccessible] = useState(true);
  const [initStateAppstleMenu, setInitStateAppstleMenu] = useState(initState);
  useEffect(() => {
    getAppstleMenuSettingsByShop();
    getSubscriptionGroups();
    getCollections();
  }, []);
  useEffect(() => {
    if (initStateAppstleMenu && initStateAppstleMenu?.filterGroups?.length) {
      setAccordion(accordion);
    } else {
      setAccordion([]);
    }
  }, [initStateAppstleMenu]);
  useEffect(() => {
    if (appstleMenuSettingsEntity) {
      let menuData = appstleMenuSettingsEntity?.filterMenu ? JSON.parse(appstleMenuSettingsEntity?.filterMenu) : [];
      setInitStateAppstleMenu(menuData);
      setMenuHeaderList(menuData);
    }
  }, [appstleMenuSettingsEntity]);

  const headerOptionshandler = value => {
    if (value) {
      setInitStateAppstleMenu({
        ...initState,
        menuType: buttonTextArray.find(item => item.value === value)?.value,
        filterGroups: []
      });

      setModalConditionCase('add');
      setModalCaseIndex(null);
      setModalTitle(value);
      setOpenModal(true);
      setOpenAddFilter(false);
    }
  };
  const addTagHandler = index => {
    if (
      index !== null &&
      initStateAppstleMenu?.filterGroups[index].filterOptions.findIndex(item => item.filterOptionValue === filterInputValue) === -1 &&
      filterInputValue
    ) {
      initStateAppstleMenu?.filterGroups[index].filterOptions.push({
        filterOptionTitle: filterInputLabel,
        filterOptionIcon: '',
        filterOptionValue: filterInputValue
      });
      let DuplicateObject = {...initStateAppstleMenu?.filterGroups[index]};
      let duplicateArray = [...initStateAppstleMenu?.filterGroups];
      duplicateArray[index] = {...DuplicateObject, filterGroupFieldInValid: false};
      setInitStateAppstleMenu({...initStateAppstleMenu, filterGroups: duplicateArray});
      setFilterInputValue('');
      setFilterInputLabel('');
    } else {
      let DuplicateObject = {...initStateAppstleMenu?.filterGroups[index]};
      let duplicateArray = [...initStateAppstleMenu?.filterGroups];
      duplicateArray[index] = {...DuplicateObject, filterGroupFieldInValid: true};
      setInitStateAppstleMenu({...initStateAppstleMenu, filterGroups: duplicateArray});
    }
  };
  const deleteTagHanfler = (index, tagName) => {
    const duplicateArray = [...initStateAppstleMenu?.filterGroups[index].filterOptions];
    if (tagName && index !== null) {
      const findindex = duplicateArray.findIndex(item => item.filterOptionValue === tagName);
      if (findindex !== null) {
        duplicateArray.splice(findindex, 1);
      }
      initStateAppstleMenu?.filterGroups[index].filterOptions.splice(findindex, 1);
      setInitStateAppstleMenu({...initStateAppstleMenu});
    }
  };
  const deleteGroupHandler = index => {
    if (index !== null) {
      const duplicateArray = [...initStateAppstleMenu.filterGroups];
      const duplicateAccordion = [...accordion];
      duplicateAccordion.splice(index, 1);
      duplicateArray.splice(index, 1);
      setAccordion(duplicateAccordion);
      toggleAccordion(index, duplicateAccordion);
      setInitStateAppstleMenu({...initStateAppstleMenu, filterGroups: duplicateArray});
    }
  };
  const deleteMenuHeaderEntity = index => {
    const duplicateArray = [...menuHeaderList];
    if (duplicateArray?.length && index !== null) {
      duplicateArray.splice(index, 1);
    }
    setMenuHeaderList([...duplicateArray]);
  };
  const editHeaderOptionsHandler = (dataForEdit, index) => {
    setAccordion([]);
    if (dataForEdit) {
      console.log(dataForEdit, 'dataForEdit');
      for (let i = 0; i < dataForEdit.filterGroups.length; i++) {
        accordion.push(false);
        setOpenAddFilter(true);
        setAccordion([...accordion]);
      }
      setInitStateAppstleMenu({...dataForEdit});
      setModalCaseIndex(index);
      setModalConditionCase('edit');
      setFilterInputValue('');
      setFilterInputLabel('');
      setOpenModal(true);
      setModalTitle(dataForEdit.menuType);
    }
  };
  const addFilterOptionHandler = () => {
    const indexes = initStateAppstleMenu?.filterGroups.reduce((r, v, i) => r.concat(v.filterGroupTitle === '' ? i : []), []);
    if (indexes?.length) {
      indexes.map(indexes =>
        initStateAppstleMenu.filterGroups.forEach((item, index) =>
          !item.filterGroupTitle ? (item.filterGroupFieldNameInValid = true) : (item.filterGroupFieldNameInValid = false)
        )
      );
      setInitStateAppstleMenu({...initStateAppstleMenu});
    } else {
      initStateAppstleMenu.filterGroups.forEach((item, index) =>
        !item.filterGroupTitle ? (item.filterGroupFieldNameInValid = true) : (item.filterGroupFieldNameInValid = false)
      );
      accordion.push(true);
      setAccordion(accordion);
      if (accordion.length) {
        let accordionState = accordion.map((item, index, array) => (index === array.length - 1 ? true : false));
        setAccordion(accordionState);
      }
      initStateAppstleMenu.filterGroups.push({
        filterGroupTitle: '',
        filterGroupIcon: '',
        filterGroupFieldInValid: false,
        filterGroupFieldNameInValid: false,
        filterGroupFieldNameErrorText: '',
        filterGroupFieldOpen: 0,
        filterGroupType: 'tag_filter',
        filterOptions: []
      });
      setOpenAddFilter(true);
      setInitStateAppstleMenu({...initStateAppstleMenu});
    }
  };
  const addToMenuHandler = () => {
    const indexes = initStateAppstleMenu?.filterGroups.reduce((r, v, i) => r.concat(v.filterGroupTitle === '' ? i : []), []);
    if (indexes?.length) {
      indexes.map(indexes =>
        initStateAppstleMenu.filterGroups.forEach((item, index) =>
          !item.filterGroupTitle ? (item.filterGroupFieldNameInValid = true) : (item.filterGroupFieldNameInValid = false)
        )
      );
      setInitStateAppstleMenu({...initStateAppstleMenu});
    } else {
      initStateAppstleMenu.filterGroups.forEach((item, index) =>
        !item.filterGroupTitle ? (item.filterGroupFieldNameInValid = true) : (item.filterGroupFieldNameInValid = false)
      );
      setInitStateAppstleMenu({...initStateAppstleMenu});
      if (modalConditionCase === 'edit') {
        menuHeaderList[modalCaseIndex] = {...initStateAppstleMenu};
      } else {
        menuHeaderList.push(initStateAppstleMenu);
      }
      setMenuHeaderList([...menuHeaderList]);
      setOpenModal(false);
    }
  };

  const toggleAccordion = (tab, subAccordion) => {
    const state = subAccordion.map((x, index) => (tab === index ? !x : false));
    setAccordion(state);
  };
  const saveEntity = (entity) => {
    entity.filterMenu = JSON.stringify(menuHeaderList);
    if (appstleMenuSettingsEntity.id) {
      updateEntity(entity)
    } else {
      createEntity(entity)
    }
  };

  let submit;

  const [showModal, setShowModal] = useState(false);
  const toggleShowModal = () => setShowModal(!showModal);

  return (
    <Fragment>
      <PageTitle
        heading="Build an Appstle Menu"
        // subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://help.shopify.com/en/manual/apps/custom-apps' target='blank'> Learn how to create private app and get store front access token.</a>"
        icon="lnr-pencil icon-gradient bg-mean-fruit"
        actionTitle="Save"
        enablePageTitleAction={isPageAccessible}
        onActionClick={() => submit()}
        formErrors={formErrors}
        errorsVisibilityToggle={errorsVisibilityToggle}
        onActionUpdating={updating}
        updatingText="Updating"
        sticky={true}
        tutorialButton={{
          show: true,
          videos: [
            {
              title: "Appstle Menu",
              url: "https://www.youtube.com/watch?v=EqcHDgBBjhU",
            },
          ],
          docs: [
            {
              title: "What is 'Appstle Menu'? How Does it Improve Customer Experience and Retention?",
              url: "https://intercom.help/appstle/en/articles/6733809-what-is-appstle-menu-how-does-it-improve-customer-experience-and-retention"
            }
          ]
        }}
      />
      <FeatureAccessCheck setIsPageAccessible={setIsPageAccessible} hasAnyAuthorities={'accessAppstleMenu'} upgradeButtonText="Upgrade your plan">
        <div className="mt-3 mb-3">
          {shopInfo?.storeFrontAccessToken == null || shopInfo?.storeFrontAccessToken === '' ? (
            <Row>
              <Alert color={'warning'} className="appstle_error_box  w-100">
                <div>
                  <p>
                    Store front access token is not added yet, please add storefront access token to
                    use appstle menu features. Go to Shop
                    Settings > Add Storefront access token.
                  </p>
                  <a href='https://help.shopify.com/en/manual/apps/custom-apps' target='blank'> Learn how to create
                    private app and get storefront access token.</a>
                </div>
              </Alert>
            </Row>
          ) : (
            ''
          )}
        </div>

        {/* <Alert className="mb-2" color="warning">
          <h6>Need assistance?</h6>
          <p>Watch our tutorial videos or read our help documentation to get a better understanding.</p>
          <span>
            <Button color="warning" onClick={toggleShowModal}>Video Tutorials</Button>
            <a
              href='https://intercom.help/appstle/en/articles/6733809-what-is-appstle-menu-how-does-it-improve-customer-experience-and-retention'
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
              <h6>Appstle Menu</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=EqcHDgBBjhU"
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

        <Form
          initialValues={appstleMenuSettingsEntity}
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
              <form onSubmit={handleSubmit}>
                <div className="mt-3 mb-3">
                  <Card className="AppstleMenu-wrapper">
                    <CardHeader className={'d-flex justify-content-between align-items-center'}>
                      <div>Add headers to your Appstle Menu</div>
                      <div className="status-button d-flex flex-column align-items-center p-2">
                        <Field
                          render={({input, meta}) => {
                            return (
                              <div
                                className="switch has-switch"
                                data-on-label="ACTIVE"
                                data-off-label="PAUSED"
                                onClick={event => {
                                  input.onChange(!input.value);
                                }}>
                                <div className={cx('switch-animate', {
                                  'switch-on': input.value,
                                  'switch-off': !input.value
                                })}>
                                  <input type="checkbox"/>
                                  <span className="switch-left bg-success">ON</span>
                                  <label>&nbsp;</label>
                                  <span className="switch-right bg-success">OFF</span>
                                </div>
                              </div>
                            );
                          }}
                          validate={() => {
                          }}
                          autoComplete="off"
                          id={'active'}
                          className="form-control"
                          name={'active'}
                        />
                      </div>
                    </CardHeader>
                    <CardBody>
                      Menu headers sit at the top of the page and allow your customers to toggle between the
                      options you'd like to display. Click on
                      the 3 options above to add a header of that type. You can mix and match headers, and add
                      additional product filters to each
                      header.
                      <div className="mt-3">
                        <Card>
                          <CardHeader>Settings</CardHeader>
                          <CardBody>
                            <Row className={'mb-3'}>
                              <Col lg={6} md={6}>
                                <FormGroup>
                                  <Label for="handle">
                                    <strong>Name your template</strong>
                                  </Label>

                                  <Field
                                    render={({input, meta}) => {
                                      return (
                                        <InputGroup>
                                          <InputGroupAddon addonType='prepend'>
                                            {`https://${appstleMenuSettingsEntity?.shop}/pages/`}
                                          </InputGroupAddon>
                                          <Input
                                            {...input}
                                            placeholder="Enter your page handle name "
                                          />
                                          <InputGroupAddon addonType='append'>
                                            <Button
                                              color="primary"
                                              onClick={() => window.open(`https://${appstleMenuSettingsEntity?.shop}/pages/${input.value}`, '_blank')}
                                            >
                                              <FontAwesomeIcon icon={faExternalLinkAlt}/>
                                            </Button>
                                          </InputGroupAddon>
                                        </InputGroup>
                                      );
                                    }}
                                    validate={() => {
                                    }}
                                    autoComplete="off"
                                    id={'handle'}
                                    className="form-control"
                                    name={'handle'}
                                  />
                                </FormGroup>
                              </Col>

                              <Col lg={6} md={6}>
                                <FormGroup>
                                  <Label for="productViewStyle">
                                    <strong>Product view style</strong>
                                  </Label>

                                  <Field
                                    render={({input, meta}) => {
                                      return (
                                        <Input
                                          type="select"
                                          invalid={meta.error && meta.touched ? true : null}
                                          {...input}
                                        >
                                          <option value="QUICK_ADD">Quick View</option>
                                          <option value="VIEW_DETAILS">View Details</option>
                                        </Input>
                                      );
                                    }}
                                    validate={() => {
                                    }}
                                    autoComplete="off"
                                    id={'productViewStyle'}
                                    className="form-control"
                                    name={'productViewStyle'}
                                  />
                                </FormGroup>
                              </Col>
                            </Row>
                          </CardBody>
                        </Card>
                        <Row className={"mt-3"}>
                          <Col lg={6} md={6} className="custom-menu-header">
                            <Card>
                              <CardHeader>Menu Headers</CardHeader>
                              <CardBody>
                                <Table className="mb-0">
                                  <thead>
                                  <tr>
                                    <th>Name</th>
                                    <th style={{textAlign: 'center'}}>Actions</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  {menuHeaderList?.length
                                    ? menuHeaderList?.map((item, index) => {
                                      return (
                                        <tr>
                                          <td>{item.menuTitle}</td>
                                          <td style={{textAlign: 'center'}}>
                                            <Button
                                              className="mb-2 mr-2 btn-icon btn-icon-only btn-pill"
                                              title="Edit Subscription Plan"
                                              color="info"
                                              onClick={() => editHeaderOptionsHandler(item, index)}
                                            >
                                              <i className="lnr-pencil btn-icon-wrapper"> </i>
                                            </Button>
                                            <Button
                                              className="mb-2 mr-2 btn-icon btn-icon-only btn-pill"
                                              title="Remove Subscription Plan"
                                              color="warning"
                                              onClick={() => deleteMenuHeaderEntity(index)}
                                            >
                                              <i className="lnr-trash btn-icon-wrapper"> </i>
                                            </Button>
                                          </td>
                                        </tr>
                                      );
                                    })
                                    : ''}
                                  </tbody>
                                </Table>
                              </CardBody>
                            </Card>
                          </Col>
                          <Col lg={6} md={6} className="custom-header-options">
                            <Card>
                              <CardHeader>Add menu header</CardHeader>
                              <CardBody>
                                {buttonTextArray?.length
                                  ? buttonTextArray.map(btn => {
                                    return (
                                      <div className="custom-button">
                                        <Button color="primary" className="mt-2"
                                                onClick={() => headerOptionshandler(btn.value)}>
                                          {btn.label} <FiPlus/>
                                        </Button>
                                      </div>
                                    );
                                  })
                                  : null}
                              </CardBody>
                            </Card>
                          </Col>
                        </Row>
                      </div>
                    </CardBody>
                    <AppstleMenuModal
                      show={openModal}
                      setShow={setOpenModal}
                      selected={buttonTextArray.find(item => item.value === modalTitle)}
                      addToMenuHandler={addToMenuHandler}
                      openAddFilter={openAddFilter}
                      setOpenAddFilter={setOpenAddFilter}
                      addTagHandler={addTagHandler}
                      setFilterInputValue={setFilterInputValue}
                      filterInputValue={filterInputValue}
                      setFilterInputLabel={setFilterInputLabel}
                      filterInputLabel={filterInputLabel}
                      deleteTagHanfler={deleteTagHanfler}
                      setInitStateAppstleMenu={setInitStateAppstleMenu}
                      deleteGroupHandler={deleteGroupHandler}
                      toggleAccordion={toggleAccordion}
                      addFilterOptionHandler={addFilterOptionHandler}
                      accordion={accordion}
                      setAccordion={setAccordion}
                      initStateAppstleMenu={initStateAppstleMenu}
                      modalConditionCase={modalConditionCase}
                    />
                  </Card>
                </div>
              </form>
            );
          }}
        />
        {/* <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
              <h6>Appstle Menu</h6>
              <YoutubeVideoPlayer
                url="https://www.youtube.com/watch?v=EqcHDgBBjhU"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />
            </div>
          </HelpPopUp> */}
      </FeatureAccessCheck>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  account: state.authentication.account,
  appstleMenuSettingsEntity: state.appstleMenuSettings.entity,
  shopInfo: state.shopInfo.entity,
  updating: state.appstleMenuSettings.updating,
  shopifyShopInfo: state.shopifyShopInfo,

});

const mapDispatchToProps = {
  updateEntity,
  createEntity,
  getSubscriptionGroups,
  getCollections,
  getAppstleMenuSettingsByShop
};

export default connect(mapStateToProps, mapDispatchToProps)(BuildAppstleMenu);
