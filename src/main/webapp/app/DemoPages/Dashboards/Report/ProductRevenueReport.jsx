import React from 'react';
import Loader from 'react-loaders';
import { Card, CardBody, CardTitle, Table } from 'reactstrap';

const ProductRevenueReport = ({
  getSuccessPastOrderEntity,
  getEntitiesWithoutPagination,
  productRevenueAnalyticsListData,
  productRevenueLoading,
  loading,
  shopInfo,
  subscriptionEntities,
  ...props
}) => {
  return (
    <>
      {productRevenueLoading && productRevenueAnalyticsListData?.length > 0 ? (
        <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
          <Loader type="line-scale" />
        </div>
      ) : productRevenueAnalyticsListData && productRevenueAnalyticsListData?.length > 0 ? (
        <>
          <Card>
            <CardBody>
              <CardTitle>Product Wise Revenue, Cancellation, Approval and Churn Rate (For Recurring Orders)</CardTitle>
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
                              {props?.totalSubscribedAmount && props?.totalSubscribedAmount[0]}
                              {item?.totalProductPrice?.toLocaleString()}
                            </div>{' '}
                          </td>
                          <td>{item?.cancellationRate}%</td>
                          <td>{item?.approvalRate}%</td>
                          <td>{item?.churnRate}%</td>
                        </tr>
                      );
                    })}
                  </tbody>
                </Table>
                {productRevenueAnalyticsListData?.length == 0 && <div className="text-center m-3">No data available</div>}
              </div>
            </CardBody>
          </Card>
        </>
      ) : (
        <></>
      )}
    </>
  );
};

export default ProductRevenueReport;
