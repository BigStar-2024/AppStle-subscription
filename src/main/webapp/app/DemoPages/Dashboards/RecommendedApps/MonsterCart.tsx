import React from 'react';
import { CardBody } from 'reactstrap';

const MonsterCart = () => (
  <a href="https://apps.shopify.com/monster-upsells?utm_source=subscriptions-by-appstle&utm_campaign=newsletter" target="_blank">
    <div style={{ height: '90px', border: ' 1px solid rgb(223, 223, 223)', overflow: 'hidden' }} className="rounded d-flex">
      <div style={{ width: '90px', height: '90px' }}>
        <img
          className=""
          style={{ width: '100%', height: '100%' }}
          src="https://cdn.shopify.com/app-store/listing_images/b0d1f54b1219bacc5e80ab7ce7c7dba5/icon/CLbN_oefsIADEAE=.png"
        />
      </div>
      <CardBody style={{ padding: '0.9rem' }}>
        <div>
          <p style={{ lineHeight: '1.333' }}>Monster Cart Upsell+Free Gifts</p>
        </div>
      </CardBody>
    </div>
  </a>
);

export default MonsterCart;
