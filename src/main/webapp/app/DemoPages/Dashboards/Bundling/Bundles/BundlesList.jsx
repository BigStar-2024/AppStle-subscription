import React, { Fragment, useEffect, useState } from 'react';
import { connect } from 'react-redux';
import Loader from 'react-loaders';
import { Button, Card, CardBody, Col, Row, Alert, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { useHistory } from 'react-router';
import { deleteEntity, getBundleRulesByShop, updateIndex, updateStatus } from 'app/entities/bundle-rule/bundle-rule.reducer';
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import cx from 'classnames';
import { faBars, faPencilRuler, faPlus, faTrash } from '@fortawesome/free-solid-svg-icons';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import HelpPopUp from "app/DemoPages/Components/HelpPopUp/HelpPopUp";
import YoutubeVideoPlayer from '../../Tutorials/YoutubeVideoPlayer';

const reorder = (list, startIndex, endIndex) => {
  const result = Array.from(list);
  const [removed] = result.splice(startIndex, 1);
  result.splice(endIndex, 0, removed);
  return result;
};

function DraggableOffer({ draggableOffer, index, match, updateBundleStatus, deleteEntity }) {
  return (
    <Draggable draggableId={draggableOffer.id} index={index}>
      {provided => (
        <div
          ref={provided.innerRef}
          {...provided.draggableProps}
          {...provided.dragHandleProps}
          className={'d-flex justify-content-between p-2'}
        >
          <div className={'d-flex align-items-center'}>
            <div className="sortable-mover p-3">
              <FontAwesomeIcon icon={faBars} style={{ fontSize: 24 }} />
            </div>
            <div className="sortable-second p-2">
              <span className="display-block">
                <b>{draggableOffer.name}</b>
              </span>
              <br />
              <span className="display-block">{draggableOffer.title}</span>
            </div>
          </div>

          <div className="sortable-third d-flex float-right align-items-center">
            <div className="status-button d-flex flex-column align-items-center p-2">
              <div
                className="switch has-switch"
                data-on-label="ACTIVE"
                data-off-label="PAUSED"
                onClick={event => {
                  updateBundleStatus(draggableOffer.id, !(draggableOffer.status === 'ACTIVE'));
                }}
              >
                <div
                  className={cx('switch-animate', {
                    'switch-on': draggableOffer.status === 'ACTIVE',
                    'switch-off': !(draggableOffer.status === 'ACTIVE')
                  })}
                >
                  <input type="checkbox" />
                  <span className="switch-left bg-success">ON</span>
                  <label>&nbsp;</label>
                  <span className="switch-right bg-success">OFF</span>
                </div>
              </div>
            </div>
            <div className="action-button flex-column align-items-center">
              <Button color={'primary'} tag={Link} to={`/dashboards/bundles/${draggableOffer.id}/edit`} icon="pencil-alt">
                <FontAwesomeIcon icon={faPencilRuler} />
                &nbsp;Edit
              </Button>
              &nbsp;
              <Button color={'danger'} tag={Link} to={`/dashboards/bundles/${draggableOffer.id}/delete`} icon="trash">
                <FontAwesomeIcon icon={faTrash} />
                &nbsp;Delete
              </Button>
            </div>
          </div>
        </div>
      )}
    </Draggable>
  );
}

// eslint-disable-next-line no-shadow
const DraggableOfferList = React.memo(function DraggableOfferList({ draggableOffers, match, updateBundleStatus, deleteEntity }) {
  return draggableOffers.map((draggableOffer, index) => (
    <DraggableOffer
      draggableOffer={draggableOffer}
      index={index}
      key={draggableOffer.id}
      match={match}
      updateBundleStatus={updateBundleStatus}
      deleteEntity={deleteEntity}
    />
  ));
});

const BundlesList = ({
  getBundleRulesByShop,
  bundles,
  loading,
  totalItems,
  shopInfo,
  updateIndex,
  match,
  updateStatus,
  updateStatusSuccess,
  deleteEntity
}) => {
  const [state, setState] = useState({ draggableOffers: [] });
  const history = useHistory();
  const [searchValue, setSearchValue] = useState('');
  const [sortByType, setSortByType] = useState('name');
  const [activePage, setActivePage] = useState(1);
  const [totalRowData, setTotalRowData] = useState(0);
  const [hasData, setHasData] = useState(false);
  const [itemsPerPage, setItemsPerPage] = useState(100);
  const [sortByDir, setSortByDir] = useState('asc');
  const [isPageAccessible, setIsPageAccessible] = useState(true);
  const handleGridData = () => {
    let sortField = `${sortByType},${sortByDir}`;
    getBundleRulesByShop(activePage - 1, itemsPerPage, sortField);
  };
  useEffect(() => {
    handleGridData();
  }, [activePage, sortByType, sortByDir, updateStatusSuccess]);

  useEffect(() => {
    setTotalRowData(totalItems);
    if (totalItems > 0) {
      setHasData(true);
    }
  }, [bundles]);

  const handlePagination = activePage => {
    setActivePage(activePage);
  };

  useEffect(() => {
    if (bundles.length > 0) {
      setState({
        draggableOffers: bundles.map(k => {
          const custom = {
            id: `${k.id}`,
            title: k.title,
            name: k.name,
            status: k.status,
            sequenceNo: k.sequenceNo,
            description: k.description
          };
          return custom;
        })
      });
    }
  }, [bundles]);

  function onDragEnd(result) {
    if (!result.destination) {
      return;
    }

    if (result.destination.index === result.source.index) {
      return;
    }
    const destinationIndex = state.draggableOffers[result.destination.index]['sequenceNo'];
    const sourceIndex = state.draggableOffers[result.source.index]['sequenceNo'];
    const draggableOffers = reorder(state.draggableOffers, result.source.index, result.destination.index);

    setState({ draggableOffers });
    updateIndex(draggableOffers[result.destination.index]['id'], sourceIndex, destinationIndex);
  }

  const updateBundleStatus = (id, value) => {
    updateStatus(id, value);
  };

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
          heading="Bundles"
          subheading=""
          icon="pe-7s-users icon-gradient"
          enablePageTitleAction={isPageAccessible}
          actionTitle="Create Bundle"
          onActionClick={() => {
            history.push('/dashboards/bundles/add');
          }}
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
                url: "https://intercom.help/appstle/en/articles/6756487-how-to-setup-and-configure-bundling-with-appstle-subscriptions",
              },
              {
                title: "How to Offer Bundling As a One-Time Purchase",
                url: "https://intercom.help/appstle/en/articles/8047972-how-to-offer-bundling-as-a-one-time-purchase",
              },
            ],
          }}
        />
        <FeatureAccessCheck setIsPageAccessible={setIsPageAccessible} hasAnyAuthorities={'accessBundling'} upgradeButtonText="Upgrade your plan" >
        {loading ? (
          <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
            <Loader type="line-scale" />
          </div>
        ) : bundles && bundles?.length > 0 ? (
          <Fragment>
            <Card className="main-card">
              <CardBody>
                <div>
                  {bundles && bundles.length > 0 ? (
                    // <Table responsive className="text-nowrap table-lg mb-0" hover>
                    <div className="bg-white offerbox-outer">
                      <DragDropContext onDragEnd={onDragEnd}>
                        <Droppable droppableId="list">
                          {provided => (
                            <div ref={provided.innerRef} {...provided.droppableProps}>
                              <DraggableOfferList
                                draggableOffers={state.draggableOffers}
                                match={match}
                                updateBundleStatus={updateBundleStatus}
                                deleteEntity={deleteEntity}
                              />
                              {provided.placeholder}
                            </div>
                          )}
                        </Droppable>
                      </DragDropContext>
                    </div>
                  ) : loading ? (
                    <>Loading...</>
                  ) : (
                    <div>
                      <Row className="align-items-center">
                        <Col>
                          <div className="text-center">
                            <h3>Create your first offer !!</h3>
                            <br />
                            <Link to="/dashboards/offer/new" className="btn btn-primary text-nowrap" color="primary">
                              Create new offer
                            </Link>
                          </div>
                        </Col>
                      </Row>
                    </div>
                  )}
                </div>
              </CardBody>
            </Card>
          </Fragment>
        ) : (
          <Row className="align-items-center welcome-page">
            <Col sm="5">
              <div>
                <h4>Welcome to Bundles Page</h4>
                <p className="text-muted">This page will give you the complete list of all the bundles.</p>

                <Button
                  color={'primary'}
                  tag={Link}
                  to={{
                    pathname: `/dashboards/bundles/add`
                  }}
                  icon="pencil-alt"
                >
                  <FontAwesomeIcon icon={faPlus} />
                  &nbsp;Create Bundle
                </Button>
              </div>
            </Col>
          </Row>
        )}
        </FeatureAccessCheck>
         <HelpPopUp>
            <div className="mt-4 border-bottom pb-4">
              <h6>Bundling</h6>
              <YoutubeVideoPlayer 
                url="https://www.youtube.com/watch?v=63BM5YIJh70"
                iframeHeight="100%"
                divClassName="video-container"
                iframeClassName="responsive-iframe"
              />              
            </div>
          </HelpPopUp>
      </ReactCSSTransitionGroup>
    </Fragment>
  );
};

const mapStateToProps = state => ({
  bundles: state.bundleRule.entities,
  totalItems: state.bundleRule.totalItems,
  loading: state.bundleRule.loading,
  shopInfo: state.shopInfo.entity,
  updateStatusSuccess: state.bundleRule.updateStatusSuccess
});

const mapDispatchToProps = {
  getBundleRulesByShop,
  updateIndex,
  updateStatus,
  deleteEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(BundlesList);
