import {faCopy} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import React, {Component} from 'react';
import {CopyToClipboard} from 'react-copy-to-clipboard';
import {connect} from 'react-redux';
import {Button, Col, Input, InputGroup, InputGroupAddon, Label, Row} from 'reactstrap';
import {getEntity, regenerateApiKeyEntity} from "app/entities/shop-info/shop-info.reducer";
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';

export class API extends Component {
    constructor(props) {
        super(props);
        this.state = {
            copied: false
        };
    }

    componentDidMount() {
        this.props.getEntity(-1);
    }

    onCopy = () => {
        this.setState({copied: true});
    };

    regenerateKey = () => {
        this.props.regenerateApiKeyEntity()
    }

    render() {
        const apiKey = this.props.shopInfo.apiKey;
        return (
            <Row>
                <Col md={10}>
                    <Label>Api Key</Label>
                    <InputGroup>
                        <Input disabled value={apiKey}/>

                        <InputGroupAddon addonType="append">
                            <CopyToClipboard onCopy={this.onCopy} text={apiKey}>
                                <Button color="primary">
                                    <FontAwesomeIcon icon={faCopy}/>
                                    {
                                        this.state.copied && " Copied"
                                    }
                                </Button>
                            </CopyToClipboard>
                        </InputGroupAddon>
                    </InputGroup>
                </Col>
                <Col md={2}>
                    <Label>&nbsp;</Label>
                    <MySaveButton
                        text="Re-generate key"
                        updating={this.props.updatingAPIKey}
                        updatingText={'Processing'}
                        className="btn-primary w-100"
                        onClick={this.regenerateKey}
                        style={{height: "38px"}}
                    >
                        Re-generate key
                    </MySaveButton>
                </Col>
            </Row>
        );
    }
}

const mapStateToProps = state => ({
    shopInfo: state.shopInfo.entity,
    updatingAPIKey: state.shopInfo.updatingAPIKey
});

const mapDispatchToProps = {getEntity, regenerateApiKeyEntity};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(API);
