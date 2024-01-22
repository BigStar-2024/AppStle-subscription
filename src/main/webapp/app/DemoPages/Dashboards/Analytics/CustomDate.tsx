// @ts-expect-error ts(5097) An import path can only end with a '.tsx' extension when 'allowImportingTsExtensions' is enabled.
import { AnalyticsContext, options } from 'app/DemoPages/Dashboards/Analytics/Analytics.tsx';
import DatePickerComponent from 'app/DemoPages/Components/DatePicker/DatePicker';
import momentTZ from 'moment-timezone';
import React, { useContext } from 'react';
import Select from 'react-select';
import { CardTitle, Col, Row } from 'reactstrap';

export default function CustomDateSelector() {
  const {
    customDatePickerToggle,
    fromDay,
    toDay,
    selectedDays,
    toDate,
    fromDate,
    handleFromDays,
    handleToDays,
    handleChangeStatisticsDays,
    selectCustomStyles,
    shopInfo,
  } = useContext(AnalyticsContext);

  return (
    <>
      <Row>
        <Col lg={12} sm={12}>
          <CardTitle>Date Range</CardTitle>
          <div className="btn-actions-pane-right text-capitalize">
            <div className="d-flex align-items-center">
              <div style={{ width: '230px', paddingRight: '1rem' }}>
                <span className="d-inline-block ml-2 mb-3" style={{ width: '100%' }}>
                  <Select
                    options={options}
                    value={
                      customDatePickerToggle
                        ? { value: 'CUSTOM_DATE', label: 'Custom Date' }
                        : options.filter(option => option.value === selectedDays)
                    }
                    onChange={handleChangeStatisticsDays}
                    styles={selectCustomStyles}
                  />
                </span>
              </div>
              <div>
                {customDatePickerToggle && (
                  <span className="d-inline-block mb-3">
                    <DatePickerComponent selectedDate={fromDay} onSelectDay={handleFromDays} />
                  </span>
                )}
              </div>
              <div>
                {customDatePickerToggle && (
                  <span className="d-inline-block ml-2 mb-3">
                    <DatePickerComponent selectedDate={toDay} onSelectDay={handleToDays} />
                  </span>
                )}
              </div>
            </div>
          </div>

          <Row>
            <Col lg={6} sm={6}>
              {customDatePickerToggle && (
                <div>
                  <b>From </b>
                  {fromDate && (
                    <div style={{ fontSize: '1.0rem', flex: 1, display: 'inline-flex' }}>
                      {momentTZ(fromDate).tz(shopInfo.shopTimeZone.substring(12)).format('MMMM DD, YYYY hh:mm A')} {'('}
                      {shopInfo.shopTimeZone.substring(12)}
                      {')'}
                    </div>
                  )}
                </div>
              )}
            </Col>
            <Col lg={6} sm={6}>
              {customDatePickerToggle && (
                <div>
                  <b>To </b>
                  {toDate && (
                    <div style={{ fontSize: '1.0rem', flex: 1, display: 'inline-flex' }}>
                      {momentTZ(toDate).tz(shopInfo.shopTimeZone.substring(12)).format('MMMM DD, YYYY hh:mm A')} {'('}
                      {shopInfo.shopTimeZone.substring(12)}
                      {')'}
                    </div>
                  )}
                </div>
              )}
            </Col>
          </Row>
        </Col>
      </Row>
      <hr />
    </>
  );
}
