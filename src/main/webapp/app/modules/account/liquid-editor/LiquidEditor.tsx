/* tslint:disable */
import * as React from "react";

import {connect} from "react-redux";
import {
  Row,
  Col,
  Card,
  CardHeader,
  CardBody,
  CardFooter,
  Table,
  Pagination,
  PaginationItem,
  PaginationLink,
  Button,
  Form,
  Badge,
  Input,
  FormGroup,
  Label
} from "reactstrap";

import Select from "react-select";
import {getAsset, getAssetKeys, getThemeDetails, saveAsset} from "app/modules/account/liquid-editor/assets";
import { Editor } from "@monaco-editor/react";

class LiquidEditor extends React.Component<any, any> {
  constructor(props) {
    super(props);
    this.state = {
      assetKey: "",
      asset: "",
      assetKeys: [],
      selectedOption: {},
      editorValue: ''
    };
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillMount() {
  }

  editorDidMount() {
    const { asset } = this.props;
    if(asset == null) {
      this.setState({editorValue: ''});
    }
    this.setState({editorValue: asset});
  }

  componentDidMount(): void {
    this.props.getAssetKeys();
    this.props.getThemeDetails();
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.asset !== this.props.asset) {
      this.setState({asset: nextProps.asset, editorValue: nextProps.asset});
    }
    if (nextProps.assetKeys.length != this.props.assetKeys.length) {
      var mappedAssetKeys = [];
      const assetKeys = nextProps.assetKeys;
      for (let key in assetKeys) {
        var assetKey = {value: "", label: ""};
        assetKey.label = assetKeys[key];
        assetKey.value = assetKeys[key];
        mappedAssetKeys.push(assetKey);
      }
      this.setState({assetKeys: mappedAssetKeys});
    }
  }

  handleChange = (selectedOption) => {
    if (selectedOption == null) {
      this.setState({selectedOption: selectedOption});
      return;
    }
    this.props.getAsset(selectedOption.value);
    this.setState({assetKey: selectedOption.value, selectedOption: selectedOption});
  }

  render() {
    const {themeName, account} = this.props;
    const {assetKey, asset, assetKeys, selectedOption} = this.state;
    return (
      <div className="animated fadeIn">
        <Row>
          <Col>Theme Name: {themeName}</Col>
        </Row>
        <Row>
          <Col>Shop: {account.login}</Col>
        </Row>
        <Row>
          <Col>
            <Card>
              <Select
                value={selectedOption}
                onChange={this.handleChange}
                options={assetKeys}
              />

            </Card>
          </Col>
        </Row>
        <Row>
          <Col>
            <Card>
              <Form onSubmit={this.onSubmit}>
                <CardHeader>
                  <i className="fa fa-align-justify"/>
                </CardHeader>
                <CardBody>
                  {/*<AceEditor
                    mode="html"
                    theme="monokai"
                    name="liquid-editor"
                    width="1100px"
                    onChange={event => {
                      this.setState({
                        asset: event
                      });
                    }}
                    fontSize={14}
                    showPrintMargin={false}
                    showGutter={false}
                    highlightActiveLine={true}
                    value={asset}
                    setOptions={{
                      enableBasicAutocompletion: true,
                      enableLiveAutocompletion: true,
                      enableSnippets: true,
                      showLineNumbers: true,
                      tabSize: 2
                    }}
                  />*/}

                  <Editor
                    height="90vh"
                    value={this.state.editorValue}
                    onChange={(ev, value) => {
                      this.setState({
                        asset: value
                      });
                      //this.handleEditorChange(ev, value);
                    }}
                    language="html"
                    onMount={() => this.editorDidMount()}
                  />

                </CardBody>
                <CardFooter className="text-right">
                  <Button color="primary">Save</Button>
                </CardFooter>
              </Form>
            </Card>
          </Col>
        </Row>
      </div>
    );
  }

  onSubmit(event) {
    event.preventDefault();
    const {assetKey, asset} = this.state;
    this.props.saveAsset(assetKey, asset);
    alert("Your changes were saved successfully.");
  }
}

const mapStateToProps = storeState => ({
  asset: storeState.assets.asset,
  assetKeys: storeState.assets.assetKeys,
  themeName: storeState.assets.themeName,
  account: storeState.authentication.account,
});

const mapDispatchToProps = {
  getAsset,
  saveAsset,
  getAssetKeys,
  getThemeDetails
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LiquidEditor);
