// @ts-expect-error ts(5097) An import path can only end with a '.tsx' extension when 'allowImportingTsExtensions' is enabled.
import { AnalyticsContext } from 'app/DemoPages/Dashboards/Analytics/Analytics.tsx';
import DateRangeText from 'app/DemoPages/Dashboards/Analytics/DateRangeText';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import axios from 'axios';
import React, { useContext } from 'react';
import {
  Button,
  Card,
  CardBody,
  CardTitle,
  FormFeedback,
  Input,
  Label,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Table,
} from 'reactstrap';
import SweetAlert from 'sweetalert-react';

export default function ProductDeliveryForecasting() {
  const {
    productDeliveryAnalyticsList,
    isModalOpen,
    setIsModalOpen,
    emailValidity,
    emailSuccessAlert,
    setEmailSuccessAlert,
    emailSendingProgress,
    emailFailAlert,
    setEmailFailAlert,
    blurred,
    setBlurred,
    setInputValueForTestEmailId,
    setEmailSendingProgress,
    setEmailValidity,
    inputValueForTestEmailId,
  } = useContext(AnalyticsContext);

  function sendDeliveryForecastExportMail(emailId: string) {
    if (emailIsValid(emailId)) {
      setEmailSendingProgress(true);
      axios
        .get(`api/subscription-contract-details/export-delivery-forecast`, {
          params: {
            emailId: emailId,
          },
        })
        .then(() => {
          cleanupBeforeModalClose();
          setEmailSuccessAlert(true);
        })
        .catch(() => {
          cleanupBeforeModalClose();
          setEmailFailAlert(true);
        });
    }
  }

  function emailIsValid(emailId: string) {
    if (
      /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
        emailId,
      )
    ) {
      setEmailValidity(true);
      return true;
    } else {
      setEmailValidity(false);
      return false;
    }
  }

  function cleanupBeforeModalClose() {
    setEmailValidity(true);
    setEmailSendingProgress(false);
    setEmailValidity(false);
    setIsModalOpen(false);
    setBlurred(false);
    setInputValueForTestEmailId('');
  }

  return (
    <Card>
      <CardBody>
        {productDeliveryAnalyticsList.length > 0 && (
          <>
            <CardTitle style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexDirection: 'row' }}>
              <div>
                Product Delivery Forecasting
                <DateRangeText />
              </div>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
                <Button color="primary" className="ladda-button undefined btn btn-shadow " onClick={() => setIsModalOpen(!isModalOpen)}>
                  Export
                </Button>
              </div>
            </CardTitle>
            <Modal isOpen={isModalOpen} toggle={() => setIsModalOpen(!isModalOpen)} backdrop>
              <ModalHeader>Export Delivery Forecast List</ModalHeader>
              <ModalBody>
                <Label>Email Address</Label>
                <Input
                  type="email"
                  invalid={!emailValidity}
                  onBlur={event => {
                    setInputValueForTestEmailId(event.target.value);
                    emailIsValid(event.target.value);
                    setBlurred(true);
                  }}
                  onInput={event => {
                    if (blurred) {
                      setInputValueForTestEmailId((event.target as HTMLInputElement).value);
                      emailIsValid((event.target as HTMLInputElement).value);
                    }
                  }}
                  placeholder="Please enter an email address here"
                  style={{
                    width: '100%',
                  }}
                />
                <FormFeedback>Please enter a valid email address</FormFeedback>
                <br />
              </ModalBody>
              <ModalFooter>
                <Button
                  color="secondary"
                  onClick={() => {
                    cleanupBeforeModalClose();
                  }}
                >
                  Cancel
                </Button>
                <MySaveButton
                  onClick={() => {
                    sendDeliveryForecastExportMail(inputValueForTestEmailId);
                  }}
                  text="Send Email"
                  updating={emailSendingProgress}
                  updatingText={'Sending'}
                />
              </ModalFooter>
            </Modal>

            <SweetAlert
              title="Export Request Submitted"
              confirmButtonColor=""
              show={emailSuccessAlert}
              text="Export may take time. Rest assured, once it's processed, it will be emailed to you."
              type="success"
              onConfirm={() => setEmailSuccessAlert(false)}
            />
            <SweetAlert
              title="Failed"
              confirmButtonColor=""
              show={emailFailAlert}
              type="error"
              onConfirm={() => setEmailFailAlert(false)}
            />
            <Table>
              <thead>
                <tr>
                  <th>
                    <span>Product</span>
                    <span className="text-primary">{` => Variant`}</span>
                  </th>
                  <th>Next 7 Days</th>
                  <th>Next 30 Days</th>
                  <th>Next 90 Days</th>
                  <th>Next 365 Days</th>
                </tr>
              </thead>
              <tbody>
                {productDeliveryAnalyticsList?.map((productDeliveryAnalytics, index) => (
                  <tr>
                    <th scope="row">
                      <span>{productDeliveryAnalytics.title}</span>
                      <span className="text-primary">
                        {productDeliveryAnalytics.variantTitle ? ` => ${productDeliveryAnalytics.variantTitle}` : ``}
                      </span>
                    </th>
                    <td>{productDeliveryAnalytics?.deliveryInNext7Days}</td>
                    <td>{productDeliveryAnalytics?.deliveryInNext30Days}</td>
                    <td>{productDeliveryAnalytics?.deliveryInNext90Days}</td>
                    <td>{productDeliveryAnalytics?.deliveryInNext365Days}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </>
        )}
      </CardBody>
    </Card>
  );
}
