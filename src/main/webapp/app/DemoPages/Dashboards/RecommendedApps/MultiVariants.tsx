import React from 'react';
import { CardBody } from 'reactstrap';

function MultiVariants() {
  return (
    <a href="https://apps.shopify.com/multivariants?utm_source=subscriptions-by-appstle&utm_campaign=newsletter" target="_blank">
      <div style={{ height: '90px', border: ' 1px solid rgb(223, 223, 223)', overflow: 'hidden' }} className="rounded d-flex">
        <div style={{ width: '90px', height: '90px' }}>
          <img
            className=""
            style={{ width: '100%', height: '100%' }}
            src="https://cdn.shopify.com/app-store/listing_images/8bd94c23dd747d81fc3d843cfbd14e01/icon/CInQwsL0lu8CEAE=.png"
          />
        </div>
        <CardBody style={{ padding: '0.9rem' }}>
          <div>
            <p style={{ lineHeight: '1.333' }}>MultiVariants - Bulk Order</p>
          </div>
        </CardBody>
      </div>
    </a>
  );
}

export default MultiVariants;
