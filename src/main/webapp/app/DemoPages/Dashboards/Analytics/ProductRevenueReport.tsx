// @ts-expect-error ts(5097) An import path can only end with a '.tsx' extension when 'allowImportingTsExtensions' is enabled.
import { AnalyticsContext } from 'app/DemoPages/Dashboards/Analytics/Analytics.tsx';
import React, { useContext } from 'react';
import Loader from 'react-loaders';
import { Card, CardBody, CardTitle, Table } from 'reactstrap';
import DateRangeText from 'app/DemoPages/Dashboards/Analytics/DateRangeText';

function ProductRevenueReport({ productRevenueAnalyticsListData, productRevenueLoading }) {
  const { totalSubscribedAmount } = useContext(AnalyticsContext);

  return (
    <Card>
      <CardBody>
        <CardTitle>
          Product Wise Revenue, Cancellation, Approval and Churn Rate (For Recurring Orders)
          <DateRangeText />
        </CardTitle>
        <div>
          <Table className="mb-0 mt-4" hover>
            <thead>
              <tr>
                <th>Product Name</th>
                <th>No of quantity (Approx)</th>
                <th>Total Revenue (Approx)</th>
                <th>Product Cancellation Rate</th>
                <th>Product Approval Rate</th>
                <th>Product Churn Rate</th>
              </tr>
            </thead>
            <tbody>
              {productRevenueLoading ? (
                <tr className="text-center">
                  <td className="w-100" colSpan={6}>
                    <Loader type="line-scale" active />
                  </td>
                </tr>
              ) : (
                <>
                  {productRevenueAnalyticsListData?.map(item => {
                    return (
                      <tr key={item?.variantId}>
                        <td>
                          {item?.variantImage && <img src={`${item?.variantImage}`} width="50"></img>}&nbsp; {item?.title}{' '}
                          {item?.variantTitle != '-' && item?.variantTitle}
                        </td>
                        <td>{item?.totalProductQuantity}</td>
                        <td>
                          <div className="widget-numbers text-success" style={{ fontSize: '1.0rem' }}>
                            {totalSubscribedAmount && totalSubscribedAmount[0]}
                            {item?.totalProductPrice?.toLocaleString()}
                          </div>{' '}
                        </td>
                        <td>{item?.cancellationRate}%</td>
                        <td>{item?.approvalRate}%</td>
                        <td>{item?.churnRate}%</td>
                      </tr>
                    );
                  })}
                </>
              )}
            </tbody>
          </Table>
          {!productRevenueLoading && productRevenueAnalyticsListData?.length == 0 && (
            <div className="text-center m-3">No data available</div>
          )}
        </div>
      </CardBody>
    </Card>
  );
}

export default ProductRevenueReport;
