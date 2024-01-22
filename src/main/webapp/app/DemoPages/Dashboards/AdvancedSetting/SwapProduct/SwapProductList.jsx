import React, {useState, useEffect, Fragment, useCallback} from 'react';
import {connect} from 'react-redux';
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
  PaginationLink,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Alert
} from 'reactstrap';
import axios from "axios";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faExternalLinkSquareAlt} from '@fortawesome/free-solid-svg-icons';
import {Field, Form} from 'react-final-form';
import {Link} from "react-router-dom";
import {JhiItemCount, JhiPagination} from "react-jhipster";
import {useHistory, useLocation, useParams} from 'react-router';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import {
  getEntities,
  createEntity,
  getEntity,
  updateEntity,
  deleteEntity
} from 'app/entities/product-swap/product-swap.reducer'
import ConfirmDeletePopup from '../../Utilities/ConfirmDeletePopup';
import {toast} from 'react-toastify';
import './swapproduct.scss';
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";
import YoutubeVideoPlayer from '../../Tutorials/YoutubeVideoPlayer';
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";

const ShippingProfileList = ({
                               shippingProfileEntities,
                               shippingProfileEntity,
                               getEntities,
                               getEntity,
                               updateEntity,
                               account,
                               createEntity,
                               deleteEntity, loading, history
                             }) => {

  const [searchValue, setSearchValue] = useState('');
  const [checked, setchecked] = useState(false);
  const [isOpenFlag, setIsOpenFlag] = useState(false);
  const [swapProductId, setSwapProductId] = useState(null);
  const [processingFreeShipping, setProcessingFreeShipping] = useState(false);
  const [subPlanIds, setSubPlanIds] = useState([])
  const [isPageAccessible, setIsPageAccessible] = useState(true);
  const toggleModal = () => setIsOpenFlag(!isOpenFlag);
  useEffect(() => {
    getEntities()
  }, [])

  const toastOption = {
    position: toast.POSITION.BOTTOM_CENTER
  }

  const intitialState = {
    search: ''
  };

  const removeSwapProduct = (id) => {
    setSwapProductId(id);
    setIsOpenFlag(true);
  }

  const deleteSwapProduct = (id) => {
    toggleModal();
    deleteEntity(id);
  }

  const onDeleteModalClose = () => {
    setSwapProductId(null);
    toggleModal();
  }

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
          transitionLeave={false}>
          <PageTitle heading="Product Swap Automation"
                     subheading="<i class='metismenu-icon lnr-location'></i> <a href='https://intercom.help/appstle/en/articles/5213399-how-to-set-up-automatic-product-swap' target='blank'> Click here to know more about product swap automation.</a>"
                     icon="pe-7s-repeat icon-gradient"
                     enablePageTitleAction={isPageAccessible}
                     actionTitle="Create Product Swap Automation"
                     onActionClick={() => {
                       history.push('/dashboards/swap-product/new');
                     }}
                     sticky={true}
                     tutorialButton={{
                      show: true,
                      videos: [
                        {
                          title: "Swap Automation - Subscriptions",
                          url: "https://youtu.be/WdvrWjk3uFo",
                        },
                        {
                          title: "Swap Automation - Advanced Use Case",
                          url: "https://youtu.be/muZtybC2u7w",
                        }
                      ],
                      docs: [
                        {
                          title: "How to Set Up Automatic Product Swap",
                          url: "https://intercom.help/appstle/en/articles/5213399-how-to-set-up-automatic-product-swap"
                        }
                      ]
                    }}
          />
          <FeatureAccessCheck setIsPageAccessible={setIsPageAccessible} hasAnyAuthorities={'enableProductSwapAutomation'} upgradeButtonText="Upgrade to enable Product Swap Automation">
          {
            loading && !shippingProfileEntities.length > 0 ?
              (<div style={{margin: "10% 0 0 43%"}}
                    className="loader-wrapper d-flex justify-content-center align-items-center">
                <Loader type="line-scale"/>
              </div>)
              :
              shippingProfileEntities && shippingProfileEntities.length > 0 ?
                <Fragment>
                  <Row className="align-items-center" style={{marginLeft: '0px'}}>
                    <Col>
                      <div>
                        <h4>Welcome to Product Swap Automation</h4>
                        <p className='text-muted'>
                          Product Swaps Automation works only with Pay As You Go Plan.
                        </p>
                        <p className='text-muted'> For more details, just reach out to us on <a
                          href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>
                      </div>
                    </Col>
                  </Row>
                  <ListGroup>
                    <ListGroupItem>
                      <Table className="mb-0">
                        <thead>
                        <tr>
                          <th>Name</th>
                          <th>Source Variants Count</th>
                          <th>Destination Variants Count</th>
                          {/*<th>Updated First Order?</th>*/}
                          <th>Every Recurring Order?</th>
                          <th>Keep Discount?</th>
                          <th>For Specific Billing Cycle</th>
                          <th style={{textAlign: "center"}}>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                          shippingProfileEntities?.map((item) => {
                            return (
                              <tr>
                                <td>{item?.name}</td>
                                <td>{JSON.parse(item.sourceVariants).length == 1 ? JSON.parse(item.sourceVariants).length + ' Product' : JSON.parse(item.sourceVariants).length + ' Products'} </td>
                                <td>{JSON.parse(item.destinationVariants).length == 1 ? JSON.parse(item.destinationVariants).length + ' Product' : JSON.parse(item.destinationVariants).length + ' Products'} </td>
                                {/*<td>{item.updatedFirstOrder ? 'True' : 'False'}</td>*/}
                                <td>{item.checkForEveryRecurringOrder ? 'True' : 'False'}</td>
                                <td>{item.carryDiscountForward ? 'True' : 'False'}</td>
                                <td>{item.forBillingCycle}</td>
                                <td style={{textAlign: "center"}}>
                                  <Link to={{pathname: `/dashboards/swap-product/${item.id}/edit`, variantObj: item}}>
                                    <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill"
                                            title="Edit Swap Product" color="info">
                                      <i className="lnr-pencil btn-icon-wrapper"> </i>
                                    </Button>
                                  </Link>

                                  <Link onClick={() => removeSwapProduct(item.id)}>
                                    <Button className="mb-2 mr-2 btn-icon btn-icon-only btn-pill"
                                            title="Remove swap Product" color="warning">
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
                    </ListGroupItem>
                  </ListGroup>
                </Fragment>
                :
                (<div className='welcome-page-with-video'>
                  <Row className="align-items-center welcome-page-with-video">
                    <Col sm='7'>
                      {/* <Alert className="mb-2" color="warning">
                        <h6>Need assistance?</h6>
                        <p>Watch our tutorial videos or read our help documentation to get a better understanding.</p>
                        <span>
                          <Button color="warning" onClick={toggleShowModal}>Video Tutorials</Button>
                          <a
                            href='https://intercom.help/appstle/en/articles/5213399-how-to-set-up-automatic-product-swap'
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
                          <div style={{ height: '500px', overflowY: 'scroll' }}>
                            <div className="mt-4 border-bottom pb-4">
                              <h6>Product Swap Automation - Introduction</h6>
                              <YoutubeVideoPlayer
                                url="https://youtu.be/_IN9qQAv5I0"
                                iframeHeight="100%"
                                divClassName="video-container"
                                iframeClassName="responsive-iframe"
                              />
                            </div>
                            <div className="py-4 border-bottom">
                            <h6>Product Swap Automation - Product Cycle</h6>
                              <YoutubeVideoPlayer
                                url="https://youtu.be/wtxfw2icreE"
                                iframeHeight="100%"
                                divClassName="video-container"
                                iframeClassName="responsive-iframe"
                              />
                            </div>
                            <div className="py-4 border-bottom">
                              <h6>Product Swap Automation - Advanced Use</h6>
                              <YoutubeVideoPlayer
                                url="https://youtu.be/81x6rcpmJT8"
                                iframeHeight="100%"
                                divClassName="video-container"
                                iframeClassName="responsive-iframe"
                              />
                            </div>
                          </div>
                        </ModalBody>
                        <ModalFooter>
                          <Button color="link" onClick={toggleShowModal}>Cancel</Button>
                        </ModalFooter>
                      </Modal> */}
                      <div>
                        <h4>Welcome to Product Swap</h4>
                        <p className='text-muted'>
                          This page will enable you to ‘automate’ product changes (swapping, adding, deleting) in an
                          active subscription.
                        </p>

                        <p className='text-muted'>
                          You can also do product swaps ‘manually’, for which you need to edit the subscription
                          contract.
                          <a
                            href="https://intercom.help/appstle/en/articles/5195883-how-can-a-merchant-add-swap-delete-products"
                            target="_blank"> Learn more</a>
                        </p>

                        <p className='text-muted'>
                          You can also use the product swap feature, for product cycling, and deliver different
                          variations
                          of a product in each recurring order. For example, if your customers have a monthly
                          subscription
                          of ice creams, you can use the product swap feature to deliver a different flavor every month.
                        </p>

                        <p className='text-muted'>If you have any questions at any time, just reach out to us on <a
                          href="javascript:window.Intercom('showNewMessage')">our chat widget</a></p>
                        <Button
                          color="primary"
                          size='lg'
                          onClick={() => {
                            history.push('/dashboards/swap-product/new');
                          }}>
                          <span className='font-size-lg font-weight-light'>Create Product Swap Automation</span>
                        </Button>
                        <br/> <br/>
                      </div>
                    </Col>
                  </Row>
                  {/* <h5 className="mt-3">Help Videos</h5> */}
                  {/* <Row>
                    <Col md={4}>
                      <Card style={{marginLeft: 'auto', borderRadius: '18px'}}
                            className="card-border mr-3 mb-3 card-hover-shadow-2x">
                        <CardBody className="top-elem">
                          <div className='text-center pb-3'>Product Swap Automation - Introduction</div>
                          <div className="text-center">
                            <YoutubeVideoPlayer url={'https://youtu.be/_IN9qQAv5I0'}/>
                          </div>

                        </CardBody>
                      </Card>
                    </Col>
                    <Col md={4}>
                      <Card style={{marginLeft: 'auto', borderRadius: '18px'}}
                            className="card-border mr-3 mb-3 card-hover-shadow-2x">
                        <CardBody className="top-elem">
                          <div className='text-center pb-3'>Product Swap Automation - Product Cycle</div>
                          <div className="text-center">
                            <YoutubeVideoPlayer url={'https://youtu.be/wtxfw2icreE'}/>
                          </div>

                        </CardBody>
                      </Card>
                    </Col>
                    <Col md={4}>
                      <Card style={{marginLeft: 'auto', borderRadius: '18px'}}
                            className="card-border mr-3 mb-3 card-hover-shadow-2x">
                        <CardBody className="top-elem">
                          <div className='text-center pb-3'>Product Swap Automation - Advance Use</div>
                          <div className="text-center">
                            <YoutubeVideoPlayer url={'https://youtu.be/81x6rcpmJT8'}/>
                          </div>

                        </CardBody>
                      </Card>
                    </Col>
                  </Row> */}
                </div>)

          }
          </FeatureAccessCheck>
          {
            (shippingProfileEntities && shippingProfileEntities.length >= 1) && <HelpPopUp>
              <div className="mt-4">
                <h6>Product Swap Automation - Basic Introduction</h6>
                <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
                  <iframe className="embed-responsive-item" src="https://www.youtube.com/embed/_IN9qQAv5I0"
                          allowFullScreen/>
                </div>
              </div>
              <div className="my-4">
                <h6>Product Swap Automation - Product Cycle</h6>
                <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
                  <iframe className="embed-responsive-item" src="https://www.youtube.com/embed/wtxfw2icreE"
                          allowFullScreen/>
                </div>
              </div>
              <div className="my-4">
                <h6>Product Swap Automation - Advance Use Case</h6>
                <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
                  <iframe className="embed-responsive-item" src="https://www.youtube.com/embed/81x6rcpmJT8"
                          allowFullScreen/>
                </div>
              </div>
            </HelpPopUp>
          }

        </ReactCSSTransitionGroup>

        {/* DELETE POPUP MODEL */}
        <ConfirmDeletePopup
          buttonLabel="Mybutton"
          modaltitle="Swap Product"
          modalMessage="Are you sure to delete ?"
          confirmBtnText="Yes"
          cancelBtnText="No"
          isOpenFlag={isOpenFlag}
          toggleModal={toggleModal}
          onCloseModel={onDeleteModalClose}
          deleteId={swapProductId}
          deleteEntity={id => deleteSwapProduct(id)}
        />
      </Fragment>
  )
}

const mapStateToProps = state => ({
  shippingProfileEntities: state.productSwap.entities,
  shippingProfileEntity: state.productSwap.entity,
  loading: state.productSwap.loading,
  account: state.authentication.account
});

const mapDispatchToProps = {
  getEntities,
  getEntity,
  updateEntity,
  createEntity,
  deleteEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(ShippingProfileList);
