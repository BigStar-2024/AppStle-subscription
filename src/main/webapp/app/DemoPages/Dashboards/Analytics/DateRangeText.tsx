// @ts-expect-error ts(5097) An import path can only end with a '.tsx' extension when 'allowImportingTsExtensions' is enabled.
import { AnalyticsContext } from 'app/DemoPages/Dashboards/Analytics/Analytics.tsx';
import momentTZ from 'moment-timezone';
import React, { useContext } from 'react';

export default function DateRangeText() {
  const c = useContext(AnalyticsContext);

  return (
    <p style={{ fontSize: '0.8rem' }}>
      (from {momentTZ(c.fromDate).tz(c.shopInfo.shopTimeZone.substring(12)).format('MMMM DD, YYYY')} to{' '}
      {momentTZ(c.toDate).tz(c.shopInfo.shopTimeZone.substring(12)).format('MMMM DD, YYYY')})
    </p>
  );
}
