import React, {Component} from 'react'
import axios from 'axios'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';

export default class AutoSubscriptionPlanCreate extends Component {

  state = {
    file: '',
    resultMessage: '',
    formdata: null,
    loading: false,
    errorList: [],
    processedIdList: [],
    alreadyProcessedIdList: [],
    missingHeaderList: [],
    customerDataMissingHeadersList: [],
    importType: '',
  };

  componentDidMount = () => {
  //   const {file} = this.props;
  //   this.setState({file})
  };


  saveEntity = (event, errors, values) => {
    this.setState({loading: true})

    axios.post(`/api/miscellaneous/bulk-subscription-groups?planName=${encodeURIComponent(values.subscriptionPlanName)}&existingPlanName=${encodeURIComponent(values.existingPlanName)}`, {...values.subscriptionPlans, productIds: values.productIds}, {timeout: 60000000000, headers: {"Content-Type": "application/json"}})
      .then(res => {
        console.log("bulk upload example"+res)
        this.setState({loading: false})
        if (res.data === "success - bulk subscription created") {
            this.setState({resultMessage: "success - bulk subscription created"})
        } else {
          this.setState({resultMessage: "Error while creating subscription plan"})
        }
      })
  };

  render() {
    return (
      <Row className="justify-content-center">
        <Col md="8">
        {this.state.loading ? (
            <p>Creating subscription plan...</p>
          ) : (
        <AvForm onSubmit={this.saveEntity}>
              <AvGroup>
                <Label id="subscriptionPlanName" for="subscriptionPlanName">
                  New Subscription Plan Name
                </Label>
                <AvField id="subscriptionPlanName" type="text" defaultValue="Subscription -" name="subscriptionPlanName" />
              </AvGroup>

              <AvGroup>
                <Label id="existingPlanName" for="existingPlanName">
                  Existing Plan Name
                </Label>
                <AvField id="existingPlanName" type="text" name="existingPlanName" />
              </AvGroup>

              <AvGroup>
                <Label id="productIds" for="productIds">
                  Product Ids (Optional)
                </Label>
                <AvField id="productIds" type="textarea" placeholder="Comma separated list of product ids" name="productIds" rows={8} />
              </AvGroup>

              {/* <AvGroup>
                <Label id="subscriptionPlans" for="subscriptionPlans">
                  Frequency Plan List Json
                </Label>
                <AvField id="subscriptionPlans" type="textarea" defaultValue={`{
  "subscriptionPlans" : [ ]
}`} rows={8} name="subscriptionPlans" />
              </AvGroup> */}
              <Button color="primary" id="save-entity" type="submit">
                <FontAwesomeIcon icon="save" />
                &nbsp; Create
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    )
  }
}
