import React from 'react';
import {Card, CardBody, CardSubtitle, CardTitle,CardFooter} from 'reactstrap';

function TxtCart() {
    return   (<a href="https://apps.shopify.com/txtcart-plus?utm_source=appstle&utm_campaign=partnership&utm_medium=in-app-recommendation" target="_blank">
    <div style={{height: "90px", border:" 1px solid rgb(223, 223, 223)", overflow: "hidden"}} className="rounded d-flex">
    <div  style={{width: '90px', height: "90px"}}>
            <img className="" style={{width: '100%', height: "100%"}} src={require('./txtCartPlus.png')} />
    </div>
    <CardBody style={{padding: "0.9rem"}}>
        <div>
        <p style={{lineHeight: "1.333"}}>TxtCart Plus + SMS Marketing</p>
        </div>
    </CardBody>
  </div>
  </a>)
}

export default TxtCart