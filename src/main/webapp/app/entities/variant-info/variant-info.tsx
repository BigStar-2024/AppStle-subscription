import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './variant-info.reducer';
import { IVariantInfo } from 'app/shared/model/variant-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVariantInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const VariantInfo = (props: IVariantInfoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { variantInfoList, match } = props;
  return (
    <div>
      <h2 id="variant-info-heading">
        Variant Infos
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Variant Info
        </Link>
      </h2>
      <div className="table-responsive">
        {variantInfoList && variantInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Shop</th>
                <th>Product Id</th>
                <th>Variant Id</th>
                <th>Product Title</th>
                <th>Variant Title</th>
                <th>Sku</th>
                <th>Variant Price</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {variantInfoList.map((variantInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${variantInfo.id}`} color="link" size="sm">
                      {variantInfo.id}
                    </Button>
                  </td>
                  <td>{variantInfo.shop}</td>
                  <td>{variantInfo.productId}</td>
                  <td>{variantInfo.variantId}</td>
                  <td>{variantInfo.productTitle}</td>
                  <td>{variantInfo.variantTitle}</td>
                  <td>{variantInfo.sku}</td>
                  <td>{variantInfo.variantPrice}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${variantInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${variantInfo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${variantInfo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Variant Infos found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ variantInfo }: IRootState) => ({
  variantInfoList: variantInfo.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VariantInfo);
