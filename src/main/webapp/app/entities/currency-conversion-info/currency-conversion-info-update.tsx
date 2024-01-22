import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './currency-conversion-info.reducer';
import { ICurrencyConversionInfo } from 'app/shared/model/currency-conversion-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICurrencyConversionInfoUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CurrencyConversionInfoUpdate = (props: ICurrencyConversionInfoUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { currencyConversionInfoEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/currency-conversion-info');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...currencyConversionInfoEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="subscriptionApp.currencyConversionInfo.home.createOrEditLabel">Create or edit a CurrencyConversionInfo</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : currencyConversionInfoEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="currency-conversion-info-id">ID</Label>
                  <AvInput id="currency-conversion-info-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fromLabel" for="currency-conversion-info-from">
                  From
                </Label>
                <AvField
                  id="currency-conversion-info-from"
                  type="text"
                  name="from"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="toLabel" for="currency-conversion-info-to">
                  To
                </Label>
                <AvField
                  id="currency-conversion-info-to"
                  type="text"
                  name="to"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="storedOnLabel" for="currency-conversion-info-storedOn">
                  Stored On
                </Label>
                <AvField id="currency-conversion-info-storedOn" type="date" className="form-control" name="storedOn" />
              </AvGroup>
              <AvGroup>
                <Label id="currencyRateLabel" for="currency-conversion-info-currencyRate">
                  Currency Rate
                </Label>
                <AvField id="currency-conversion-info-currencyRate" type="string" className="form-control" name="currencyRate" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/currency-conversion-info" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  currencyConversionInfoEntity: storeState.currencyConversionInfo.entity,
  loading: storeState.currencyConversionInfo.loading,
  updating: storeState.currencyConversionInfo.updating,
  updateSuccess: storeState.currencyConversionInfo.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CurrencyConversionInfoUpdate);
