// tslint:disable
import YoutubeVideoPlayer from "./YoutubeVideoPlayer";
import PageTitle from "app/Layout/AppMain/PageTitle";
import React, { Fragment, useEffect, useState } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { Card, CardBody, CardHeader, Col, Label, NavLink, Row, Collapse } from 'reactstrap';
import { ChevronForward } from 'react-ionicons';
import './video-modal.scss';

const Tutorials = () => {
  const [accordionState, setAccordionState] = useState([false, false, false, false, false, false, false, false]);
  const [isOpen, setIsOpen] = useState(false)

  const forward_arrow_icon = {
    marginLeft: 'auto',
    transition: 'transform 0.2s',
    transformOrigin: 'center'
  }

  const toggleAccordion = (tab) => {
    const prevState = accordionState;
    const state = prevState.map((x, index) => (tab === index ? !x : false));
    setAccordionState(state);
  }

  const generalVideos = [
    {
      name: 'Swap Automation - Subscriptions',
      url: 'https://youtu.be/WdvrWjk3uFo',
      isYouTubeURL: true
    },
    {
      name: 'Swap Automation - Advanced Use Case',
      url: 'https://youtu.be/muZtybC2u7w',
      isYouTubeURL: true
    },
    {
      name: "Klaviyo Integration",
      url: "https://youtu.be/A0UaERSE1XI",
      isYouTubeURL: true
    },
    {
      name: "App Integrations for Appstle Subscriptions (w/ Klaviyo Integration Example)",
      url: "https://youtu.be/Vf67zzsFIbo",
      isYouTubeURL: true
    },
    {
      name: 'How To Set Up Custom Email Domain',
      url: 'https://youtu.be/Xv7M7ty3cdU',
      isYouTubeURL: true
    },
    {
      name: 'Dunning Management',
      url: 'https://youtu.be/jlmCwVZzViQ',
      isYouTubeURL: true
    }
  ];

  const loyaltyVideos = [
    {
      name: 'Shipping Price',
      url: 'https://youtu.be/ER5i3fAjHTQ',
      isYouTubeURL: true
    },
    {
      name: 'Amount Off',
      url: 'https://youtu.be/Mj3IiqcFuSE',
      isYouTubeURL: true
    },
    {
      name: 'Free Product',
      url: 'https://youtu.be/8oFpQq6lkDg',
      isYouTubeURL: true
    },
    {
      name: 'Percentage Discount',
      url: 'https://youtu.be/dwONFEPDQYo',
      isYouTubeURL: true
    }
  ];

  const subscriptionPlanVideos = [
    {
      name: 'Pay As You Go',
      url: 'https://youtu.be/AKR4gfRdEMU',
      isYouTubeURL: true
    },
    {
      name: 'Prepaid Auto Renew',
      url: 'https://youtu.be/P0ncMfwwz_s',
      isYouTubeURL: true
    },
    {
      name: 'Date-Picker',
      url: 'https://youtu.be/f6DuDchH-QA',
      isYouTubeURL: true
    },
    {
      name: 'Free Trial',
      url: 'https://youtu.be/4zR59ga5jKk',
      isYouTubeURL: true
    },
    {
      name: 'Monthly Cutoff Days',
      url: 'https://youtu.be/FVyb76d7X9Y',
      isYouTubeURL: true
    },
    {
      name: 'Specific Tags',
      url: 'https://youtu.be/LpEO3tKhcJc',
      isYouTubeURL: true
    },
    {
      name: 'Prepaid One Time',
      url: 'https://youtu.be/zNJSLOhOJYM',
      isYouTubeURL: true
    },
    {
      name: 'Advanced Features',
      url: 'https://youtu.be/v1G3_-uhj3M',
      isYouTubeURL: true
    },
    {
      name: 'Weekly Cutoff Days',
      url: 'https://youtu.be/Sc2DI31ow5o',
      isYouTubeURL: true
    }
  ];

  const customerVideos = [
    {
      name: 'Customer Section',
      url: 'https://youtu.be/e2k77lyHjZM',
      isYouTubeURL: true
    },
    {
      name: 'Syncing Customer Emails',
      url: 'https://youtu.be/OR-2FVvO-q4',
      isYouTubeURL: true
    },
    {
      name: 'How to Access the Customer Portal',
      url: 'https://youtu.be/aIslGRp83cY',
      isYouTubeURL: true
    }
  ];

  const subscriptionVideos = [
    {
      name: 'Create Subscription',
      url: 'https://youtu.be/4ViJlPmWSSA',
      isYouTubeURL: true
    },
    {
      name: 'Subscriptions and Filtering',
      url: 'https://youtu.be/QDEP-0jLWek',
      isYouTubeURL: true
    },
    {
      name: 'Export Subscription Data',
      url: 'https://youtu.be/AvohNGVCX6E',
      isYouTubeURL: true
    },
    {
      name: 'Top Bar',
      url: 'https://youtu.be/835-hT_GkDQ',
      isYouTubeURL: true
    },
    {
      name: 'Adding and Removing Products and Attributes',
      url: 'https://youtu.be/5hsTuLus1i4',
      isYouTubeURL: true
    },
    {
      name: 'Pricing Policy',
      url: 'https://youtu.be/c-EyYIoQjLM',
      isYouTubeURL: true
    },
    {
      name: 'Central Panel',
      url: 'https://youtu.be/gEbM43EDUpo',
      isYouTubeURL: true
    },
    {
      name: 'Discount Codes',
      url: 'https://youtu.be/NGMBXWfsZRA',
      isYouTubeURL: true
    },
    {
      name: 'Side Panel',
      url: 'https://youtu.be/qW5_Dopw9BA',
      isYouTubeURL: true
    },
    {
      name: 'Past Orders',
      url: 'https://youtu.be/GLeVBXSNmv4',
      isYouTubeURL: true
    },
    {
      name: 'Upcoming Orders',
      url: 'https://youtu.be/R3gJZb7ZSIw',
      isYouTubeURL: true
    },
    {
      name: 'Cancel Subscription',
      url: 'https://youtu.be/3Mhcr0aVYrM',
      isYouTubeURL: true
    },
    {
      name: 'Activity Logs',
      url: 'https://youtu.be/dBlNeE3SLIk',
      isYouTubeURL: true
    },
    {
      name: 'Creating Multiple Subscription Options Using Multiple Variants',
      url: 'https://youtu.be/eW-CSvV_52k',
      isYouTubeURL: true
    }
  ];

  const acquireSubscribersVideos = [
    {
      name: 'Quick Checkout',
      url: 'https://youtu.be/7ctZj-UbwUw',
      isYouTubeURL: true
    },
    {
      name: 'Quick Actions',
      url: 'https://youtu.be/mTHHcnXkViI',
      isYouTubeURL: true
    },
    {
      name: 'Build-A-Box - Classic',
      url: 'https://youtu.be/1sIU8pTYwd4',
      isYouTubeURL: true
    },
    {
      name: 'Build-A-Box - Single Product',
      url: 'https://youtu.be/4QPhUyZV0wI',
      isYouTubeURL: true
    },
    {
      name: 'Bundling',
      url: 'https://youtu.be/63BM5YIJh70',
      isYouTubeURL: true
    },
    {
      name: 'Appstle Menu',
      url: 'https://youtu.be/EqcHDgBBjhU',
      isYouTubeURL: true
    }
  ];

  const shippingVideos = [
    {
      name: 'How to Create a Shipping Profile',
      url: 'https://youtu.be/AcknZAvk0s8',
      isYouTubeURL: true
    },
    {
      name: 'How to Define Delivery Conditions',
      url: 'https://youtu.be/IvIEyR74bvM',
      isYouTubeURL: true
    }
  ];

  const cancellationVideos = [
    {
      name: 'Cancel Immediately',
      url: 'https://youtu.be/89ijO4qRaAM',
      isYouTubeURL: true
    },
    {
      name: 'Provide Cancellation Instructions',
      url: 'https://youtu.be/PH-C0aPNntM',
      isYouTubeURL: true
    },
    {
      name: 'Customer Retention Flow',
      url: 'https://youtu.be/K7Qglo_nJoo',
      isYouTubeURL: true
    },
    {
      name: 'Pause Option (as a Retention) Step Before Cancellation',
      url: 'https://youtu.be/BShJNofKHk8',
      isYouTubeURL: true
    }
  ];

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
          heading="Video Tutorials"
          subheading=""
          icon="lnr-layers icon-gradient bg-mean-fruit"
          sticky={false}
        />
        <div>
          <Card className="main-card">
            <CardHeader
              onClick={() => (toggleAccordion(0))}
              aria-expanded={accordionState[0]}
              aria-controls="modifysubscriptionstatus"
              style={{ cursor: 'pointer' }}>
              <i className="header-icon pe-7s-tools icon-gradient bg-plum-plate"> </i> <span>Configuration of  <span style={{ color: "rgba(18, 21, 78,0.9)" }}> Subscription Plan </span></span>
              <span style={{ ...forward_arrow_icon, transform: accordionState[0] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[0]} data-parent="#accordion" id="modifysubscriptionstatus"
              aria-labelledby="WidgetLabel">
              <CardBody>
                <Row>
                  {subscriptionPlanVideos.map((value, index) => {
                    return <Col md={4} key={index}>
                      <Card style={{ width: '100%' }} className="card-border mr-3 mb-3 card-hover-shadow-2x">
                        <CardHeader>{value.name}</CardHeader>
                        <CardBody className="top-elem">
                          <div className="text-center">
                            {value.isYouTubeURL ? <YoutubeVideoPlayer key={`key_${index}`} url={value.url} iframeHeight="100%" divClassName="video-container" iframeClassName="responsive-iframe"/> :
                              <div className="video-container" ><iframe className="responsive-iframe" src={value.url} width="100%" height="100%" frameBorder="0" allowFullScreen ></iframe></div>}
                          </div>
                        </CardBody>
                      </Card>
                    </Col>
                  })}
                </Row>
              </CardBody>
            </Collapse>
          </Card>

          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => (toggleAccordion(1))}
              aria-expanded={accordionState[1]}
              aria-controls="modifysubscriptionstatus"
              style={{ cursor: 'pointer' }}>
              <i className="header-icon pe-7s-network icon-gradient bg-plum-plate"> </i> <span>Configuration of  <span style={{ color: "rgba(18, 21, 78,0.9)" }}> Loyalty Settings </span></span>
              <span style={{ ...forward_arrow_icon, transform: accordionState[1] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[1]} data-parent="#accordion" id="modifysubscriptionstatus"
              aria-labelledby="WidgetLabel">
              <CardBody>
                <Row>
                  {loyaltyVideos.map((value, index) => {
                    return <Col md={4} key={index}>
                      <Card style={{ width: '100%' }} className="card-border mr-3 mb-3 card-hover-shadow-2x">
                        <CardHeader>{value.name}</CardHeader>
                        <CardBody className="top-elem">
                          <div className="text-center">
                            {value.isYouTubeURL ? <YoutubeVideoPlayer key={`key_${index}`} url={value.url} iframeHeight="100%" divClassName="video-container" iframeClassName="responsive-iframe"/> :
                              <div className="video-container" ><iframe className="responsive-iframe" src={value.url} width="100%" height="100%" frameBorder="0" allowFullScreen ></iframe></div>}
                          </div>
                        </CardBody>
                      </Card>
                    </Col>
                  })}
                </Row>
              </CardBody>
            </Collapse>
          </Card>

          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => (toggleAccordion(2))}
              aria-expanded={accordionState[2]}
              aria-controls="modifysubscriptionstatus"
              style={{ cursor: 'pointer' }}>
              <i className="header-icon pe-7s-users icon-gradient bg-plum-plate"> </i> <span>Customers</span>
              <span style={{ ...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[2]} data-parent="#accordion" id="modifysubscriptionstatus"
              aria-labelledby="WidgetLabel">
              <CardBody>
                <Row>
                  {customerVideos.map((value, index) => {
                    return <Col md={4} key={index}>
                      <Card style={{ width: '100%' }} className="card-border mr-3 mb-3 card-hover-shadow-2x">
                        <CardHeader>{value.name}</CardHeader>
                        <CardBody className="top-elem">
                          <div className="text-center">
                            {value.isYouTubeURL ? <YoutubeVideoPlayer key={`key_${index}`} url={value.url} iframeHeight="100%" divClassName="video-container" iframeClassName="responsive-iframe" /> :
                              <div className="video-container" ><iframe className="responsive-iframe" src={value.url} width="100%" height="100%" frameBorder="0" allowFullScreen ></iframe></div>}
                          </div>
                        </CardBody>
                      </Card>
                    </Col>
                  })}
                </Row>
              </CardBody>
            </Collapse>
          </Card>

          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => (toggleAccordion(3))}
              aria-expanded={accordionState[3]}
              aria-controls="modifysubscriptionstatus"
              style={{ cursor: 'pointer' }}>
              <i className="header-icon pe-7s-repeat icon-gradient bg-plum-plate"> </i> <span>Subscriptions</span>
              <span style={{ ...forward_arrow_icon, transform: accordionState[3] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[3]} data-parent="#accordion" id="modifysubscriptionstatus"
              aria-labelledby="WidgetLabel">
              <CardBody>
                <Row>
                  {subscriptionVideos.map((value, index) => {
                    return <Col md={4} key={index}>
                      <Card style={{ width: '100%' }} className="card-border mr-3 mb-3 card-hover-shadow-2x">
                        <CardHeader>{value.name}</CardHeader>
                        <CardBody className="top-elem">
                          <div className="text-center">
                            {value.isYouTubeURL ? <YoutubeVideoPlayer key={`key_${index}`} url={value.url} iframeHeight="100%" divClassName="video-container" iframeClassName="responsive-iframe" /> :
                              <div className="video-container" ><iframe className="responsive-iframe" src={value.url} width="100%" height="100%" frameBorder="0" allowFullScreen ></iframe></div>}
                          </div>
                        </CardBody>
                      </Card>
                    </Col>
                  })}
                </Row>
              </CardBody>
            </Collapse>
          </Card>

          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => (toggleAccordion(4))}
              aria-expanded={accordionState[4]}
              aria-controls="modifysubscriptionstatus"
              style={{ cursor: 'pointer' }}>
              <i className="header-icon lnr-cog icon-gradient bg-plum-plate"> </i> General Tutorial Videos
              <span style={{ ...forward_arrow_icon, transform: accordionState[4] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[4]} data-parent="#accordion" id="modifysubscriptionstatus"
              aria-labelledby="WidgetLabel">
              <CardBody>
                <Row>
                  {generalVideos.map((value, index) => {
                    return <Col md={4} key={index}>
                      <Card style={{ width: '100%' }} className="card-border mr-3 mb-3 card-hover-shadow-2x">
                        <CardHeader>{value.name}</CardHeader>
                        <CardBody className="top-elem">
                          <div className="text-center">
                            {value.isYouTubeURL ? <YoutubeVideoPlayer key={`key_${index}`} url={value.url} iframeHeight="100%" divClassName="video-container" iframeClassName="responsive-iframe"/> :
                              <div className="video-container" ><iframe className="responsive-iframe" src={value.url} width="100%" height="100%" frameBorder="0" allowFullScreen ></iframe></div>}
                          </div>
                        </CardBody>
                      </Card>
                    </Col>
                  })}
                </Row>
              </CardBody>
            </Collapse>
          </Card>
          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => (toggleAccordion(5))}
              aria-expanded={accordionState[5]}
              aria-controls="modifysubscriptionstatus"
              style={{ cursor: 'pointer' }}>
              <i className="header-icon lnr-magic-wand icon-gradient bg-plum-plate"> </i> ACQUIRE SUBSCRIBERS
              <span style={{ ...forward_arrow_icon, transform: accordionState[5] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[5]} data-parent="#accordion" id="modifysubscriptionstatus"
              aria-labelledby="WidgetLabel">
              <CardBody>
                <Row>
                  {acquireSubscribersVideos.map((value, index) => {
                    return <Col md={4} key={index}>
                      <Card style={{ width: '100%' }} className="card-border mr-3 mb-3 card-hover-shadow-2x">
                        <CardHeader>{value.name}</CardHeader>
                        <CardBody className="top-elem">
                          <div className="text-center">
                            {value.isYouTubeURL ? <YoutubeVideoPlayer key={`key_${index}`} url={value.url} iframeHeight="100%" divClassName="video-container" iframeClassName="responsive-iframe"/> :
                              <div className="video-container" ><iframe className="responsive-iframe" src={value.url} width="100%" height="100%" frameBorder="0" allowFullScreen ></iframe></div>}
                          </div>
                        </CardBody>
                      </Card>
                    </Col>
                  })}
                </Row>
              </CardBody>
            </Collapse>
          </Card>
          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => toggleAccordion(6)}
              aria-expanded={accordionState[6]}
              aria-controls="modifysubscriptionstatus"
              style={{ cursor: 'pointer' }}
            >
              <i className="header-icon lnr-rocket icon-gradient bg-plum-plate"> </i> SHIPPING PROFILES
              <span style={{ ...forward_arrow_icon, transform: accordionState[6] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[6]} data-parent="#accordion" id="modifysubscriptionstatus" aria-labelledby="WidgetLabel">
              <CardBody>
                <Row>
                  {shippingVideos.map((value, index) => {
                    return (
                      <Col md={4} key={index}>
                        <Card style={{ width: '100%' }} className="card-border mr-3 mb-3 card-hover-shadow-2x">
                          <CardHeader>{value.name}</CardHeader>
                          <CardBody className="top-elem">
                            <div className="text-center">
                              {value.isYouTubeURL ? (
                                <YoutubeVideoPlayer
                                  key={`key_${index}`}
                                  url={value.url}
                                  iframeHeight="100%"
                                  divClassName="video-container"
                                  iframeClassName="responsive-iframe"
                                />
                              ) : (
                                <div className="video-container">
                                  <iframe
                                    className="responsive-iframe"
                                    src={value.url}
                                    width="100%"
                                    height="100%"
                                    frameBorder="0"
                                    allowFullScreen
                                  ></iframe>
                                </div>
                              )}
                            </div>
                          </CardBody>
                        </Card>
                      </Col>
                    );
                  })}
                </Row>
              </CardBody>
            </Collapse>
          </Card>
          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => toggleAccordion(7)}
              aria-expanded={accordionState[7]}
              aria-controls="modifysubscriptionstatus"
              style={{ cursor: 'pointer' }}
            >
              <i className="header-icon lnr-cross-circle icon-gradient bg-plum-plate"> </i> CANCELLATION MANAGEMENT
              <span style={{ ...forward_arrow_icon, transform: accordionState[7] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[7]} data-parent="#accordion" id="modifysubscriptionstatus" aria-labelledby="WidgetLabel">
              <CardBody>
                <Row>
                  {cancellationVideos.map((value, index) => {
                    return (
                      <Col md={4} key={index}>
                        <Card style={{ width: '100%' }} className="card-border mr-3 mb-3 card-hover-shadow-2x">
                          <CardHeader>{value.name}</CardHeader>
                          <CardBody className="top-elem">
                            <div className="text-center">
                              {value.isYouTubeURL ? (
                                <YoutubeVideoPlayer
                                  key={`key_${index}`}
                                  url={value.url}
                                  iframeHeight="100%"
                                  divClassName="video-container"
                                  iframeClassName="responsive-iframe"
                                />
                              ) : (
                                <div className="video-container">
                                  <iframe
                                    className="responsive-iframe"
                                    src={value.url}
                                    width="100%"
                                    height="100%"
                                    frameBorder="0"
                                    allowFullScreen
                                  ></iframe>
                                </div>
                              )}
                            </div>
                          </CardBody>
                        </Card>
                      </Col>
                    );
                  })}
                </Row>
              </CardBody>
            </Collapse>
          </Card>
        </div>

      </ReactCSSTransitionGroup>
    </Fragment>
  )
}

export default Tutorials
