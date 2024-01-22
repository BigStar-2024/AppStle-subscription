import PageTitle from 'app/Layout/AppMain/PageTitle';
import React, {Component, Fragment} from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {IntercomAPI} from 'react-intercom';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import FeatureAccessCheck from "app/shared/auth/featureAccessCheck";
import {
  Card,
  CardBody,
  CardTitle,
  Col,
  Container,
  Input,
  FormGroup,
  Row,
  Table,
  Modal,
  Button,
  ModalBody,
  ModalFooter,
  ListGroup,
  ListGroupItem,
  Progress,
  ModalHeader,
  Alert
} from 'reactstrap';
import './Migration.scss'
import {FaCheckCircle,FaCaretRight} from 'react-icons/fa'
import {BsChatDotsFill} from 'react-icons/bs'
import {GrMail} from 'react-icons/gr'
import {AiOutlineMail,AiOutlineMessage} from 'react-icons/ai'

export class WidgetPlacement extends Component {
   constructor(props) {
    super(props);
    this.state = {
      cardActive: 'radio-card-1',
    };
  }
  render() {
    return (
      <Fragment>
      <ReactCSSTransitionGroup
        component="div"
        transitionName="TabsAnimation"
        transitionAppear
        transitionAppearTimeout={0}
        transitionEnter={false}
        transitionLeave={false}
      >
        <PageTitle
          heading="Subscription Migration"
          subheading="Import bulk subscription contracts."
          icon="lnr-pencil icon-gradient bg-mean-fruit"
        />

        <div className="custom-migration">
          {/* <p>
            We would love to migrate your subscriptions from another app or platform. Please message us{' '}
            <Link
              onClick={() => {
                IntercomAPI('showNewMessage', "I would like to import my subscriptions from another app or platform. Can you please help?");
              }}
            >
              here
            </Link>{', '}
          </p> */}
{/*           
          <div className="container"> */}
        {/* <div className="grid-wrapper grid-col-auto"> */}
        <Row>

        <Col lg={6} sm={6}> 
        <label for="radio-card-1" className="radio-card" onClick={(e) => {this.setState({...this.state, cardActive : e.target.id})  }}>
            <input type="radio" name="radio-card" id="radio-card-1"  checked={this.state.cardActive==='radio-card-1'}  />
            <div className="card-content-wrapper">
              <div className='custom-icon d-flex justify-content-end'>

              <span className="check-icon"></span>
              </div>
              <div className="card-content d-flex ">
                 <div className='custom-card-image'>
                <img
                  src="https://image.freepik.com/free-vector/group-friends-giving-high-five_23-2148363170.jpg"
                  alt=""
                  />
                  </div>
              <div className='custom-heading-description'>
                <div className='d-flex '>
                <h3 className='mt-2'>Migration V1</h3>
                </div>
                <p className='mt-2 pr-3'>The subscription APIs that are using their own checkout and not Shopify’s native checkout are actually 
                fall under the V1 category of the migration like (ReCharge V1 or Bold V1), 
                and if you are migrating your store from some other platforms like Woocomerce or WIX and also want to migrate the subscribers to Shopify,
                 the migration type is V1.</p>
                 <p className='heading mt-3 mb-2' >Supporting following gateways:</p>
                  <div className='tags-v1 d-flex'>
                <div className='tag'><span>Stripe</span></div>
                <div className='tag ml-3'><span>Paypal</span></div>
                <div className='tag ml-3'><span>Authorize.net</span></div>
                <div className='tag ml-3'><span>Braintree</span></div>
                </div>
                 
              </div>
              </div>
            </div>
          </label></Col>
        <Col lg={6} sm={6}><label for="radio-card-2" className="radio-card" onClick={(e) => {this.setState({...this.state, cardActive : e.target.id})}} >
            <input type="radio" name="radio-card" id="radio-card-2" checked={this.state.cardActive==='radio-card-2'} />
            <div className="card-content-wrapper">
              <div className='custom-icon d-flex justify-content-end'>
              <span className="check-icon"></span>
              </div>
              <div className="card-content d-flex">
                <div className='custom-card-image'>
                <img
                  src="https://image.freepik.com/free-vector/people-putting-puzzle-pieces-together_52683-28610.jpg"
                  alt=""
                />
                </div>
              <div className='custom-heading-description'>
                <div className='d-flex '>
                <h3 className='mt-2'>Migration V2</h3>
                </div>
                <p className='mt-2 pr-3'>The subscription APIs that are using Shopify’s native checkout are actually fall under the 
                V2 category of the migration like (Appstle, Bold V2.</p>
                <p className='heading mt-3 mb-2' >Supporting following gateways:</p>
                <div className='tags-v2 d-flex'>
                <div className='tag'><span>Stripe</span></div>
                <div className='tag ml-3'><span>Paypal/Paypal Express</span></div>
                <div className='tag ml-3'><span>Authorize.net</span></div>
                <div className='tag ml-3'><span>Braintree</span></div>
                </div>
              </div>
              </div>
            </div>
          </label></Col>
         

          
          </Row>
        {/* </div>
      </div> */}
      <div className='custom-card'>

      
        <Card className="mt-4">
              <CardBody>
                <div className="d-flex custom-video-data" style={{ justifyContent: 'space-between' }}>
                  <div className='text-justify custom-list'>
                    {this.state.cardActive ==='radio-card-1' ? 
                    <span className='custom-thanks '>Steps for the V1 Migration are as follows:</span> 
                    : <span className='custom-thanks '>Steps for the V2 Migration are as follows:</span>  } 
                    {this.state.cardActive ==='radio-card-1' ? <>
                    <div  className=' d-flex custom-space mt-4'>
                      <div className='custom-svg'>
                         1.
                      </div>
                      <div className='custom-text '>
                        <p>We need the customer data (from the relevant payment gateway and the subscription data.</p>
                      </div>
                    </div>
                    <div  className=' d-flex custom-space'>
                      <div className='custom-svg'>
                        2.  
                      </div>
                      <div className='custom-text'>
                        <p>After you provide us with the data, we will run the validations on our end and let you know if there is something missing.</p>
                      </div>
                    </div>
                    <div  className=' d-flex custom-space'>
                      <div className='custom-svg'>
                          3.
                      </div>
                      <div className='custom-text'>
                        <p>Once we get the final updated data, we will run the migration and keep them in Pause state so that they don't get double charged from Appstle and your existing subscription API.</ p>
                      </div>
                    </div>
                    <div  className=' d-flex custom-space'>
                      <div className='custom-svg'>
                         4.
                      </div>
                      <div className='custom-text'>
                        <p>After the final import you can see the migrated subscribers in Appstle and can verify them. Once you see if everything is good you can activate them and disable the other app.</p>
                      </div>
                    </div>
                    <div  className=' d-flex custom-space'>
                      {/* <div className='custom-svg'>
                         5.
                      </div> */}
                      <div className='custom-text'> 
                        <p>To know more about the standard data format to migrate your subscribers, please feel free to reach out to us anytime, we are available 24*7 <a style={{color:'#3057d5'}} onClick={() => {
                    IntercomAPI('showNewMessage', "I would like to enable widget on Collection and HomePage. Can you please help?");
                }}>here.</a></p>
                      </div>
                    </div>
                    </> : <> 
                    <div  className=' d-flex custom-space mt-4'>
                      <div className='custom-svg'>
                        1.
                      </div>
                      <div className='custom-text'>
                        <p>We only need the subscription data from your existing API for subscription.</p>
                      </div>
                    </div>
                    <div  className=' d-flex custom-space'>
                      <div className='custom-svg'>
                        2.
                      </div>
                      <div className='custom-text'>
                        <p>After you provide us with the data, we will run the validations on our end and let you know if there is something missing.</p>
                      </div>
                    </div>
                    <div  className=' d-flex custom-space'>
                      <div className='custom-svg'>
                        3.
                      </div>
                      <div className='custom-text'>
                        <p>Once we get the final updated data, we will run the migration and keep them in Pause state so that they don't get double charged from Appstle and your existing subscription API.</p>
                      </div>
                    </div>
                    <div  className=' d-flex custom-space'>
                      <div className='custom-svg'>
                        4.
                      </div>
                      <div className='custom-text'>
                        <p> After the final import you can see the migrated subscribers in Appstle and can verify them. Once you see if everything is good you can activate them and disable the other app.</p>
                      </div>
                    </div>
                    <div  className=' d-flex custom-space'>
                      {/* <div className='custom-svg'>
                         2.
                      </div> */}
                      <div className='custom-text'>
                        <p>﻿To know more about the standard data format to migrate your subscribers, please feel free to reach out to us anytime, we are available 24*7 <a style={{color:'#3057d5'}} onClick={() => {
                    IntercomAPI('showNewMessage', "I would like to enable widget on Collection and HomePage. Can you please help?");
                }}>here</a>
                          </p>
                      </div>
                    </div></>}
                    {/* // shopify */}
                   
                  </div>
                 
                </div>
              </CardBody>
            </Card>
            </div>
        </div>

      </ReactCSSTransitionGroup>
    </Fragment>
    );
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WidgetPlacement);
