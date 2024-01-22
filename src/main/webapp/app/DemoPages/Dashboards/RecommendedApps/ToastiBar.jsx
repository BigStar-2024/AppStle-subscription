import React from 'react';
import {Card, CardBody, CardSubtitle, CardTitle,CardFooter} from 'reactstrap';

function ToastiBar() {
return    <a href="https://apps.shopify.com/mps-sales-notification?utm_source=appstle&utm_campaign=partnership&utm_medium=in-app-recommendation" target="_blank">
<div style={{height: "90px", border:" 1px solid rgb(223, 223, 223)", overflow: "hidden"}} className="rounded d-flex">
<div  style={{width: '90px', height: "90px"}}>
            <img className="" style={{width: '100%', height: "100%"}}src="https://cdn.shopify.com/app-store/listing_images/4c58eca6a3e208fe6f5d1b495d26295b/icon/CObC-Kf0lu8CEAE=.png" />
    </div>
    <CardBody style={{padding: "0.9rem"}}>
        <div>
        <p style={{lineHeight: "1.333"}}>ToastiBar - Sales Popup App</p>
        </div>
    </CardBody>
  </div>
</a>
}

export default ToastiBar