import React, { Component } from 'react'
import axios from 'axios'
import { Row, Col, Button, Alert, Table, Form, FormGroup, Input, Label } from 'reactstrap';
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Papa from 'papaparse';
const SUBSCRIPTION_DATA_DEFAULT_HEADERS_V1 = ["ID", "Status", "Delivery first name", "Delivery last name", "Delivery address 1", "Delivery address 2", "Delivery province code", "Delivery city", "Delivery zip", "Delivery country code", "Delivery phone", "Shipping Price", "Next order date", "Billing interval type", "Billing interval count", "Delivery interval type", "Delivery interval count", "Variant quantity", "Variant price", "Variant ID", "Customer Email"];
const SUBSCRIPTION_DATA_DEFAULT_HEADERS_V2 = ["ID", "Status", "Customer ID", "Delivery first name", "Delivery last name", "Delivery address 1", "Delivery address 2", "Delivery province code", "Delivery city", "Delivery zip", "Delivery country code", "Delivery phone", "Shipping Price", "Next order date", "Billing interval type", "Billing interval count", "Delivery interval type", "Delivery interval count", "Variant quantity", "Variant price", "Variant ID", "Customer Email"];
export default class ImportCSV extends Component<any>  {

  state = {
    file: '',
    resultMessage: '',
    formdata: new FormData(),
    loading: false,
    errorList: [],
    processedIdList: [],
    alreadyProcessedIdList: [],
    missingHeaderList: [],
    customerDataMissingHeadersList: [],
    importType: 'stripe',
    headerMappingList: [],
    sheetColumn: [],
    selectedValue: [],
    headers: SUBSCRIPTION_DATA_DEFAULT_HEADERS_V1,
    shopPaymentInfo: {},
    subscriptionDataKey: '',
    customerDataKey: '',
    actual: false
  };

  importCSV = () => {
    const { subscriptionDataKey } = this.state;
    this.setState({ loading: true })
    axios.get(`/api/bulk-automations/download-import-data-file?key=${encodeURIComponent(subscriptionDataKey)}`).then(response => {
      this.setState({ loading: false })
      const {data} = response;
      if(data && data.length > 0) {
        Papa.parse(data, {
          complete: this.updateData,
          header: false
        });
      } else {
        alert("Incorrect key or No data found for subscription data file")
      }
    });

  };

  updateData = (result) => {
    var data = result.data[0];
    const headerMappingList = [];
    this.state.headers.forEach((systemColumnHeader) => {
        const headerMapping = {systemColumnHeader, 'sheetColumnOption': ''};
        data.forEach((sheetColumnOption) => {
          if(systemColumnHeader.toLowerCase() === sheetColumnOption.toLowerCase()) {
            headerMapping.sheetColumnOption = sheetColumnOption;
          }
        }
        )
        headerMappingList.push(headerMapping);
      }
    )
    this.setState({ headerMappingList, sheetColumn: data })
  }

  componentDidMount = () => {
    const { file } = this.props;
    this.setState({ file })
    axios.get("/api/shop-payment-info")
          .then((res) => this.setState({shopPaymentInfo: res?.data}))
  };
  handleChange = (e, index) =>{
    const headerMappingList = [...this.state.headerMappingList]
    headerMappingList[index][`sheetColumnOption`] = e.target.value;
    this.state.headerMappingList
    this.setState({headerMappingList});
  }

  updateImportType = (event) => {
    /*let test: string = event.target.value;
    let formdata = this.state.formdata;
    formdata.importType = test
    this.setState({formdata: formdata})*/
    let test: string = event.target.value;
    const headers = test == 'v2' ? SUBSCRIPTION_DATA_DEFAULT_HEADERS_V2 : SUBSCRIPTION_DATA_DEFAULT_HEADERS_V1;
    this.setState({ headers, importType: test })
    this.importCSV();
  };

  uploadFile = () => {
    //this.state.importType

    const search = this?.props?.location?.search;
    const apiName = this.state.actual ? `import-csv` : `import-csv-validation`;
    console.log(JSON.stringify(this.state.formdata));
    const formData = this.state.formdata;
    formData.append(`headerMappingList`, JSON.stringify(this.state.headerMappingList));
    formData.append(`subscriptionDataS3Key`, this.state.subscriptionDataKey);

    if(this.state.customerDataKey) {
      formData.append(`customerDataS3Key`, this.state.customerDataKey);
    }

    this.setState({ loading: true })
    axios.post(`/api/bulk-automations/${apiName}?importType=${this.state.importType}`, formData, { timeout: 60000000000 })
      .then(res => {
        console.log(res)
        this.setState({ loading: false })
        if (res.data.size = 1) {
          this.setState({ errorList: res.data['errorList'] })
          this.setState({ processedIdList: res.data['processedId'] })
          this.setState({ alreadyProcessedIdList: res.data['alreadyProcessedId'] })
          this.setState({ missingHeaderList: res.data['missingHeaders'] })
          this.setState({ customerDataMissingHeadersList: res.data['customerDataMissingHeaders'] })
          if (!res.data['missingHeaders'].length)
            this.setState({ ...this.state, resultMessage: "Imported Successfully" })
        } else {
          this.setState({ ...this.state, resultMessage: "Error while importing" })
        }
      })
      .catch(err => {
        console.log(err)
        this.setState({ loading: false })
        this.setState({ ...this.state, resultMessage: "Error while importing" })
      })
  };

