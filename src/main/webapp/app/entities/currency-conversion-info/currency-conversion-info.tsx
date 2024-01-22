import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './currency-conversion-info.reducer';
import { ICurrencyConversionInfo } from 'app/shared/model/currency-conversion-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICurrencyConversionInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const CurrencyConversionInfo = (props: ICurrencyConversionInfoProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { currencyConversionInfoList, match } = props;
  return (
    <div>
      <h2 id="currency-conversion-info-heading">
        Currency Conversion Infos
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Currency Conversion Info
        </Link>
      </h2>
      <div className="table-responsive">
        {currencyConversionInfoList && currencyConversionInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>From</th>
                <th>To</th>
                <th>Stored On</th>
                <th>Currency Rate</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {currencyConversionInfoList.map((currencyConversionInfo, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${currencyConversionInfo.id}`} color="link" size="sm">
                      {currencyConversionInfo.id}
                    </Button>
                  </td>
                  <td>{currencyConversionInfo.from}</td>
                  <td>{currencyConversionInfo.to}</td>
                  <td>
                    <TextFormat type="date" value={currencyConversionInfo.storedOn} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{currencyConversionInfo.currencyRate}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${currencyConversionInfo.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${currencyConversionInfo.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${currencyConversionInfo.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">No Currency Conversion Infos found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ currencyConversionInfo }: IRootState) => ({
  currencyConversionInfoList: currencyConversionInfo.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CurrencyConversionInfo);
