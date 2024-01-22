import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Row, Col } from 'reactstrap';
import { BuildABoxType } from 'app/shared/model/enumerations/build-a-box-type.model';

type BuildABoxLearnModalProps = {
  boxType: BuildABoxType;
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const ClassicInfo = () => {
  return (
    <div className="px-5">
      <Row>
        <Col xs={12} lg={6}>
          <p className="mb-0">
            Classic Build-A-Box allows merchants to use existing subscription plans and allow their customers to select from the available
            products in those plans to create a unique subscription box. The merchant configures the products and delivery frequencies in
            Manage Plans, and in Build-A-Box the merchant configures the Classic Build-A-Box and chooses which plans are available to the
            customer. Once configured, the customer purchasing the Classic Build-A-Box can choose what products they want in their box and
            choose the from the available delivery frequencies.
          </p>
        </Col>
        <Col xs={12} lg={6}>
          <img
            src="https://cdn.shopify.com/s/files/1/0563/5257/1564/files/Classic_BaB_Product_Selection.png?v=1690397276"
            alt="Classic Build-A-Box Product Selection"
            style={{ width: '100%' }}
          />
        </Col>
      </Row>
      <hr className="w-100" />
      <Row className="d-flex justify-content-center">
        <Col xs={12} md={8}>
          <h5 className="h5 text-center">Watch this video about setting up Classic Build-A-Box</h5>
          <div style={{ aspectRatio: '16 / 9', width: '100%' }}>
            <iframe
              src="https://www.youtube.com/embed/1sIU8pTYwd4"
              title="Classic Build-A-Box"
              frameBorder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
              allowFullScreen
              style={{ width: '100%', height: '100%' }}
            ></iframe>
          </div>
        </Col>
      </Row>
    </div>
  );
};

const SingleProductInfo = () => {
  return (
    <div className="px-5">
      <Row>
        <Col xs={12} md={7}>
          <p>
            Single Product Build-A-Box allows merchants to use an existing Subscription Plan for Combo Pricing and fix the final price of
            the box, regardless of the price of the components in it. The merchant can set parameters such as the number of component items
            (e.g. 3 items), description of component items (e.g 1 meal, 1 drink, 1 side).
          </p>
        </Col>
      </Row>
      <Row className="d-flex align-items-start py-2 border-bottom">
        <Col xs={12} lg={7} className="pr-3">
          <p>
            <strong>
              <u>An example:</u>
            </strong>{' '}
            A Subscription Plan has 3 products in it that are used as the "source products," which are{' '}
            <b>
              <i>7-Day Meal Plan</i>
            </b>
            ,{' '}
            <b>
              <i>14-Day Meal Plan</i>
            </b>
            , and{' '}
            <b>
              <i>7-Day Vegetarian Meal Plan</i>
            </b>
            . Each Single Product Setting has a source product and the selected available products in the Setting.
          </p>
        </Col>
        <Col xs={12} lg={5}>
          <img
            src="https://cdn.shopify.com/s/files/1/0563/5257/1564/files/Single_Product_BaB_Settings.png?v=1690406179"
            alt="Classic Build-A-Box Product Selection"
            className="border border-primary"
            style={{ width: '100%' }}
          />
        </Col>
      </Row>
      <Row className="d-flex align-items-start py-2 border-bottom">
        <Col xs={12} lg={7} className="pr-3">
          <p>
            The customer can choose which of these Single Product Settings to select for their subscription box. Each Single Product Setting
            is named and priced based on the source product.
          </p>
        </Col>
        <Col xs={12} lg={5}>
          <img
            src="https://cdn.shopify.com/s/files/1/0563/5257/1564/files/Single_Product_BaB_Plans.png?v=1690397276"
            alt="Classic Build-A-Box Product Selection"
            className="border border-primary"
            style={{ width: '100%' }}
          />
        </Col>
      </Row>
      <Row className="d-flex align-items-start py-2">
        <Col xs={12} lg={7} className="pr-3">
          <p>
            The customer can then choose which products they want in their Single Product subscription Build-A-Box and have it delivered
            based on one of the delivery frequencies available in the Subscription Plan chosen by the merchant.
          </p>
        </Col>
        <Col xs={12} lg={5}>
          <img
            src="https://cdn.shopify.com/s/files/1/0563/5257/1564/files/Single_Product_BaB_Products.png?v=1690397276"
            alt="Classic Build-A-Box Product Selection"
            className="border border-primary"
            style={{ width: '100%' }}
          />
        </Col>
      </Row>
      <hr className="w-100" />
      <Row className="d-flex justify-content-center">
        <Col xs={12} md={8}>
          <h5 className="h5 text-center">Watch this video about setting up Single Product Build-A-Box</h5>
          <div style={{ aspectRatio: '16 / 9', width: '100%' }}>
            <iframe
              src="https://www.youtube.com/embed/4QPhUyZV0wI"
              title="Single Product Build-A-Box"
              frameBorder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
              allowFullScreen
              style={{ width: '100%', height: '100%' }}
            ></iframe>
          </div>
        </Col>
      </Row>
    </div>
  );
};

const BuildABoxLearnModal = ({ boxType, isOpen, setIsOpen }: BuildABoxLearnModalProps) => {
  const toggle = () => setIsOpen(!isOpen);
  return (
    <Modal isOpen={isOpen} toggle={toggle} size="xl">
      <ModalHeader toggle={toggle}>{`What is ${
        boxType === BuildABoxType.CLASSIC ? 'Classic Build-A-Box' : 'Single Product Build-A-Box'
      }?`}</ModalHeader>
      <ModalBody className="overflow-auto">{boxType === BuildABoxType.CLASSIC ? <ClassicInfo /> : <SingleProductInfo />}</ModalBody>
      <ModalFooter className="d-flex justify-content-center">
        <Button
          size="lg"
          color="primary"
          onClick={(e: React.MouseEvent) => {
            e.preventDefault();
            setIsOpen(false);
          }}
          className="col-xs-12 col-md-4"
        >
          Close
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default BuildABoxLearnModal;
