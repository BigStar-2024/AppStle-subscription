import React, { useState } from 'react';
import { Card, CardBody, Container, Button, ButtonGroup, Row, Input, Label, Alert, InputGroup } from 'reactstrap';
import "./PlanTable.scss";

export default function DiscountCode(props) {
  const {
    isValidDiscountCode,
    setDiscountCopoun,
    discountCopoun,
    validatingDIscountCode,
    clearDiscountCode,
    planInfoDiscountEntity,
    validateAndApplyDiscount
  } = props;
  const [discountFormToggle, setDiscountFormToggle] = useState(false);

  return (
    <div className="discount-background m-auto text-center">
      <div>
        <Label>
          <b>DO YOU HAVE A DISCOUNT CODE?</b>
        </Label>
        <Button color="link" onClick={() => setDiscountFormToggle(!discountFormToggle)}>
          Click here
        </Button>
      </div>
      {discountFormToggle && (
        <div className="d-flex align-items-center justify-content-space-between m-auto" style={{width: '60%'}}>
          {
            <>
              <div className="w-100">
                <InputGroup>
                  <Input
                    disabled={isValidDiscountCode}
                    value={discountCopoun}
                    // className={(isValidDiscountCode === false && discountCopoun && props?.planInfoDiscountEntity?.valid === false) && "is-invalid"}
                    onInput={e => setDiscountCopoun(e.target.value)}
                    placeholder="Discount Code"
                  />
                  {!isValidDiscountCode ? (
                    <Button color="primary" disabled={!discountCopoun} onClick={validateAndApplyDiscount}>
                      {!validatingDIscountCode && 'Apply'}
                      {validatingDIscountCode && <div className="appstle_loadersmall" />}
                    </Button>
                  ) : (
                    <Button color="secondary" disabled={!discountCopoun} onClick={clearDiscountCode}>
                      clear
                    </Button>
                  )}
                </InputGroup>
              </div>

              {/* ) : (
            <div className="w-100">
              Discount code: &nbsp;
              <span className="applied-discount">
                <b>{discountCopoun}</b>
              </span>{' '}
              Applied.
              <Button color="link" onClick={clearDiscountCode}>
                clear
              </Button>
            </div>
          ) */}
            </>
          }
        </div>
      )}

      {discountCopoun && isValidDiscountCode === false && planInfoDiscountEntity?.valid === false && (
        <div className="mt-2">
          <Alert color="danger" className="w-50 m-auto">
            Invalid Discount Code.
          </Alert>
        </div>
      )}
    </div>
  );
}
