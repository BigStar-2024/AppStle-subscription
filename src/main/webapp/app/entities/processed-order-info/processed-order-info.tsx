import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './processed-order-info.reducer';
import { IProcessedOrderInfo } from 'app/shared/model/processed-order-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProcessedOrderInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ProcessedOrderInfo = (props: IProcessedOrderInfoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { processedOrderInfoList, match, loading } = props;
  return (
    <div>
      <h2 id="processed-order-info-heading">
        Processed Order Infos
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Processed Order Info
        </Link>
      </h2>
      <div className="table-responsive">
        {processedOrderInfoList && processedOrderInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Order Id</th>
                <th>Order Number</th>
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
              {processedOrderInfoList.map((processedOrderInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${processedOrderInfo.id}`} color="link" size="sm">
                      {processedOrderInfo.id}
                    </Button>
                  </td>
                  <td>{processedOrderInfo.shop}</td>
                  <td>{processedOrderInfo.orderId}</td>
                  <td>{processedOrderInfo.orderNumber}</td>
                  <td>{processedOrderInfo.topicName}</td>
                  <td>
                    {processedOrderInfo.processedTime ? (
                      <TextFormat type="date" value={processedOrderInfo.processedTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{processedOrderInfo.triggerRuleInfoJson}</td>
                  <td>{processedOrderInfo.attachedTags}</td>
                  <td>{processedOrderInfo.attachedTagsToNote}</td>
                  <td>{processedOrderInfo.removedTags}</td>
                  <td>{processedOrderInfo.removedTagsAfter}</td>
                  <td>{processedOrderInfo.removedTagsBefore}</td>
                  <td>{processedOrderInfo.firstName}</td>
                  <td>{processedOrderInfo.lastName}</td>
                  <td>{processedOrderInfo.status}</td>
                  <td>{processedOrderInfo.delayedTaggingValue}</td>
                  <td>{processedOrderInfo.delayedTaggingUnit}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${processedOrderInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${processedOrderInfo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${processedOrderInfo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Processed Order Infos found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ processedOrderInfo }: IRootState) => ({
  processedOrderInfoList: processedOrderInfo.entities,
  loading: processedOrderInfo.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProcessedOrderInfo);