  render() {
    return (
      <Row className="justify-content-center">
        <Col md="12">

          <h4 id="transfer-title" className="my-2">
            Add Subscription CSV
          </h4>
          {this.state.resultMessage ? (
            <Alert color={"primary"}>
              <b className="text-center">{this.state.resultMessage}</b>
            </Alert>
          ) : (
            ''
          )}
          <Row>
            <Col md="6">
              <FormGroup>
                <Label>Upload Subscription Data (S3 Key)</Label>
                <Input
                  type="text"
                  name="subscriptionDataKey"
                  onInput={e => this.setState({ subscriptionDataKey: e.target.value })}
                  onBlur={e => this.setState({ subscriptionDataKey: e.target.value })}
                />
              </FormGroup>
              <FormGroup>
                <Label>Upload Customer Data (S3 Key)</Label>
                <Input
                  type="text"
                  name="customerDataKey"
                  onInput={e => this.setState({ customerDataKey: e.target.value })}
                  onBlur={e => this.setState({ customerDataKey: e.target.value })}
                />
              </FormGroup>

            </Col>
            <Col md="6">
              {
                this.state?.shopPaymentInfo &&
                <div>
                  <p><b>Stripe/Braintree Gateway Status</b>: {this.state?.shopPaymentInfo['legacySubscriptionGatewayEnabled']?.toString()}</p>
                  <p><b>Authorize.net Gateway Status</b>: {this.state?.shopPaymentInfo['eligibleForSubscriptionMigration']?.toString()}</p>
                  <p><b>PayPal Gateway Status</b>: {this.state?.shopPaymentInfo['paypalExpressSubscriptionGatewayStatus']?.toString()}</p>
                </div>
              }
            </Col>
          </Row>

          <br />
          <label className="mt-4 mb-3 mr-3">Import Type</label>
          <select name="importType" id="importType" onChange={this.updateImportType}>
            <option value="stripe">Stripe</option>
            <option value="braintree">Braintree</option>
            <option value="paypal">PayPal</option>
            <option value="authorize_net">Authorize.Net</option>
            <option value="v2">V2</option>
          </select>
              <br /><br />
          <Button color="info" className="mt-3"
            disabled={this.state.loading === true}
            onClick={this.importCSV}>{this.state.loading === true ? "Loading..." : "Map Headers"}</Button>
          <br /><br />
              {
                this.state.sheetColumn.length > 0 &&
              <Row>
                <Col sm="6">
                  <h3>Default Column</h3>
                  <Table borderless className="mb-0">
                    <tbody>
                      {
                        this.state.headers.map((systemColumnHeader) =>
                          <tr>
                            <td height="62">{systemColumnHeader}</td>
                          </tr>
                        )
                      }
                    </tbody>
                  </Table>
                </Col>
                <Col sm="6">
                  <h3>Sheet Column</h3>
                  <Form>
                    <Table borderless className="mb-0">
                      <tbody>
                      {
                        this.state.headers.map((systemColumnHeader, index) =>
                            <tr>
                              <td>
                                  <Input type="select" name="select" onChange={(e) => this.handleChange(e, index)}>
                                    <option value="">No matching column</option>
                                    {
                                      this.state.sheetColumn.map((sheetColumnOption) =>
                                        <option selected={systemColumnHeader.toLowerCase() === sheetColumnOption.toLowerCase()} value={sheetColumnOption}>{sheetColumnOption} </option>
                                      )
                                    }
                                  </Input>
                              </td>
                            </tr>
                          )
                        }
                      </tbody>
                    </Table>
                  </Form>
                </Col>
              </Row>
            }
          <br />

          <Row>
            <Col>
              <div className="form-group-checkbox">
                <input type="checkbox" id="actual" checked={this.state.actual} onChange={e => this.setState({ actual: e.target.checked })} />
                <label for="actual" className="checkbox-label ml-2">Is Actual Import?</label>
              </div>
            </Col>
          </Row>

          <Button color="primary" className="mt-3"
            disabled={this.state.loading === true}
            onClick={this.uploadFile}>{this.state.loading === true ? "Loading..." : "Upload"}</Button>
          <br />
          <br />
          <>
            <h5 id="transfer-title" className="my-2">
              Missing headers List
            </h5>
            {this.state.missingHeaderList.length > 0 ? (
              this.state.missingHeaderList.map((alreadyProcessed, i) => (
                <h6>{alreadyProcessed}</h6>
              ))
            ) : (
              ''
            )}
            <br />
            <br />
            <h5 id="transfer-title" className="my-2">
              Already Processed Id List
            </h5>
            {this.state.alreadyProcessedIdList.length > 0 ? (
              this.state.alreadyProcessedIdList.map((alreadyProcessed, i) => (
                <h6>{alreadyProcessed}</h6>
              ))
            ) : (
              ''
            )}
            <br />
            <br />
            <h5 id="transfer-title" className="my-2">
              Processed Id List
            </h5>
            {this.state.processedIdList.length > 0 ? (
              this.state.processedIdList.map((processedId, i) => (
                <h6>{processedId}</h6>
              ))
            ) : (
              ''
            )}
            <br />
            <br />
            <h5 id="transfer-title" className="my-2">
              Error List
            </h5>
            {this.state.errorList.length > 0 ? (
              this.state.errorList.map((error, i) => (
                <h6>{error}</h6>
              ))
            ) : (
              ''
            )}
          </>
        </Col>
      </Row>
    )
  }
}
