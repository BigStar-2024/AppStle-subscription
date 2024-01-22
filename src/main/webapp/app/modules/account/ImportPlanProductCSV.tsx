import React, { Component, useEffect, useState } from 'react'
import axios from 'axios'
import { Row, Col, Button, Alert, Table, Form, FormGroup, Input } from 'reactstrap';
import Papa from 'papaparse';
import { bulkAttachProductsInSubscriptionGroups } from 'app/entities/subscription-group/subscription-group.reducer';
import { connect } from 'react-redux';
import { IRootState } from 'app/shared/reducers';

export interface IImportPlanProductCSVProps extends StateProps, DispatchProps {}

export const ImportPlanProductCSV = (props: IImportPlanProductCSVProps) => {

  const [formdata, setFormdata] = useState(new FormData());
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(props.loading);
  }, [props.loading]);

  const addFile = (filename, { target: { files } }) => {
    console.log(files[0]);
    const data = formdata;
    data.set(filename, files[0]);

    setFormdata(data)
  };

  const uploadFile = () => {
    //state.importType
    console.log(JSON.stringify(formdata));
    const formData = formdata;
    props.bulkAttachProductsInSubscriptionGroups(formData);
  };

    return (
      <Row className="justify-content-center">
        <Col md="8">

          <h4 id="transfer-title" className="my-2">
            Add Product CSV
          </h4>

          <div className="d-flex">
            <div>
              <label>Upload Product Data </label>
              <input type="file" className="ml-3" onChange={e => addFile('product-data', e)} />
              <br />
            </div>
          </div>
          <br />
          <br />
          <Button color="primary" className="mt-3"
            onClick={uploadFile}>Upload {props.loading === true ? "Loading..." : ""}</Button>
          <br />
          <br />
        </Col>
      </Row>
    )
}

const mapStateToProps = ({ subscriptionGroup }: IRootState) => ({
  loading: subscriptionGroup.updating,
});


const mapDispatchToProps = { bulkAttachProductsInSubscriptionGroups };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ImportPlanProductCSV);
