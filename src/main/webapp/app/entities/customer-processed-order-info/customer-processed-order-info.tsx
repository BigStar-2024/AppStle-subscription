import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './customer-processed-order-info.reducer';
import { ICustomerProcessedOrderInfo } from 'app/shared/model/customer-processed-order-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerProcessedOrderInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const CustomerProcessedOrderInfo = (props: ICustomerProcessedOrderInfoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { customerProcessedOrderInfoList, match, loading } = props;
  return (
    <div>
      <h2 id="customer-processed-order-info-heading">
        Customer Processed Order Infos
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Customer Processed Order Info
        </Link>
      </h2>
      <div className="table-responsive">
        {customerProcessedOrderInfoList && customerProcessedOrderInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Order Id</th>
                <th>Order Number</th>
                <th>Customer Number</th>
                <th>Topic Name</th>
                <th>Processed Time</th>
                <th>Trigger Rule Info Json</th>
                <th>Attached Tags</th>
                <th>Attached Tags To Note</th>
                <th>Removed Tags</th>
                <th>Removed Tags After</th>
                <th>Removed Tags Before</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Status</th>
                <th>Delayed Tagging Value</th>
                <th>Delayed Tagging Unit</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerProcessedOrderInfoList.map((customerProcessedOrderInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${customerProcessedOrderInfo.id}`} color="link" size="sm">
                      {customerProcessedOrderInfo.id}
                    </Button>
                  </td>
                  <td>{customerProcessedOrderInfo.shop}</td>
                  <td>{customerProcessedOrderInfo.orderId}</td>
                  <td>{customerProcessedOrderInfo.orderNumber}</td>
                  <td>{customerProcessedOrderInfo.customerNumber}</td>
                  <td>{customerProcessedOrderInfo.topicName}</td>
                  <td>
                    {customerProcessedOrderInfo.processedTime ? (
                      <TextFormat type="date" value={customerProcessedOrderInfo.processedTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{customerProcessedOrderInfo.triggerRuleInfoJson}</td>
                  <td>{customerProcessedOrderInfo.attachedTags}</td>
                  <td>{customerProcessedOrderInfo.attachedTagsToNote}</td>
                  <td>{customerProcessedOrderInfo.removedTags}</td>
                  <td>{customerProcessedOrderInfo.removedTagsAfter}</td>
                  <td>{customerProcessedOrderInfo.removedTagsBefore}</td>
                  <td>{customerProcessedOrderInfo.firstName}</td>
                  <td>{customerProcessedOrderInfo.lastName}</td>
                  <td>{customerProcessedOrderInfo.status}</td>
                  <td>{customerProcessedOrderInfo.delayedTaggingValue}</td>
                  <td>{customerProcessedOrderInfo.delayedTaggingUnit}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customerProcessedOrderInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerProcessedOrderInfo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customerProcessedOrderInfo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Customer Processed Order Infos found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ customerProcessedOrderInfo }: IRootState) => ({
  customerProcessedOrderInfoList: customerProcessedOrderInfo.entities,
  loading: customerProcessedOrderInfo.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CustomerProcessedOrderInfo);
