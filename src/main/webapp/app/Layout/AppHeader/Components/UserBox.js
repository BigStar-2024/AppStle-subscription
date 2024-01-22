import { faQuestionCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { Fragment,useState  } from 'react';
import { Intercom } from 'react-intercom';
import { Bounce, toast } from 'react-toastify';
import { Button , Modal,ModalHeader,ModalBody,ModalFooter,Label,FormGroup,Field,Input} from 'reactstrap';
import { NavLink } from 'react-router-dom';
import calendlyIcon from "./calendly.svg";
import Select from 'react-select';
import "./userBoxModal.scss"
import { AiOutlineMessage } from 'react-icons/ai';
import {IntercomAPI} from 'react-intercom';
import { connect } from 'react-redux';
import { Link } from "react-router-dom";
class UserBox extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      active: false,
      show: false,
      reason:'',
    };
  }

  notify2() {
    this.toastId = toast("You don't have any new items in your calendar for today! Go out and play!", {
      transition: Bounce,
      closeButton: true,
      autoClose: 500,
      position: 'bottom-center',
      type: 'success'
    });
  }

  isInsideShopify() {
     return ((window.location.href.includes("myshopify.com") || (window?.location?.ancestorOrigins?.[0] ? window?.location?.ancestorOrigins[0]?.includes("myshopify.com") : false)));
  }

  render() {
    return (
      <Fragment>
        <div className="header-btn-lg pr-0">
          <div className="widget-content p-0">
            <div className="widget-content-wrapper">
              {/* <div className="widget-content-left">
                                <UncontrolledButtonDropdown>
                                    <DropdownToggle color="link" className="p-0">
                                        <img width={42} className="rounded-circle" src={avatar1} alt=""/>
                                        <FontAwesomeIcon className="ml-2 opacity-8" icon={faAngleDown}/>
                                    </DropdownToggle>
                                    <DropdownMenu right className="rm-pointers dropdown-menu-lg">
                                        <div className="dropdown-menu-header">
                                            <div className="dropdown-menu-header-inner bg-info">
                                                <div className="menu-header-image opacity-2"
                                                     style={{
                                                         backgroundImage: 'url(' + city3 + ')'
                                                     }}
                                                />
                                                <div className="menu-header-content text-left">
                                                    <div className="widget-content p-0">
                                                        <div className="widget-content-wrapper">
                                                            <div className="widget-content-left mr-3">
                                                                <img width={42} className="rounded-circle" src={avatar1}
                                                                     alt=""/>
                                                            </div>
                                                            <div className="widget-content-left">
                                                                <div className="widget-heading">
                                                                    Alina Mcloughlin
                                                                </div>
                                                                <div className="widget-subheading opacity-8">
                                                                    A short profile description
                                                                </div>
                                                            </div>
                                                            <div className="widget-content-right mr-2">
                                                                <Button className="btn-pill btn-shadow btn-shine"
                                                                        color="focus">
                                                                    Logout
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="scroll-area-xs" style={{
                                            height: '150px'
                                        }}>
                                            <PerfectScrollbar>
                                                <Nav vertical>
                                                    <NavItem className="nav-item-header">
                                                        Activity
                                                    </NavItem>
                                                    <NavItem>
                                                        <NavLink href="javascript:void(0);">
                                                            Chat
                                                            <div className="ml-auto badge badge-pill badge-info">8</div>
                                                        </NavLink>
                                                    </NavItem>
                                                    <NavItem>
                                                        <NavLink href="javascript:void(0);">Recover Password</NavLink>
                                                    </NavItem>
                                                    <NavItem className="nav-item-header">
                                                        My Account
                                                    </NavItem>
                                                    <NavItem>
                                                        <NavLink href="javascript:void(0);">
                                                            Settings
                                                            <div className="ml-auto badge badge-success">New</div>
                                                        </NavLink>
                                                    </NavItem>
                                                    <NavItem>
                                                        <NavLink href="javascript:void(0);">
                                                            Messages
                                                            <div className="ml-auto badge badge-warning">512</div>
                                                        </NavLink>
                                                    </NavItem>
                                                    <NavItem>
                                                        <NavLink href="javascript:void(0);">
                                                            Logs
                                                        </NavLink>
                                                    </NavItem>
                                                </Nav>
                                            </PerfectScrollbar>
                                        </div>
                                        <Nav vertical>
                                            <NavItem className="nav-item-divider mb-0"/>
                                        </Nav>
                                        <div className="grid-menu grid-menu-2col">
                                            <Row className="no-gutters">
                                                <Col sm="6">
                                                    <Button
                                                        className="btn-icon-vertical btn-transition btn-transition-alt pt-2 pb-2"
                                                        outline color="warning">
                                                        <i className="pe-7s-chat icon-gradient bg-amy-crisp btn-icon-wrapper mb-2"> </i>
                                                        Message Inbox
                                                    </Button>
                                                </Col>
                                                <Col sm="6">
                                                    <Button
                                                        className="btn-icon-vertical btn-transition btn-transition-alt pt-2 pb-2"
                                                        outline color="danger">
                                                        <i className="pe-7s-ticket icon-gradient bg-love-kiss btn-icon-wrapper mb-2"> </i>
                                                        <b>Support Tickets</b>
                                                    </Button>
                                                </Col>
                                            </Row>
                                        </div>
                                        <Nav vertical>
                                            <NavItem className="nav-item-divider"/>
                                            <NavItem className="nav-item-btn text-center">
                                                <Button size="sm" className="btn-wide" color="primary">
                                                    Open Messages
                                                </Button>
                                            </NavItem>
                                        </Nav>
                                    </DropdownMenu>
                                </UncontrolledButtonDropdown>
                            </div> */}
                {/* {!this.isInsideShopify() &&
                    <div className="widget-content-left  ml-2 header-user-info shop-title">
                        <div className="widget-heading"> <span>  <i className="header-icon lnr-store icon-gradient bg-plum-plate" style={{fontSize:"24px"}}> </i>
                        <a href={"https://"+this.props.shop+"/admin"} style={{textDecoration:"none", color:"#495057"}} target={"_blank"}>{this.props.shop}</a></span></div>
                    </div>
                } */}
              <div className="widget-content-right header-user-info ml-3">
              <a href='https://calendly.com/joshua_iglesia/demo-followup-with-joshua-iglesia-appstle' target='_blank' style={{color: '#fff', marginRight: '14px'}}>
              <Button
                  className="btn-shadow p-2"
                  size="md"
                  style={{ backgroundColor: '#0b3858', padding: '8px 7px!important' }}
                >
                <img src={calendlyIcon} style={{width: '19px', backgroundColor:"#054f8400"}}></img>
                  &nbsp; Schedule a demo call
                </Button>
                </a>
                {/*<Button
                  className="btn-shadow p-2 btn-danger"
                  size="md"
                  onClick={() => {
                    this.setState({...this.state, show: true})
                  }}
                //   style={{ backgroundColor: '#2e7ab0' }}
                >
                   Uninstall App
                </Button>*/}
              </div>
              {/* <Headphones color="#ffffff" fontSize="20px" icon="pe-7s-headphones"/> */}
              {/* </Button> */}
              {/* <UncontrolledTooltip placement="bottom" target={'Tooltip-1'}>
                                    Click for Toastify Notifications!
                                </UncontrolledTooltip> */}
              {/* </div> */}
            </div>
          </div>
        </div>
         <Modal
        modalClassName={'custom-header-modal'}
        size="lg"
        isOpen={ this.state.show }
        toggle={() => {this.setState({...this.state, show: false})}}
        className={this.props.className}>
                    <ModalHeader toggle={() => {this.setState({...this.state, show: false})}}>Uninstall App</ModalHeader>
                    <ModalBody>
                        <Label  for={`reasonForUninstall`}>
                        Reason for uninstall:
                        </Label>
                        <FormGroup>
                              <Input type="select"
                              onChange={(e) => {this.setState({...this.state, reason:e.target.value})
                              }}>

                                    <option value="justTestingApp">Just testing app</option>
                                    <option value="SubscriptionsModelNotNeeded">Subscriptions model not needed</option>
                                    <option value="AppNotPerformingWell">App not performing well</option>
                                    <option value="FoundABetterApp">Found a better app</option>
                                  </Input>
                            </FormGroup>
                        <div className='text-center mt-4 mb-2'>{this.state.reason === 'AppNotPerformingWell' ? "We can hop on a quick chat or call to fix any issues you are facing."
                        : this.state.reason === 'FoundABetterApp' ? "We hate to see you go. Provide us the opportunity to serve you better." : '' }</div>
                        <div className='text-center'>
                             {this.state.reason === 'AppNotPerformingWell' ||  this.state.reason === 'FoundABetterApp'?
                            <Button color="primary" className='mb-2' onClick={() => {
                                this.state.reason === 'AppNotPerformingWell' ?
                                    IntercomAPI('showNewMessage', 'Uninstall: App not performing well.') :  IntercomAPI('showNewMessage', 'Uninstall: Found a better App.');}}>
                           <span className='mr-2'><AiOutlineMessage size={20} /></span>
                            {this.state.reason === 'AppNotPerformingWell' || this.state.reason === 'FoundABetterApp' ? "Please ping us here to chat with us" : "" }

                            </Button>: null}
                        </div>
                    </ModalBody>
                    <ModalFooter>
                        <Button color="link" onClick={() => {this.setState({...this.state, show: false})}}>Cancel</Button>
                        <Button color="danger" onClick={() =>{window.top.location.href = `https://${this.props?.shopInfo?.shop}/admin/settings/apps`}}>
                            Uninstall
                            </Button>


                    </ModalFooter>
                </Modal>
      </Fragment>
    );
  }
}
const mapStateToProps = state => ({
  shopInfo: state.shopInfo.entity
});
export default connect(mapStateToProps, null)(UserBox);
