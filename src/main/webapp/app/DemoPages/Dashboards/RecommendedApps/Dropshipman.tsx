import React from 'react';
import { CardBody } from 'reactstrap';

const Dropshipman = () => (
  <a href="https://apps.shopify.com/aliexpress-dropshipping-master?utm_source=subscriptions-by-appstle&utm_campaign=newsletter" target="_blank">
    <div style={{ height: '90px', border: ' 1px solid rgb(223, 223, 223)', overflow: 'hidden' }} className="rounded d-flex">
      <div style={{ width: '90px', height: '90px' }}>
        <img
          className=""
          style={{ width: '100%', height: '100%' }}
          src="https://cdn.shopify.com/app-store/listing_images/1252d9d2612d6b071896af7336148d30/icon/CPmDoZmM1P8CEAE=.png"
        />
      </div>
      <CardBody style={{ padding: '0.9rem' }}>
        <div>
          <p style={{ lineHeight: '1.333' }}>Dropshipman: Dropshipping & POD</p>
        </div>
      </CardBody>
    </div>
  </a>
);

export default Dropshipman;
