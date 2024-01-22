import React from 'react';
import {Card, CardBody, CardSubtitle, CardTitle,CardFooter} from 'reactstrap';

function Froonze() {
    return   (
      <a href="https://apps.shopify.com/customer-accounts?utm_source=subscriptions-by-appstle&utm_campaign=newsletter" target="_blank">
        <div style={{height: "90px", border:" 1px solid rgb(223, 223, 223)", overflow: "hidden"}} className="rounded d-flex">
          <div  style={{width: '90px', height: "90px"}}>
            <img className="" style={{width: '100%', height: "100%"}} src="https://cdn.shopify.com/app-store/listing_images/39667e1b914fcca4e73731f12966de2f/icon/CN_T2salmfQCEAE=.png" />
          </div>
          <CardBody style={{padding: "0.9rem"}}>
            <div>
              <p style={{lineHeight: "1.333"}}>Froonze</p>
            </div>
          </CardBody>
        </div>
      </a>
    )
}

export default Froonze