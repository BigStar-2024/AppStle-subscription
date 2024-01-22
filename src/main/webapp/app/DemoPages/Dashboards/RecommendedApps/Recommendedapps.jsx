import PageTitle from 'app/Layout/AppMain/PageTitle';
import React, { Fragment, useState } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { connect } from 'react-redux';
import { Row, Card, Col, CardBody, CardSubtitle, CardTitle, CardFooter } from 'reactstrap';
import { getEntity } from 'app/entities/shop-info/shop-info.reducer';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import PageFly from '../Integrations/PageFly/PageFly';

function Recommendedapps() {
  const [modelToggleState, setModelToggleState] = useState({ pageFly: false });
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
          heading="Recommended Apps"
          // subheading="Integrations with other apps and services"
          icon="lnr-pencil icon-gradient bg-mean-fruit"
          actionTitle="Request Support For Other App"
          enablePageTitleAction
          onActionClick={() => {
            Intercom('showNewMessage', 'I would like to request a new Apps.');
          }}
          sticky={true}
        />
        <Row>
          <Col md={4} className="pb-3">
            <Card
              className="he-100"
              // onClick={() => {
              //   setModelToggleState({ pageFly: true });
              // }}
            >
              <CardBody className="text-center">
                <div className="m-auto">
                  <img className="mb-2" style={{ maxWidth: '40%' }} src={require('./PAGEFLY_LOGO_VERTICAL_Blue.png')} />
                </div>
                <br />
                <br />
                <CardTitle>PageFly Landing Page Builder</CardTitle>
                <CardSubtitle>
                  We recommend PageFly app.
                  <br />
                  Appstle widget successfully integrates with PageFly Pages.
                  <br />
                </CardSubtitle>
              </CardBody>
              <CardFooter className="justify-content-center">
                Want to try PageFly? &nbsp;
                <a href="https://pagef.ly/1up9nv" target="_blank">
                  {' '}
                  Click Here
                </a>
              </CardFooter>
            </Card>
          </Col>
          <Col md={4} className="pb-3">
            <Card
              className="he-100"
              // onClick={() => {
              //   setModelToggleState({ pageFly: true });
              // }}
            >
              <CardBody className="text-center">
                <div className="m-auto">
                  <img className="mb-2" style={{ maxWidth: '40%', width: '30%' }} src={require('./salesnotify.png')} />
                </div>
                <br />
                <br />
                <CardTitle>ToastiBar - Sales Popup App</CardTitle>
                <CardSubtitle>
                  We recommended ToastiBar - Sales Popup app. <br />
                  Appstle widget successfully integrates with ToastiBar - Sales Popup app.
                  <br />
                </CardSubtitle>
              </CardBody>
              <CardFooter className="justify-content-center">
                Want to try ToastiBar - Sales Popup? &nbsp;
                <a
                  href="https://apps.shopify.com/mps-sales-notification?utm_source=appstle&utm_campaign=partnership&utm_medium=in-app-recommendation"
                  target="_blank"
                >
                  {' '}
                  Click Here
                </a>
              </CardFooter>
            </Card>
          </Col>
          <Col md={4} className="pb-3">
            <Card
              className="he-100"
              // onClick={() => {
              //   setModelToggleState({ pageFly: true });
              // }}
            >
              <CardBody className="text-center">
                <div className="m-auto">
                  <img className="mb-2" style={{ maxWidth: '40%', width: '30%' }} src={require('./txtCartPlus.png')} />
                </div>
                <br />
                <br />
                <CardTitle>TxtCart Plus + SMS Marketing</CardTitle>
                <CardSubtitle>
                  We recommend TxtCart app.
                  <br />
                  Text Marketing, SMS Cart Recovery and Analytics Powered by AI
                  <br />
                </CardSubtitle>
              </CardBody>
              <CardFooter className="justify-content-center">
                Want to try TxtCart? &nbsp;
                <a
                  href="https://apps.shopify.com/txtcart-plus?utm_source=appstle&utm_campaign=partnership&utm_medium=in-app-recommendation"
                  target="_blank"
                >
                  {' '}
                  Click Here
                </a>
              </CardFooter>
            </Card>
          </Col>
          <Col md={4} className="pb-3">
            <Card
              className="he-100"
              // onClick={() => {
              //   setModelToggleState({ pageFly: true });
              // }}
            >
              <CardBody className="text-center">
                <div className="m-auto">
                  <img
                    className="mb-2"
                    style={{ maxWidth: '40%', width: '30%' }}
                    src="https://cdn.shopify.com/app-store/listing_images/39667e1b914fcca4e73731f12966de2f/icon/CN_T2salmfQCEAE=.png"
                  />
                </div>
                <br />
                <br />
                <CardTitle>Froonze</CardTitle>
                <CardSubtitle>
                  We recommend Froonze app.
                  <br />
                  Customer account page, Wishlist, Social Login, Reorder
                  <br />
                </CardSubtitle>
              </CardBody>
              <CardFooter className="justify-content-center">
                Want to try Froonze? &nbsp;
                <a
                  href="https://apps.shopify.com/customer-accounts?utm_source=subscriptions-by-appstle&utm_campaign=newsletter"
                  target="_blank"
                >
                  {' '}
                  Click Here
                </a>
              </CardFooter>
            </Card>
          </Col>
          <Col md={4} className="pb-3">
            <Card
              className="he-100"
              // onClick={() => {
              //   setModelToggleState({ pageFly: true });
              // }}
            >
              <CardBody className="text-center">
                <div className="m-auto">
                  <img
                    className="mb-2"
                    style={{ maxWidth: '40%', width: '30%' }}
                    src="https://cdn.shopify.com/app-store/listing_images/43736e019bc30181bffc39c28399ed98/icon/CKKnmp6v-vcCEAE=.png"
                  />
                </div>
                <br />
                <br />
                <CardTitle>Sky Pilot</CardTitle>
                <CardSubtitle>
                  We recommend Sky Pilot - Digital Downloads.
                  <br />
                  Sell and deliver music, videos, books, or any digital files instantly, and without limits.
                  <br />
                </CardSubtitle>
              </CardBody>
              <CardFooter className="justify-content-center">
                Want to try Sky Pilot? &nbsp;
                <a href="https://apps.shopify.com/sky-pilot?utm_source=subscriptions-by-appstle&utm_campaign=newsletter" target="_blank">
                  {' '}
                  Click Here
                </a>
              </CardFooter>
            </Card>
          </Col>
          <Col md={4} className="pb-3">
            <Card className="he-100">
              <CardBody className="text-center">
                <div className="m-auto">
                  <img
                    className="mb-2"
                    style={{ maxWidth: '40%', width: '30%' }}
                    src="https://cdn.shopify.com/app-store/listing_images/8bd94c23dd747d81fc3d843cfbd14e01/icon/CInQwsL0lu8CEAE=.png"
                  />
                </div>
                <br />
                <br />
                <CardTitle>MultiVariants - Bulk Order</CardTitle>
                <CardSubtitle>
                  We recommend MultiVariants - Bulk Order
                  <br />
                  List variants in a beautiful table/grid layout. Let customers bulk order multiple variants easily.  <br />
                </CardSubtitle>
              </CardBody>
              <CardFooter className="justify-content-center">
                Want to try MultiVariants - Bulk Order? &nbsp;
                <a href="https://apps.shopify.com/multivariants?utm_source=subscriptions-by-appstle&utm_campaign=newsletter" target="_blank">
                  {' '}
                  Click Here
                </a>
              </CardFooter>
            </Card>
          </Col>
          <Col md={4} className="pb-3">
            <Card className="he-100">
              <CardBody className="text-center">
                <div className="m-auto">
                  <img
                    className="mb-2"
                    style={{ maxWidth: '40%', width: '30%' }}
                    src="https://cdn.shopify.com/app-store/listing_images/b0d1f54b1219bacc5e80ab7ce7c7dba5/icon/CLbN_oefsIADEAE=.png"
                  />
                </div>
                <br />
                <br />
                <CardTitle>Monster Cart Upsell+Free Gifts</CardTitle>
                <CardSubtitle>
                  We recommend Monster Cart Upsell+Free Gifts
                  <br />
                  Boost Revenue in minutes... using a Slide Cart Drawer with In Cart Upsells & Free Gift Rewards.
                  <br />
                </CardSubtitle>
              </CardBody>
              <CardFooter className="justify-content-center">
                Want to try Monster Cart Upsell+Free Gifts? &nbsp;
                <a href="https://apps.shopify.com/monster-upsells?utm_source=subscriptions-by-appstle&utm_campaign=newsletter" target="_blank">
                  {' '}
                  Click Here
                </a>
              </CardFooter>
            </Card>
          </Col>
          <Col md={4} className="pb-3">
            <Card className="he-100">
              <CardBody className="text-center">
                <div className="m-auto">
                  <img
                    className="mb-2"
                    style={{ maxWidth: '40%', width: '30%' }}
                    src="https://cdn.shopify.com/app-store/listing_images/1252d9d2612d6b071896af7336148d30/icon/CPmDoZmM1P8CEAE=.png"
                  />
                </div>
                <br />
                <br />
                <CardTitle>Dropshipman: Dropshipping & POD</CardTitle>
                <CardSubtitle>
                  We recommend Dropshipman: Dropshipping & POD
                  <br />
                  Trustworthy dropshipping platforms, print on demand, and solution for exceptional dropshippers
                  <br />
                </CardSubtitle>
              </CardBody>
              <CardFooter className="justify-content-center">
                Want to try Dropshipman: Dropshipping & POD? &nbsp;
                <a href="https://apps.shopify.com/aliexpress-dropshipping-master?utm_source=subscriptions-by-appstle&utm_campaign=newsletter" target="_blank">
                  {' '}
                  Click Here
                </a>
              </CardFooter>
            </Card>
          </Col>
        </Row>
      </ReactCSSTransitionGroup>

      {/* <PageFly
            isOpen={modelToggleState?.pageFly}
            onClose={() => {
                setModelToggleState({ pageFly: false });
            }}
          /> */}
    </Fragment>
  );
}

export default Recommendedapps;
