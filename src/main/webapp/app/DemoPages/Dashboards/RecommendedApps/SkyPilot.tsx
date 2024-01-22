import React from 'react';
import { CardBody } from 'reactstrap';

function SkyPilot() {
  return (
    <a href="https://apps.shopify.com/sky-pilot?utm_source=subscriptions-by-appstle&utm_campaign=newsletter" target="_blank">
      <div style={{ height: '90px', border: ' 1px solid rgb(223, 223, 223)', overflow: 'hidden' }} className="rounded d-flex">
        <div style={{ width: '90px', height: '90px' }}>
          <img
            className=""
            style={{ width: '100%', height: '100%' }}
            src="https://cdn.shopify.com/app-store/listing_images/43736e019bc30181bffc39c28399ed98/icon/CKKnmp6v-vcCEAE=.png"
          />
        </div>
        <CardBody style={{ padding: '0.9rem' }}>
          <div>
            <p style={{ lineHeight: '1.333' }}>Sky Pilot</p>
          </div>
        </CardBody>
      </div>
    </a>
  );
}

export default SkyPilot;
