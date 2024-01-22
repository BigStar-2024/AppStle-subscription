import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './currency-conversion-info.reducer';
import { ICurrencyConversionInfo } from 'app/shared/model/currency-conversion-info.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICurrencyConversionInfoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CurrencyConversionInfoDetail = (props: ICurrencyConversionInfoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { currencyConversionInfoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          CurrencyConversionInfo [<b>{currencyConversionInfoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="from">From</span>
          </dt>
          <dd>{currencyConversionInfoEntity.from}</dd>
          <dt>
            <span id="to">To</span>
          </dt>
          <dd>{currencyConversionInfoEntity.to}</dd>
          <dt>
            <span id="storedOn">Stored On</span>
          </dt>
          <dd>
            <TextFormat value={currencyConversionInfoEntity.storedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="currencyRate">Currency Rate</span>
          </dt>
          <dd>{currencyConversionInfoEntity.currencyRate}</dd>
        </dl>
        <Button tag={Link} to="/currency-conversion-info" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/currency-conversion-info/${currencyConversionInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ currencyConversionInfo }: IRootState) => ({
  currencyConversionInfoEntity: currencyConversionInfo.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CurrencyConversionInfoDetail);
